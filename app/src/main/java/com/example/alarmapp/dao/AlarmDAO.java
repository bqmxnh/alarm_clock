package com.example.alarmapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.alarmapp.model.Alarm;

import java.util.List;

@Dao
public interface AlarmDAO {

    @Insert
    long insertAlarm(Alarm alarm);

    @Query("SELECT * FROM alarms")
    List<Alarm> getAllAlarms();

    @Query("SELECT * FROM alarms WHERE id = :id")
    Alarm getAlarm(int id);

    @Update
    int updateAlarm(Alarm alarm);

    @Delete
    int deleteAlarm(Alarm alarm);
}
