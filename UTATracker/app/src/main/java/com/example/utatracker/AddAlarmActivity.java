package com.example.utatracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.utatracker.data.AlarmReminderContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AddAlarmActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    Button saveButton;

    ConstraintLayout dateExpandable, timeExpandable, notifyExpandable;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    RelativeLayout dateLayout, timeLayout, notifyLayout;
    TimePickerDialog timePicker;



//    Calendar mCalendar;
//
    private String mDate;
    private String mTime;
    private String mLine;
    private String mStartStation;
    private String mEndStation;
    private String mDirection;
    private String mAlertTime;
    private String mActive;
//    private int mYear, mMonth, mHour, mMinute, mDay;

    MaterialDayPicker dayPicker;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);


        dateExpandable = findViewById(R.id.dateExpandView);
        dateLayout = findViewById(R.id.date);
        saveButton = findViewById(R.id.saveButton);
        dayPicker = findViewById(R.id.day_picker);

//        mCalendar = Calendar.getInstance();
//        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
//        mMinute = mCalendar.get(Calendar.MINUTE);
//        mYear = mCalendar.get(Calendar.YEAR);
//        mMonth = mCalendar.get(Calendar.MONTH) + 1;
//        mDay = mCalendar.get(Calendar.DATE);
//
//        mDate = mDay + "/" + mMonth + "/" + mYear;
//        mTime = mHour + ":" + mMinute;

        mDateText = findViewById(R.id.date_text);
        mTimeText = findViewById(R.id.time_text);

        saveButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startActivity(new Intent(AddAlarmActivity.this, HomeActivity.class));
              }
        });

        // Expandable date selector animation
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateExpandable.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(dateLayout, new AutoTransition());
                    dateExpandable.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(dateLayout, new AutoTransition());
                    dateExpandable.setVisibility(View.GONE);
                }
            }
        });

        // Date picker change listener
        final List[] daysOfTheWeek = new List[0];
        dayPicker.setDaySelectionChangedListener(new MaterialDayPicker.DaySelectionChangedListener() {
            @Override
            public void onDaySelectionChanged(@NonNull List<MaterialDayPicker.Weekday> selectedDays) {
                // ~~~~~~~~~~~~~~~~~~~~~~ selectedDays contains the days selected from day picker ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Log.d("hello",String.format("[DaySelectionChangedListener]%s", selectedDays.toString()));
                mDate = selectedDays.toString();
                mDateText.setText(mDate);
            }
        });

        timeLayout = findViewById(R.id.time);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                timePicker = new TimePickerDialog(AddAlarmActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // ~~~~~~~~~~~~~~~~~~~~~~~~~~THIS IS HOW YOU GET THE HOUR (hourOfDay) AND MINUTE (minute) WHEN SELECTED~~~~~~~~~~~~~~~`~
                        Log.d("timePicker", hourOfDay + ":" + minute);
                       mTime = hourOfDay + ":" + minute;
                       mTimeText.setText(mTime);
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                timePicker.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle values = new Bundle();

                values.putString("date", mDate);
                values.putString("time", mTime);
                values.putString("line", mLine);
                values.putString("start_station", mStartStation);
                values.putString("end_station", mEndStation);
                values.putString("direction", mDirection);
                values.putString("active", mActive);

                startActivity(new Intent(AddAlarmActivity.this, HomeActivity.class));
            }
        });
    }

    public void selectStartLocation(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    public void selectStopLocation(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.firstStop:
                Toast.makeText(this, "FIRST STOP", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.secondStop:
                Toast.makeText(this, "SECOND STOP", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.thirdStop:
                Toast.makeText(this, "THIRD STOP", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
