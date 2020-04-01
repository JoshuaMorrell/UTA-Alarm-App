package com.example.utatracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    public static final String DEFAULT_ID = "default";
    ListView alarmView;
    FloatingActionButton fab;
    ArrayList<Alarm> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        alarms = new ArrayList<Alarm>();
        for(String alarm: sharedPref.getStringSet("alarms", new HashSet<String>())){
            alarms.add(Alarm.fromString(alarm));
        }

        fab = findViewById(R.id.fab);
        alarmView = findViewById(R.id.list);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(HomeActivity.this, AddAlarmActivity.class));
            }
        });

        if (alarms != null && alarms.size() != 0) {
            AlarmAdapter adapter = new AlarmAdapter(this, alarms);
            alarmView = (ListView) findViewById(R.id.list);
            alarmView.setAdapter(adapter);

            View emptyView = findViewById(R.id.empty_view);
            alarmView.setEmptyView(emptyView);

            alarmView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    //code specific to first list item
                    Toast.makeText(getApplicationContext(), "Place Your First Option Code", Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    //code specific to 2nd list item
                    Toast.makeText(getApplicationContext(), "Place Your Second Option Code", Toast.LENGTH_SHORT).show();
                } else if (position == 2) {

                    Toast.makeText(getApplicationContext(), "Place Your Third Option Code", Toast.LENGTH_SHORT).show();
                } else if (position == 3) {

                    Toast.makeText(getApplicationContext(), "Place Your Forth Option Code", Toast.LENGTH_SHORT).show();
                } else if (position == 4) {

                    Toast.makeText(getApplicationContext(), "Place Your Fifth Option Code", Toast.LENGTH_SHORT).show();
                }
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
