package com.example.utatracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AddAlarmActivity extends AppCompatActivity {
    String selectedLine;
    Button saveButton;

    ConstraintLayout dateExpandable, timeExpandable, notifyExpandable, lineExpandable;
    RelativeLayout dateLayout, timeLayout, notifyLayout, startLayout, endLayout, lineLayout;
    TimePickerDialog timePicker;

    MaterialDayPicker dayPicker;
    NumberPicker linePicker;

    String[] lines, redDirection, blueDirection, greenDirection, sDirection, frontDirection;

    List<String> redLineStations, blueLineStations, greenLineStations, sLineStations, frontRunnerStations;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        lines = new String[]{"Red", "Blue", "Green", "S line", "Frontrunner"};
        redLineStations = getListOfStations("red");
        blueLineStations = getListOfStations("blue");
        greenLineStations = getListOfStations("green");
        sLineStations = getListOfStations("sline");
        frontRunnerStations = getListOfStations("frontrunner");

        dateExpandable = findViewById(R.id.dateExpandView);
        dateLayout = findViewById(R.id.date);
        saveButton = findViewById(R.id.saveButton);
        dayPicker = findViewById(R.id.day_picker);
        linePicker = findViewById(R.id.linePicker);
        lineExpandable = findViewById(R.id.lineExpandView);
        lineLayout = findViewById(R.id.line);

        // Disable start and end locations until line is chosen
        startLayout = findViewById(R.id.start_location);
        endLayout = findViewById(R.id.end_location);
        startLayout.setEnabled(false);
        endLayout.setEnabled(false);

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
        dayPicker.setDaySelectionChangedListener(new MaterialDayPicker.DaySelectionChangedListener() {
            @Override
            public void onDaySelectionChanged(@NonNull List<MaterialDayPicker.Weekday> selectedDays) {
                // ~~~~~~~~~~~~~~~~~~~~~~ selectedDays contains the days selected from day picker ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                Log.d("hello",String.format("[DaySelectionChangedListener]%s", selectedDays.toString()));
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
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                timePicker.show();
            }
        });

        // Line picker populator
        linePicker.setMinValue(0);
        linePicker.setMaxValue(lines.length - 1);
        linePicker.setWrapSelectorWheel(true);
        linePicker.setDisplayedValues(lines);

        // Line Expandable
        lineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lineExpandable.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(lineLayout, new AutoTransition());
                    lineExpandable.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(lineLayout, new AutoTransition());
                    lineExpandable.setVisibility(View.GONE);
                }
            }
        });

        linePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // Enable start and stop location
                startLayout.setEnabled(true);
                endLayout.setEnabled(true);
                selectedLine = lines[newVal];
            }
        });

    }

    /**
     * Given a line name, returns a list of station names
     * @param line
     * @return
     */
    private List<String> getListOfStations(String line) {
        List<String> results = new ArrayList<>();

        Field[] fields = R.string.class.getFields();
        String[] stringNames = new String[fields.length];
        for (int  i = 0; i < fields.length; i++) {
            stringNames[i] = fields[i].getName();
        }

        for (String name : stringNames) {
            if (name.startsWith(line)) {
                int resID = getResourceId(name, "string", getPackageName());
                results.add(getString(resID));
            }
        }

        return results;
    }

    private int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
