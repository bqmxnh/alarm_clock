package com.example.alarmapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarm_database";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ALARMS = "alarms";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_REPEAT_DAILY = "repeatDaily";
    public static final String COLUMN_REPEAT_WEEKLY = "repeatWeekly";
    public static final String COLUMN_ENABLED = "enabled";

    public AlarmDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ALARMS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TIME + " INTEGER, "
                + COLUMN_DATE + " INTEGER, "
                + COLUMN_REPEAT_DAILY + " INTEGER, "
                + COLUMN_REPEAT_WEEKLY + " INTEGER, "
                + COLUMN_ENABLED + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }
}
