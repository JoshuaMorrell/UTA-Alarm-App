package com.example.utatracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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
    String selectedLine, selectedStartLoc, selectedEndLoc;
    Button saveButton;

    ConstraintLayout dateExpandable, timeExpandable, notifyExpandable, lineExpandable, startExpandable, endExpandable;
    RelativeLayout dateLayout, timeLayout, notifyLayout, startLayout, endLayout, lineLayout;
    TimePickerDialog timePicker;

    MaterialDayPicker dayPicker;
    NumberPicker linePicker, startPicker, endPicker;

    String[] lines, redDirection, blueDirection, greenDirection, sDirection, frontDirection;

    String[] redLineStations, blueLineStations, greenLineStations, sLineStations, frontRunnerStations;
    String[] selectedStations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        lines = new String[]{"Red", "Blue", "Green", "S-Line", "Front Runner"};
        selectedLine = "Red";
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
        startPicker = findViewById(R.id.startPicker);
        endPicker = findViewById(R.id.endPicker);

        // Disable start and end locations until line is chosen
        startLayout = findViewById(R.id.start_location);
        endLayout = findViewById(R.id.end_location);
        startLayout.setEnabled(false);
        endLayout.setEnabled(false);
        startExpandable = findViewById(R.id.startExpandView);
        endExpandable = findViewById(R.id.endExpandView);
        lineExpandable = findViewById(R.id.lineExpandView);
        lineLayout = findViewById(R.id.line);

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

        setUpLinePicker();

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

        // Start and end layout expandable
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

    }

    private void setUpLinePicker() {
        linePicker.setMinValue(0);
        linePicker.setMaxValue(lines.length - 1);
        linePicker.setWrapSelectorWheel(true);
        linePicker.setDisplayedValues(lines);
        linePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // Enable start and stop location
                startLayout.setEnabled(true);
                endLayout.setEnabled(true);
                startExpandable.removeView(startPicker);
                endExpandable.removeView(endPicker);

                selectedLine = lines[newVal];
                Log.d("line", selectedLine);
                selectedStations = getStationListFromSelection(selectedLine);

                startPicker = new NumberPicker(getApplicationContext());
                endPicker = new NumberPicker(getApplicationContext());
                setUpSelectedLinePicker(selectedLine);
                setNumberPickerTextColor(startPicker, Color.BLACK);
                setNumberPickerTextColor(endPicker, Color.BLACK);

                startExpandable.addView(startPicker);
                endExpandable.addView(endPicker);
            }
        });
    }

    private void setUpSelectedLinePicker(final String selectedLine) {
        Log.d("SELECTED LINE", selectedLine + " " + selectedStations.length);

        startPicker.setMinValue(0);
        startPicker.setMaxValue(selectedStations.length - 1);
        startPicker.setWrapSelectorWheel(true);
        startPicker.setDisplayedValues(selectedStations);
        startPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedStartLoc = selectedStations[newVal];
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
            }
        });

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
            if (name.startsWith(line)) {
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
        Log.d("update", "updating selection");
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
