package com.example.utatracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


public class NotificationPublisher extends BroadcastReceiver {
    public static final String CHANNEL_ID = "ChannelID";
    public static final String NOTIFICATION = "notification";
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, "Notification Channel", NotificationManager.IMPORTANCE_HIGH);
            assert notificationManager != null;

            notificationManager.createNotificationChannel(notificationChannel);
        }

        int id = intent.getIntExtra(CHANNEL_ID, 0);
        assert notificationManager != null;
        notificationManager.notify(id, notification);
    }
}
