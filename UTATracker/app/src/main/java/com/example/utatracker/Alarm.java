package com.example.utatracker;

import androidx.annotation.NonNull;

public class Alarm {
    public static String mDate;
    public static String mTime;
    public static String mLine;
    public static String mStartStation;
    public static String mEndStation;
    public static String mDirection;
    public static String mActive;

    Alarm(){

    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    public static Alarm fromString(String s) {
        return null;
    }
}
