package com.example.alarmapp.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.alarmapp.conversion.Converters;
import com.example.alarmapp.dao.AlarmDAO;
import com.example.alarmapp.model.Alarm;

@Database(entities = {Alarm.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AlarmDatabase extends RoomDatabase {

    private static volatile AlarmDatabase INSTANCE;
    private static final String TAG = "AlarmDatabase";

    public abstract AlarmDAO alarmDAO();

    public static AlarmDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AlarmDatabase.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        AlarmDatabase.class, "alarm_database")
                                // Allow queries on the main thread for debugging purposes
                                .allowMainThreadQueries()
                                .build();
                    } catch (Exception e) {
                        Log.e(TAG, "Error creating database", e);
                    }
                }
            }
        }
        return INSTANCE;
    }
}
