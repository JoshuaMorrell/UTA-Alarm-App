package com.example.utatracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    public static final String DEFAULT_ID = "default";
    ListView alarmView;
    FloatingActionButton fab;
    ArrayList<Alarm> alarms;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        scheduleNotification();

        sharedPref = sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        alarms = new ArrayList<Alarm>();
        for(String alarm: new HashSet<String>(sharedPref.getStringSet("alarms", new HashSet<String>()))){
            alarms.add(Alarm.fromString(alarm));
        }

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(HomeActivity.this, AddAlarmActivity.class));
            }
        });

        if (alarms != null && alarms.size() != 0) {
            final AlarmAdapter adapter = new AlarmAdapter(this, alarms);
            alarmView = (ListView) findViewById(R.id.list);
            alarmView.setAdapter(adapter);

            alarmView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    alarms.remove(position);
                    adapter.notifyDataSetChanged();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    Set<String> set = new HashSet<>();
                    for (Alarm a : alarms)
                        set.add(a.toString());
                    editor.putStringSet("alarms", set);
                    editor.apply();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_5 :

                scheduleNotification(getNotification( "5 second delay" ) , 5000 ) ;
                return true;
            case R.id.clearPreferences:
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                finish();
                return true;

            case R.id.logout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default :
                return super .onOptionsItemSelected(item) ;
        }
    }

    private void scheduleNotification() {
        Calendar calendar = Calendar.getInstance();

//        calendar.set(Calendar.DAY_OF_WEEK, )
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 43);


        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    
    private void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.CHANNEL_ID , 1 ) ;
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 1 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock.elapsedRealtime() + delay ;
        Log.d("notify", "Future in millis: " + String.valueOf(futureInMillis));
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }

    private Notification getNotification (String content) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_timer_black_24dp)
                .setContentTitle("UTA Tracker Alarm")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }
}
