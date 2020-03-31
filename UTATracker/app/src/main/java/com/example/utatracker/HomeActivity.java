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
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    public static final String DEFAULT_ID = "default";

    FloatingActionButton fab ;
    ListView alarmView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fab = findViewById(R.id.fab);
        alarmView = findViewById(R.id.list);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddAlarmActivity.class));
            }
        });
        View emptyView = findViewById(R.id.empty_view);
        alarmView.setEmptyView(emptyView);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int res_id = item.getItemId();
//        if(res_id == R.id.logout) {
//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            mAuth.signOut();
//
//            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
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


    @Override
    public void onBackPressed() {
        return;
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
