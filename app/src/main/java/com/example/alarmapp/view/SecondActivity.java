package com.example.alarmapp.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmapp.R;
import com.example.alarmapp.dao.AlarmDAO;
import com.example.alarmapp.model.Alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    private AlarmDAO alarmDAO;
    private TimePicker timePicker;
    private Button selectDateButton, saveAlarmButton, cancelButton;
    private Date selectedDate;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        alarmDAO = new AlarmDAO(this);

        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        selectDateButton = findViewById(R.id.selectDateButton);
        saveAlarmButton = findViewById(R.id.saveAlarmButton);
        cancelButton = findViewById(R.id.cancelButton);

        selectDateButton.setOnClickListener(v -> openDatePickerDialog());
        saveAlarmButton.setOnClickListener(v -> saveAlarm());
        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void openDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedDate = calendar.getTime();
                    selectDateButton.setText(dateFormat.format(selectedDate));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveAlarm() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Date time = calendar.getTime();

        // Create a new Alarm object
        Alarm alarm = new Alarm(time, selectedDate, false, false, true);

        // Save the alarm in the database
        long id = alarmDAO.insertAlarm(alarm);
        if (id > 0) {
            Toast.makeText(this, "Alarm saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving alarm", Toast.LENGTH_SHORT).show();
        }
    }
}
