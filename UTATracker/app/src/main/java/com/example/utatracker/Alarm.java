package com.example.utatracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utatracker.data.AlarmReminderContract;

public class Alarm {
    private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;

    public View newView(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.train_alarm_item, parent, false);

    }

    public void bindView(View view, Context context) {

        mTitleText = (TextView) view.findViewById(R.id.recycle_title);
        mDateAndTimeText = (TextView) view.findViewById(R.id.recycle_date_time);
        mRepeatInfoText = (TextView) view.findViewById(R.id.recycle_repeat_info);


        String date = AlarmReminderContract.AlarmReminderEntry.KEY_DATE;
        String time = AlarmReminderContract.AlarmReminderEntry.KEY_TIME;
        String line = AlarmReminderContract.AlarmReminderEntry.KEY_LINE;
        String startStation = AlarmReminderContract.AlarmReminderEntry.KEY_START_STATION;
        String endStation = AlarmReminderContract.AlarmReminderEntry.KEY_END_STATION;
        String direction = AlarmReminderContract.AlarmReminderEntry.KEY_DIRECTION;
        String alertTime = AlarmReminderContract.AlarmReminderEntry.KEY_ALERT_TIME;

        String active = AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE;
        String dateTime = date + " " + time;

        setReminderDateTime(dateTime);
//        setReminderRepeatInfo(line, startStation, endStation, direction);
//        setActiveImage(active);

    }

    public void setReminderTitle(String title) {
        mTitleText.setText(title);
        String letter = "A";

        if(title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }
    }

    public void setReminderDateTime(String datetime) {
        mDateAndTimeText.setText(datetime);
    }

    // Set repeat views
    public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
        if(repeat.equals("true")){
            mRepeatInfoText.setText("Every " + repeatNo + " " + repeatType + "(s)");
        }else if (repeat.equals("false")) {
            mRepeatInfoText.setText("Repeat Off");
        }
    }
}
