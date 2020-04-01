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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AddAlarmActivity extends AppCompatActivity {
    String selectedLine, selectedStartLoc, selectedEndLoc, selectedDirection, selectedNotifyTime;
    Button saveButton;
    TextView lineText, startText, endText, directionText, alertText;

    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    ConstraintLayout dateExpandable, lineExpandable, startExpandable, endExpandable,
            directionExpandable, alertExpandable;
    RelativeLayout dateLayout, timeLayout, startLayout, endLayout, lineLayout,
            directionLayout, alertLayout;
    TimePickerDialog timePicker;

//    Calendar mCalendar;

    private String mDate;
    private String mTime;
    private String mLine;
    private String mDirection;
    private String mAlertTime;
    private String mActive;
//    private int mYear, mMonth, mHour, mMinute, mDay;

    MaterialDayPicker dayPicker;
    SharedPreferences sharedPref;
    NumberPicker linePicker, startPicker, endPicker, directionPicker, alertPicker;

    String[] lines, redLineStations, blueLineStations, greenLineStations, sLineStations,
            frontRunnerStations, selectedStations, alertTimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        lines = new String[]{"Red", "Blue", "Green", "S-Line", "Front Runner"};
        alertTimes = new String[] {"5 minutes", "10 minutes", "15 minutes", "20 minutes", "30 minutes"};
        selectedLine = "Red";
        redLineStations = getListOfStations("red");
        blueLineStations = getListOfStations("blue");
        greenLineStations = getListOfStations("green");
        sLineStations = getListOfStations("sline");
        frontRunnerStations = getListOfStations("frontrunner");

        lineText = findViewById(R.id.line_text);
        startText = findViewById(R.id.start_location_text);
        endText = findViewById(R.id.end_location_text);
        directionText = findViewById(R.id.direction_text);
        alertText = findViewById(R.id.alert_text);

        dateExpandable = findViewById(R.id.dateExpandView);
        dateLayout = findViewById(R.id.date);
        saveButton = findViewById(R.id.saveButton);
        dayPicker = findViewById(R.id.day_picker);

        linePicker = findViewById(R.id.linePicker);
        startPicker = findViewById(R.id.startPicker);
        endPicker = findViewById(R.id.endPicker);
        directionPicker = findViewById(R.id.directionPicker);
        alertPicker = findViewById(R.id.alertPicker);

        startLayout = findViewById(R.id.start_location);
        endLayout = findViewById(R.id.end_location);
        startExpandable = findViewById(R.id.startExpandView);
        endExpandable = findViewById(R.id.endExpandView);
        lineExpandable = findViewById(R.id.lineExpandView);
        lineLayout = findViewById(R.id.line);
        directionExpandable = findViewById(R.id.directionExpandView);
        directionLayout = findViewById(R.id.set_direction);
        alertExpandable = findViewById(R.id.alertExpandView);
        alertLayout = findViewById(R.id.alert_time);



        mDateText = findViewById(R.id.date_text);
        mTimeText = findViewById(R.id.time_text);

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
            if(selectedDays.size() > 0){
                mDate = selectedDays.toString();
                mDateText.setText(mDate.substring(1,mDate.length()-1));
            }
            else {
                mDate = null;
                mDateText.setText("Day of the Week");
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
                    // ~~~~~~~~~~~~~~~~~~~~~~~~~~THIS IS HOW YOU GET THE HOUR (hourOfDay) AND MINUTE (minute) WHEN SELECTED~~~~~~~~~~~~~~~`~
                    Log.d("timePicker", hourOfDay + ":" + minute);
                    if(minute < 10)
                        mTime = hourOfDay + ":0" + minute;
                    else
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
            if(mDate == null || mTime == null || selectedLine == null || selectedStartLoc == null || selectedEndLoc == null || selectedDirection == null){
                Toast.makeText(v.getContext(), "Complete all fields before submitting.", Toast.LENGTH_SHORT).show();
                return;
            }

            Alarm alarm = new Alarm(mDate, mTime, selectedLine, selectedStartLoc, selectedEndLoc, selectedDirection);

            Set<String> alarms = new HashSet<>(sharedPref.getStringSet("alarms", new HashSet<String>()));
            alarms.add(alarm.toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putStringSet("alarms", alarms);
            editor.commit();

            startActivity(new Intent(AddAlarmActivity.this, HomeActivity.class));
            finish();
            }
        });

        // Set up default values
        setUpLinePicker();
        selectedStations = getStationListFromSelection(selectedLine);
        selectedStartLoc = selectedStations[0];
        selectedEndLoc = selectedStations[selectedStations.length - 1];
        selectedDirection = selectedStations[0];
        selectedNotifyTime = alertTimes[0];
        setUpSelectedLinePicker();
        setUpDirectionPicker();
        setUpAlertPicker();

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

        // Start layout expandable
        startLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startExpandable.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(startLayout, new AutoTransition());
                    startExpandable.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(startLayout, new AutoTransition());
                    startExpandable.setVisibility(View.GONE);
                }
            }
        });

        // End layout expandable
        endLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endExpandable.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(endLayout, new AutoTransition());
                    endExpandable.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(endLayout, new AutoTransition());
                    endExpandable.setVisibility(View.GONE);
                }
            }
        });

        // Direction layout expandable
        directionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (directionExpandable.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(directionLayout, new AutoTransition());
                    directionExpandable.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(directionLayout, new AutoTransition());
                    directionExpandable.setVisibility(View.GONE);
                }
            }
        });

        // Alert layout expandable
        alertLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertExpandable.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(alertLayout, new AutoTransition());
                    alertExpandable.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(alertLayout, new AutoTransition());
                    alertExpandable.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setUpLinePicker() {
        linePicker.setMinValue(0);
        linePicker.setMaxValue(lines.length - 1);
        linePicker.setWrapSelectorWheel(true);
        linePicker.setDisplayedValues(lines);
        linePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                // Remove previous pickers
                startExpandable.removeView(startPicker);
                endExpandable.removeView(endPicker);
                directionExpandable.removeView(directionPicker);

                selectedLine = lines[newVal];
                selectedStations = getStationListFromSelection(selectedLine);

                // Update text view
                lineText.setText(selectedLine);

                startPicker = new NumberPicker(getApplicationContext());
                endPicker = new NumberPicker(getApplicationContext());
                directionPicker = new NumberPicker(getApplicationContext());

                setUpSelectedLinePicker();
                setUpDirectionPicker();

                setNumberPickerTextColor(startPicker, Color.BLACK);
                setNumberPickerTextColor(endPicker, Color.BLACK);
                setNumberPickerTextColor(directionPicker, Color.BLACK);

                startExpandable.addView(startPicker);
                endExpandable.addView(endPicker);
                directionExpandable.addView(directionPicker);
            }
        });
    }

    private void setUpSelectedLinePicker() {
        startPicker.setMinValue(0);
        startPicker.setMaxValue(selectedStations.length - 1);
        startPicker.setWrapSelectorWheel(true);
        startPicker.setDisplayedValues(selectedStations);
        startPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedStartLoc = selectedStations[newVal];
                startText.setText(selectedStartLoc);
            }
        });

        endPicker.setMinValue(0);
        endPicker.setMaxValue(selectedStations.length - 1);
        endPicker.setWrapSelectorWheel(true);
        endPicker.setDisplayedValues(selectedStations);
        endPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedEndLoc = selectedStations[newVal];
                endText.setText(selectedEndLoc);
            }
        });
    }

    private void setUpDirectionPicker() {
        final String[] direction = getDirectionArray();
        directionPicker.setMinValue(0);
        directionPicker.setMaxValue(direction.length - 1);
        directionPicker.setWrapSelectorWheel(true);
        directionPicker.setDisplayedValues(direction);
        directionPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedDirection = direction[newVal];
                directionText.setText(selectedDirection);
            }
        });
    }

    private void setUpAlertPicker() {
        alertPicker.setMinValue(0);
        alertPicker.setMaxValue(alertTimes.length - 1);
        alertPicker.setWrapSelectorWheel(true);
        alertPicker.setDisplayedValues(alertTimes);
        alertPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedNotifyTime = alertTimes[newVal];
                alertText.setText(selectedNotifyTime);
            }
        });
    }

    private String[] getDirectionArray() {
        final String[] direction;
        switch (selectedLine) {
            case "Blue":
                direction = new String[]{
                        getString(R.string.directionOfTravel_blueline_Draper),
                        getString(R.string.directionOfTravel_blueline_SaltLakeCt)};
                break;
            case "Red":
                direction = new String[]{
                        getString(R.string.directionofTravel_redline_Daybreak),
                        getString(R.string.directionofTravel_redline_Medical)};
                break;
            case "Green":
                direction = new String[]{
                        getString(R.string.directionofTravel_greenline_WestValley),
                        getString(R.string.directionofTravel_greenline_Airport)};
                break;
            case "S-Line":
                direction = new String[]{
                        getString(R.string.directionofTravel_sline_Fairmont),
                        getString(R.string.directionofTravel_sline_CentralPointe)};
                break;
            case "Front Runner":
                direction = new String[]{
                        getString(R.string.directionOfTravel_frontrunner_OgdenNB),
                        getString(R.string.directionOfTravel_frontrunner_ProvoCentralSB)};
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedLine);
        }

        return direction;
    }

    /**
     * Given a line name, returns a list of station names
     * @param line
     * @return
     */
    private String[] getListOfStations(String line) {
        ArrayList<String> results = new ArrayList<>();

        Field[] fields = R.string.class.getFields();
        String[] stringNames = new String[fields.length];
        for (int  i = 0; i < fields.length; i++) {
            stringNames[i] = fields[i].getName();
        }

        for (String name : stringNames) {
            if (name.contains(line)) {
                int resID = getResourceId(name, "string", getPackageName());
                results.add(getString(resID));
            }
        }

        return GetStringArray(results);
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

    // Function to convert ArrayList<String> to String[]
    public static String[] GetStringArray(ArrayList<String> arr)
    {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }

    private String[] getStationListFromSelection(String selectedLine){
        final String[] stations;
        switch (selectedLine) {
            case "Blue":
                stations = blueLineStations;
                break;
            case "Red":
                stations = redLineStations;
                break;
            case "Green":
                stations = greenLineStations;
                break;
            case "S-Line":
                stations = sLineStations;
                break;
            case "Front Runner":
                stations = frontRunnerStations;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedLine);
        }

        return stations;
    }


    public static void setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {

        try{
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
        }
        catch(NoSuchFieldException e){
            Log.w("setNumberPickerTxtColor", e);
        }
        catch(IllegalAccessException e){
            Log.w("setNumberPickerTxtColor", e);
        }
        catch(IllegalArgumentException e){
            Log.w("setNumberPickerTxtColor", e);
        }

        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText)
                ((EditText)child).setTextColor(color);
        }
        numberPicker.invalidate();
    }
}
