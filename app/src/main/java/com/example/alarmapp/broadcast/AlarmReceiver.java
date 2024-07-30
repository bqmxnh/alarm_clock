package com.example.alarmapp.broadcast;

import static com.example.alarmapp.view.MainActivity.REQUEST_SMS_PERMISSION;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.example.alarmapp.R;
import com.example.alarmapp.database.AlarmDatabase;
import com.example.alarmapp.model.Alarm;
import com.example.alarmapp.view.MainActivity;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "alarm_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "AlarmReceiver";
    private static final String PHONE_NUMBER = "0741233074";  // Replace with actual phone number
    private static final String SMS_MESSAGE_START = "Your alarm is going off!";
    private static final String SMS_MESSAGE_END = "Your alarm has ended!";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Alarm received");

        long alarmId = intent.getLongExtra("alarmId", -1);
        if (alarmId != -1) {
            Log.d(TAG, "onReceive: Alarm ID " + alarmId);

            AlarmDatabase db = AlarmDatabase.getInstance(context);
            Alarm alarm = db.alarmDAO().getAlarm((int) alarmId);

            if (alarm != null && alarm.isEnabled()) {
                Log.d(TAG, "onReceive: Alarm is enabled");

                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED) {
                    showNotification(context, alarmId);
                } else {
                    requestNotificationPermission(context);
                }

                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    sendSms(SMS_MESSAGE_START);
                    sendDelayedSms(context); // 60000 ms = 1 minute
                } else {
                    requestSmsPermission(context);
                }

                if (alarm.isRepeatDaily()) {
                    rescheduleAlarm(context, alarmId, alarm, AlarmManager.INTERVAL_DAY);
                } else if (alarm.isRepeatWeekly()) {
                    rescheduleAlarm(context, alarmId, alarm, AlarmManager.INTERVAL_DAY * 7);
                } else {
                    alarm.setEnabled(false);
                    db.alarmDAO().updateAlarm(alarm);
                }
            } else {
                Log.d(TAG, "onReceive: Alarm is null or not enabled");
            }
        } else {
            Log.d(TAG, "onReceive: Invalid alarm ID");
        }
    }

    private void showNotification(Context context, long alarmId) {
        try {
            createNotificationChannel(context);

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_alarm)
                    .setContentTitle("Alarm Received")
                    .setContentText("Alarm ID: " + alarmId)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } catch (SecurityException e) {
            // Handle the exception gracefully
            Log.e(TAG, "showNotification: SecurityException", e);
        } catch (Exception e) {
            Log.e(TAG, "showNotification: Exception", e);
        }
    }

    private void createNotificationChannel(Context context) {
        CharSequence name = "Alarm Channel";
        String description = "Channel for alarm notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        } else {
            Log.e(TAG, "createNotificationChannel: NotificationManager is null");
        }
    }

    private void requestNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent permissionIntent = new Intent();
            permissionIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            permissionIntent.putExtra("app_package", context.getPackageName());
            permissionIntent.putExtra("app_uid", context.getApplicationInfo().uid);
            context.startActivity(permissionIntent);
        }
    }

    private void sendSms(String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(AlarmReceiver.PHONE_NUMBER, null, message, null, null);
            Log.d(TAG, "sendSms: SMS sent: " + message);
        } catch (SecurityException e) {
            Log.e(TAG, "sendSms: SecurityException", e);
        } catch (Exception e) {
            Log.e(TAG, "sendSms: Exception", e);
        }
    }

    private void sendDelayedSms(Context context) {
        Handler handler = new Handler(context.getMainLooper());
        handler.postDelayed(() -> sendSms(AlarmReceiver.SMS_MESSAGE_END), 60000);
    }

    private void requestSmsPermission(Context context) {
        ActivityCompat.requestPermissions((MainActivity) context, new String[]{android.Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
    }

    @SuppressLint("ScheduleExactAlarm")
    private void rescheduleAlarm(Context context, long alarmId, Alarm alarm, long interval) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(alarm.getTime());
        calendar.add(Calendar.MILLISECOND, (int) interval);

        Date nextAlarmTime = calendar.getTime();
        alarm.setTime(nextAlarmTime);
        AlarmDatabase db = AlarmDatabase.getInstance(context);
        db.alarmDAO().updateAlarm(alarm);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarmId", alarmId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextAlarmTime.getTime(), pendingIntent);
        }
    }
}
