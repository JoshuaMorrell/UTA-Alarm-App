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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class AlarmAdapter extends ArrayAdapter<String> {
    private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;

    private final Activity context;
    private final ArrayList<String> title;
    private final ArrayList<String> dayOfWeek;
    private final ArrayList<String> time;
    private final ArrayList<String> line;

    public AlarmAdapter(@NonNull Activity context, HashSet<Alarm> alarms) {
        super(context, R.layout.train_alarm_item);

        this.context = context;
        title = new ArrayList<String>();
        dayOfWeek = new ArrayList<String>();
        time = new ArrayList<String>();
        line = new ArrayList<String>();

        Iterator<Alarm> it = alarms.iterator();
        while(it.hasNext()) {
            Alarm curAlarm = it.next();
            title.add(curAlarm.mStartStation);
            dayOfWeek.add(curAlarm.mDate);
            time.add(curAlarm.mTime);
            line.add(curAlarm.mLine);
        }
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.train_alarm_item, null,true);

        TextView titleText = rowView.findViewById(R.id.recycle_title);
        TextView subtitleText = rowView.findViewById(R.id.recycle_date_time);
        ImageView trainIcon = rowView.findViewById(R.id.train_icon);


        titleText.setText(title.get(position));
        subtitleText.setText(dayOfWeek.get(position) + " " + time.get(position));
        int lineColor = R.color.black;
        if(line.get(position).equals("Red"))
            lineColor = R.color.redLine;
        else if(line.get(position).equals("Green"))
            lineColor = R.color.greenLine;
        else if(line.get(position).equals("Blue"))
            lineColor = R.color.blueLine;
        else if(line.get(position).equals("S Line"))
            lineColor = R.color.silverLine;
        else if(line.get(position).equals("Front Runner"))
            trainIcon.setImageResource(R.drawable.ic_train_black_24dp);
        trainIcon.setColorFilter(lineColor);

        return rowView;

    };
}
