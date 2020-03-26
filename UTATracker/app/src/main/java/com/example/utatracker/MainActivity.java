package com.example.utatracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    public static final String DEFAULT_ID = "default";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_5 :
                scheduleNotification(getNotification( "5 second delay" ) , 5000 ) ;
                return true;
            default :
                return super .onOptionsItemSelected(item) ;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }
        else{
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    private void scheduleNotification(Notification notification, int delay) {
        Log.d("tag", "SCHEDULE~~~~~~GOT HERE~~~~~~~~~");
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.CHANNEL_ID , 1 ) ;
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 1 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock.elapsedRealtime() + delay ;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }

    private Notification getNotification (String content) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, DEFAULT_ID) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId(NOTIFICATION_CHANNEL_ID) ;
        return builder.build() ;
    }
}
