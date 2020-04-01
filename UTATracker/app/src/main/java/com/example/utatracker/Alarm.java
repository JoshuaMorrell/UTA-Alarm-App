package com.example.utatracker;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class Alarm {
    public static String mDate;
    public static String mTime;
    public static String mLine;
    public static String mStartStation;
    public static String mEndStation;
    public static String mDirection;
    public static String mActive;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Alarm fromString(String s) {
        return new Gson().fromJson(s, Alarm.class);
    }
}
