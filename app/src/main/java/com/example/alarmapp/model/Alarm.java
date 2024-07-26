package com.example.alarmapp.model;

import java.util.Date;

public class Alarm {
    private int id;
    private Date time;
    private Date date;
    private boolean repeatDaily;
    private boolean repeatWeekly;
    private boolean enabled;

    public Alarm() {}

    public Alarm(Date time, Date date, boolean repeatDaily, boolean repeatWeekly, boolean enabled) {
        this.time = time;
        this.date = date;
        this.repeatDaily = repeatDaily;
        this.repeatWeekly = repeatWeekly;
        this.enabled = enabled;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRepeatDaily() {
        return repeatDaily;
    }

    public void setRepeatDaily(boolean repeatDaily) {
        this.repeatDaily = repeatDaily;
    }

    public boolean isRepeatWeekly() {
        return repeatWeekly;
    }

    public void setRepeatWeekly(boolean repeatWeekly) {
        this.repeatWeekly = repeatWeekly;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
