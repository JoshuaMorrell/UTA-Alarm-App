package com.example.utatracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.utatracker.data.AlarmReminderContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAlarmActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    Button saveButton;

    ConstraintLayout dateExpandable, timeExpandable, notifyExpandable;
    RelativeLayout dateLayout, timeLayout, notifyLayout;
    TimePickerDialog timePicker;

    private String mDate;
    private String mTime;
    private String mLine;
    private String mStartStation;
    private String mEndStation;
    private String mDirection;
    private String mAlertTime;
    private String mActive;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        dateExpandable = findViewById(R.id.dateExpandView);
        dateLayout = findViewById(R.id.date);
        saveButton = findViewById(R.id.saveButton);

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

        timeLayout = findViewById(R.id.time);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                timePicker = new TimePickerDialog(AddAlarmActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Log.d("timePicker", hourOfDay + ":" + minute);
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                timePicker.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();

                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_LINE, mLine);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_START_STATION, mStartStation);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_END_STATION, mEndStation);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DIRECTION, mDirection);
                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);

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
