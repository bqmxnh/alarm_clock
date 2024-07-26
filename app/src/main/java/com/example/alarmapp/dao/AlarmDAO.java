package com.example.alarmapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alarmapp.database.AlarmDatabaseHelper;
import com.example.alarmapp.model.Alarm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmDAO {

    private final AlarmDatabaseHelper dbHelper;

    public AlarmDAO(Context context) {
        dbHelper = new AlarmDatabaseHelper(context);
    }

    public long insertAlarm(Alarm alarm) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AlarmDatabaseHelper.COLUMN_TIME, alarm.getTime().getTime());
        values.put(AlarmDatabaseHelper.COLUMN_DATE, alarm.getDate().getTime());
        values.put(AlarmDatabaseHelper.COLUMN_REPEAT_DAILY, alarm.isRepeatDaily() ? 1 : 0);
        values.put(AlarmDatabaseHelper.COLUMN_REPEAT_WEEKLY, alarm.isRepeatWeekly() ? 1 : 0);
        values.put(AlarmDatabaseHelper.COLUMN_ENABLED, alarm.isEnabled() ? 1 : 0);

        long id = db.insert(AlarmDatabaseHelper.TABLE_ALARMS, null, values);
        db.close();
        return id;
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AlarmDatabaseHelper.TABLE_ALARMS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_ID)));
                alarm.setTime(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_TIME))));
                alarm.setDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_DATE))));
                alarm.setRepeatDaily(cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_REPEAT_DAILY)) == 1);
                alarm.setRepeatWeekly(cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_REPEAT_WEEKLY)) == 1);
                alarm.setEnabled(cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_ENABLED)) == 1);

                alarms.add(alarm);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return alarms;
    }

    public Alarm getAlarm(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AlarmDatabaseHelper.TABLE_ALARMS, null, AlarmDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Alarm alarm = new Alarm();
            alarm.setId(cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_ID)));
            alarm.setTime(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_TIME))));
            alarm.setDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_DATE))));
            alarm.setRepeatDaily(cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_REPEAT_DAILY)) == 1);
            alarm.setRepeatWeekly(cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_REPEAT_WEEKLY)) == 1);
            alarm.setEnabled(cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDatabaseHelper.COLUMN_ENABLED)) == 1);

            cursor.close();
            db.close();
            return alarm;
        } else {
            return null;
        }
    }

    public int updateAlarm(Alarm alarm) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AlarmDatabaseHelper.COLUMN_TIME, alarm.getTime().getTime());
        values.put(AlarmDatabaseHelper.COLUMN_DATE, alarm.getDate().getTime());
        values.put(AlarmDatabaseHelper.COLUMN_REPEAT_DAILY, alarm.isRepeatDaily() ? 1 : 0);
        values.put(AlarmDatabaseHelper.COLUMN_REPEAT_WEEKLY, alarm.isRepeatWeekly() ? 1 : 0);
        values.put(AlarmDatabaseHelper.COLUMN_ENABLED, alarm.isEnabled() ? 1 : 0);

        int rowsUpdated = db.update(AlarmDatabaseHelper.TABLE_ALARMS, values, AlarmDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(alarm.getId())});
        db.close();
        return rowsUpdated;
    }

    public int deleteAlarm(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(AlarmDatabaseHelper.TABLE_ALARMS, AlarmDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }
}
