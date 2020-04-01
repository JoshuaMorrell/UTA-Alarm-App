package com.example.utatracker;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utatracker.data.AlarmReminderContract;

public class AlarmAdapter extends ArrayAdapter<String> {
    private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;

    private final Activity context;
    private final String[] title;
    private final String[] dayOfWeek;
    private final String[] time;
    private final String[] line;

    public AlarmAdapter(@NonNull Activity context, String[] alarmName, String[] dayOfWeek, String[] time, String[] line) {
        super(context, R.layout.train_alarm_item);

        this.context = context;
        this.title = alarmName;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.line = line;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.train_alarm_item, null,true);

        TextView titleText = rowView.findViewById(R.id.recycle_title);
        TextView subtitleText = rowView.findViewById(R.id.recycle_date_time);
        ImageView trainIcon = rowView.findViewById(R.id.train_icon);

        titleText.setText(title[position]);
        subtitleText.setText(dayOfWeek[position] + " " + time[position]);
        int lineColor = R.color.black;
        if(line[position] == "red")
            lineColor = R.color.redLine;
        else if(line[position] == "green")
            lineColor = R.color.greenLine;
        else if(line[position] == "blue")
            lineColor = R.color.blueLine;
        else if(line[position] == "s")
            lineColor = R.color.silverLine;
        trainIcon.setColorFilter(lineColor);

        return rowView;

    };
}
