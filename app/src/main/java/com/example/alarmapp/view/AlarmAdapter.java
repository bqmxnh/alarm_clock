package com.example.alarmapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alarmapp.R;
import com.example.alarmapp.dao.AlarmDAO;
import com.example.alarmapp.model.Alarm;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private AlarmDAO alarmDAO;

    public AlarmAdapter(@NonNull Context context, @NonNull List<Alarm> alarms, @NonNull AlarmDAO alarmDAO) {
        super(context, 0, alarms);
        this.alarmDAO = alarmDAO;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm, parent, false);
        }

        Alarm alarm = getItem(position);

        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        Switch alarmSwitch = convertView.findViewById(R.id.alarmSwitch);
        Button updateButton = convertView.findViewById(R.id.updateButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        if (alarm != null) {
            timeTextView.setText(timeFormat.format(alarm.getTime()));
            dateTextView.setText(dateFormat.format(alarm.getDate()));
            alarmSwitch.setChecked(alarm.isEnabled());

            alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                alarm.setEnabled(isChecked);
                updateAlarm(alarm);
            });

            updateButton.setOnClickListener(v -> {
                // Handle update button click
                // You could open a new activity to edit the alarm details
                Toast.makeText(getContext(), "Update functionality not implemented", Toast.LENGTH_SHORT).show();
            });

            deleteButton.setOnClickListener(v -> {
                // Delete alarm directly on the UI thread
                deleteAlarm(alarm);
            });
        }

        return convertView;
    }

    private void deleteAlarm(Alarm alarm) {
        // Perform delete operation directly on the UI thread
        alarmDAO.deleteAlarm(alarm);
        // Remove the alarm from the adapter and notify changes
        remove(alarm);
        notifyDataSetChanged();
        //Toast.makeText(getContext(), "Alarm deleted", Toast.LENGTH_SHORT).show();
    }

    private void updateAlarm(Alarm alarm) {
        // Perform update operation directly on the UI thread
        alarmDAO.updateAlarm(alarm);
        // Notify the adapter that data has changed
        notifyDataSetChanged();
    }
}
