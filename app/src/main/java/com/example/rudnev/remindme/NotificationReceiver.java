package com.example.rudnev.remindme;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "NOTIFICATION-ID";
    public static String NOTIFICATION = "NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        if(notificationManager!=null)
            notificationManager.notify(intent.getIntExtra(NOTIFICATION_ID, 0), notification);

    }
}
