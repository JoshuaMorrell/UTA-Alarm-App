package com.example.utatracker.data;

import android.provider.BaseColumns;

public class AlarmReminderContract {
    public static final class AlarmReminderEntry implements BaseColumns {
        public static final String KEY_DATE = "date";
        public static final String KEY_TIME = "time";
        public static final String KEY_LINE = "line";
        public static final String KEY_START_STATION = "start_station";
        public static final String KEY_END_STATION = "end_station";
        public static final String KEY_DIRECTION = "direction";
        public static final String KEY_ALERT_TIME = "alert_time";
        public static final String KEY_ACTIVE = "active";
    }
}
