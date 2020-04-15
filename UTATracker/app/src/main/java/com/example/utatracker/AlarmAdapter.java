package com.example.utatracker;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.Console;
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
        Switch enabled = view.findViewById(R.id.enabledSwitch);

        titleText.setText(alarm.mStartStation);
        subtitleText.setText(alarm.mDate + " " + alarm.mTime);
        trainIcon.setImageResource(R.drawable.ic_tram_black_24dp);

        enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("switch", "CHECKED");
                } else {
                    Log.i("switch", "UNCHECKED");
                }

            }
        });


        if(alarm.mLine.equals("Red"))
            trainIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.redLine));
        else if(alarm.mLine.equals("Green"))
            trainIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.greenLine));
        else if(alarm.mLine.equals("Blue"))
            trainIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.blueLine));
        else if(alarm.mLine.equals("S-Line"))
            trainIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.silverLine));
        else if(alarm.mLine.equals("Front Runner")) {
            trainIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
            trainIcon.setImageResource(R.drawable.ic_train_black_24dp);
        }
        else
            trainIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));

        return view;
    };
}
