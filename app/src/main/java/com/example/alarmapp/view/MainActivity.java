package com.example.alarmapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.alarmapp.R;
import com.example.alarmapp.dao.AlarmDAO;
import com.example.alarmapp.model.Alarm;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlarmDAO alarmDAO;
    private ListView alarmListView;
    private AlarmAdapter alarmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        alarmDAO = new AlarmDAO(this);
        alarmListView = findViewById(R.id.alarmListView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.my_button).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        loadAlarms();
    }

    private void loadAlarms() {
        List<Alarm> alarms = alarmDAO.getAllAlarms();
        alarmAdapter = new AlarmAdapter(this, alarms, alarmDAO);
        alarmListView.setAdapter(alarmAdapter);
    }
}
