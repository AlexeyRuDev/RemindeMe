package com.example.rudnev.remindme;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;

import java.util.Calendar;
import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /*RemindDBAdapter dbAdapter = new RemindDBAdapter(context);
        List<RemindDTO> mRemindItems = dbAdapter.getAllItems(1, null);
        Calendar currentTime = Calendar.getInstance();
        Calendar itemTime = Calendar.getInstance();
        for(RemindDTO item : mRemindItems){
            itemTime.setTime(item.getDate());
            if(currentTime.get(Calendar.HOUR)== itemTime.get(Calendar.HOUR) && currentTime.get(Calendar.MINUTE)== itemTime.get(Calendar.MINUTE)){
                Intent notificationIntent = new Intent(context, MainActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                Notification notification = builder.setContentTitle("Test app notification")
                        .setContentText("New notification")
                        .setTicker("New message")
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent).build();

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notification);
            }
        }*/

    }
}
