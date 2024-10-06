package com.example.alarmapp.broadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.alarmapp.R;
import com.example.alarmapp.view.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "ALARM_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        int alarmId = intent.getIntExtra("alarmId", -1);

        // Tạo thông báo khi báo thức kích hoạt
        sendNotification(context, alarmId);
    }

    private void sendNotification(Context context, int alarmId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo Intent để mở MainActivity khi người dùng nhấn vào thông báo
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarmId, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Tạo NotificationChannel (chỉ cần cho Android 8.0 trở lên)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Alarm Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm) // Biểu tượng thông báo
                .setContentTitle("Alarm Triggered")
                .setContentText("Your alarm is going off!") // Nội dung thông báo
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true) // Tự động tắt khi nhấn
                .setContentIntent(pendingIntent); // Intent mở khi nhấn

        // Hiển thị thông báo
        notificationManager.notify(alarmId, builder.build());
    }
}
