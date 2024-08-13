package com.example.alarmapp.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.alarmapp.R;
import com.example.alarmapp.database.AlarmDatabase;
import com.example.alarmapp.model.Alarm;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlarmDatabase alarmDatabase;
    private ListView alarmListView;
    private AlarmAdapter alarmAdapter;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    public static final int REQUEST_SMS_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_SMS_PERMISSION);
        }

        alarmDatabase = AlarmDatabase.getInstance(this);
        alarmListView = findViewById(R.id.alarmListView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.my_button).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
            finish();
        });

        loadAlarms();
    }

    private void loadAlarms() {
        List<Alarm> alarms = alarmDatabase.alarmDAO().getAllAlarms();
        alarmAdapter = new AlarmAdapter(this, alarms, alarmDatabase.alarmDAO(), this::openAlarmEditor);
        alarmListView.setAdapter(alarmAdapter);
    }

    private void openAlarmEditor(Alarm alarm) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("alarmId", alarm.getId()); // Pass the alarm ID to SecondActivity
        startActivity(intent);
    }
}
