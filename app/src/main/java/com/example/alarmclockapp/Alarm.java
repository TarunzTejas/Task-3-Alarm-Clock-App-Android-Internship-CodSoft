package com.example.alarmclockapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarms")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int hour;
    public int minute;
    public boolean isActive;
    public String someString; // store URI string of alarm tone

    public Alarm(int hour, int minute, boolean isActive, String someString) {
        this.hour = hour;
        this.minute = minute;
        this.isActive = isActive;
        this.someString = someString;
    }

}


