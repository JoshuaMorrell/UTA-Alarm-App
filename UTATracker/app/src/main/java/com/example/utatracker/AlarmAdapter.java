package com.example.utatracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    ArrayList<Alarm> alarms;

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, subtitleText, mLine, mStartStation, mEndStation, mDirection, mEnabled;
        private ImageView trainIcon;

        AlarmViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.recycle_title);
            subtitleText = view.findViewById(R.id.recycle_date_time);
            trainIcon = view.findViewById(R.id.train_icon);
        }
    }

    AlarmAdapter(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    @NonNull
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

        switch (alarm.mLine) {
            case "Red":
                holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.redLine));
                break;
            case "Green":
                holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.greenLine));
                break;
            case "Blue":
                holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.blueLine));
                break;
            case "S-Line":
                holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.silverLine));
                break;
            case "Front Runner":
                holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.black));
                holder.trainIcon.setImageResource(R.drawable.ic_train_black_24dp);
                break;
            default:
                holder.trainIcon.setColorFilter(ContextCompat.getColor(holder.trainIcon.getContext(), R.color.black));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }
}