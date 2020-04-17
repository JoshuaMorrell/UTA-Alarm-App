package com.example.utatracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    public ArrayList<Alarm> alarms;

    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, subtitleText, mLine, mStartStation, mEndStation, mDirection, mEnabled;
        private ImageView trainIcon;

        public AlarmViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.recycle_title);
            subtitleText = view.findViewById(R.id.recycle_date_time);
            trainIcon = view.findViewById(R.id.train_icon);
        }
    }

    public AlarmAdapter(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.train_alarm_item, parent, false);

        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.titleText.setText(alarm.mStartStation);
        holder.subtitleText.setText(alarm.mDate + " " + alarm.mTime);
        holder.trainIcon.setImageResource(R.drawable.ic_tram_black_24dp);

        if(alarm.mLine.equals("Red"))
            holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.redLine));
        else if(alarm.mLine.equals("Green"))
            holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.greenLine));
        else if(alarm.mLine.equals("Blue"))
            holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.blueLine));
        else if(alarm.mLine.equals("S-Line"))
            holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.silverLine));
        else if(alarm.mLine.equals("Front Runner")) {
            holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.black));
            holder.trainIcon.setImageResource(R.drawable.ic_train_black_24dp);
        }
        else
            holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.black));
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }
}