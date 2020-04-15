package com.example.utatracker;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class Alarm {
    String mDate;
    String mTime;
    String mLine;
    String mStartStation;
    String mEndStation;
    String mDirection;
    String mEnabled;

    public Alarm(String date, String time, String line, String startStation, String endStation, String direction,String enabled) {
        this.mDate = date;
        this.mTime = time;
        this.mLine = line;
        this.mStartStation = startStation;
        this.mEndStation = endStation;
        this.mDirection = direction;
        this.mEnabled = enabled;
    }
    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Alarm fromString(String s) {
        return new Gson().fromJson(s, Alarm.class);
    }
}
