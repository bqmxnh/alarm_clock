package com.example.alarmapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    private Date selectedDate;
    private MaterialButton selectDateButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        selectDateButton = findViewById(R.id.selectDateButton);
        selectDateButton.setOnClickListener(v -> openDatePickerDialog());

        MaterialButton saveAlarmButton = findViewById(R.id.saveAlarmButton);

        Switch repeatDailySwitch = findViewById(R.id.repeatDailySwitch);
        Switch repeatWeeklySwitch = findViewById(R.id.repeatWeeklySwitch);

        MaterialButton cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> finish());
    }

    private void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year1, month1, dayOfMonth);
                    selectedDate = selectedCalendar.getTime();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String dateString = sdf.format(selectedDate);
                    selectDateButton.setText(dateString);
                }, year, month, day);
        datePickerDialog.show();
    }
}
