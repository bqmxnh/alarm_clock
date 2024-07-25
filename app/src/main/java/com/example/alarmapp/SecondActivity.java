package com.example.alarmapp;

import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

}
