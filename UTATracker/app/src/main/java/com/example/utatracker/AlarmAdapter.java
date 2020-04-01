package com.example.utatracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.utatracker.data.AlarmReminderContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class AlarmAdapter extends ArrayAdapter<String> {
    private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;

    private final Activity context;
    private final ArrayList<Alarm> alarms;

    public AlarmAdapter(@NonNull Activity context, ArrayList<Alarm> alarms) {
        super(context, R.layout.train_alarm_item);
        this.context = context;

        this.alarms = alarms;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.train_alarm_item, null,true);

        TextView titleText = rowView.findViewById(R.id.recycle_title);
        TextView subtitleText = rowView.findViewById(R.id.recycle_date_time);
        ImageView trainIcon = rowView.findViewById(R.id.train_icon);


        titleText.setText(alarms.get(position).mStartStation);
        subtitleText.setText(alarms.get(position).mDate + " " + alarms.get(position).mTime);
        int lineColor = R.color.black;
        if(alarms.get(position).mLine.equals("Red"))
            lineColor = R.color.redLine;
        else if(alarms.get(position).mLine.equals("Green"))
            lineColor = R.color.greenLine;
        else if(alarms.get(position).mLine.equals("Blue"))
            lineColor = R.color.blueLine;
        else if(alarms.get(position).mLine.equals("S Line"))
            lineColor = R.color.silverLine;
        else if(alarms.get(position).mLine.equals("Front Runner"))
            trainIcon.setImageResource(R.drawable.ic_train_black_24dp);
        trainIcon.setColorFilter(lineColor);

        return rowView;

    };
}
