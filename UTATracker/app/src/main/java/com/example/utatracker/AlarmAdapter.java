package com.example.utatracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class AlarmAdapter extends ArrayAdapter<Alarm> {
    public AlarmAdapter(@NonNull Activity context, ArrayList<Alarm> alarms) {
        super(context, 0, alarms);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Alarm alarm = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.train_alarm_item, parent, false);
        }

        TextView titleText = view.findViewById(R.id.recycle_title);
        TextView subtitleText = view.findViewById(R.id.recycle_date_time);
        ImageView trainIcon = view.findViewById(R.id.train_icon);

        titleText.setText(alarm.mStartStation);
        subtitleText.setText(alarm.mDate + " " + alarm.mTime);
        int lineColor = R.color.black;
        if(alarm.mLine.equals("Red"))
            lineColor = R.color.redLine;
        else if(alarm.mLine.equals("Green"))
            lineColor = R.color.greenLine;
        else if(alarm.mLine.equals("Blue"))
            lineColor = R.color.blueLine;
        else if(alarm.mLine.equals("S Line"))
            lineColor = R.color.silverLine;
        else if(alarm.mLine.equals("Front Runner"))
            trainIcon.setImageResource(R.drawable.ic_train_black_24dp);
        trainIcon.setColorFilter(lineColor);

        return view;
    };
}
