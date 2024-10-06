package com.example.alarmapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alarmapp.R;
import com.example.alarmapp.dao.AlarmDAO;
import com.example.alarmapp.model.Alarm;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    public interface OnItemClickListener {
        void onItemClick(Alarm alarm);
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private AlarmDAO alarmDAO;
    private OnItemClickListener onItemClickListener;

    public AlarmAdapter(@NonNull Context context, @NonNull List<Alarm> alarms, @NonNull AlarmDAO alarmDAO, OnItemClickListener onItemClickListener) {
        super(context, 0, alarms);
        this.alarmDAO = alarmDAO;
        this.onItemClickListener = onItemClickListener;
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
        TextView repeatTextView = convertView.findViewById(R.id.repeatTextView);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch alarmSwitch = convertView.findViewById(R.id.alarmSwitch);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        if (alarm != null) {
            timeTextView.setText(timeFormat.format(alarm.getTime()));
            dateTextView.setText(dateFormat.format(alarm.getDate()));
            alarmSwitch.setChecked(alarm.isEnabled());

            if (alarm.isRepeatDaily()) {
                repeatTextView.setText(R.string.repeat_daily);
            } else if (alarm.isRepeatWeekly()) {
                repeatTextView.setText(R.string.repeat_weekly);
            } else {
                repeatTextView.setText(R.string.once);
            }

            alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                alarm.setEnabled(isChecked);
                updateAlarm(alarm);
            });

            deleteButton.setOnClickListener(v -> deleteAlarm(alarm));

            convertView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(alarm);
                }
            });
        }

        return convertView;
    }

    private void deleteAlarm(Alarm alarm) {
        alarmDAO.deleteAlarm(alarm);
        remove(alarm);
        notifyDataSetChanged();
    }

    private void updateAlarm(Alarm alarm) {
        alarmDAO.updateAlarm(alarm);
        notifyDataSetChanged();
    }
}
