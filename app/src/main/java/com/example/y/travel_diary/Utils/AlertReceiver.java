package com.example.y.travel_diary.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.example.y.travel_diary.MainActivity;
import com.example.y.travel_diary.R;

public class AlertReceiver extends BroadcastReceiver{

    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {

        String planName = intent.getStringExtra("planName");

        createNotification(context, "Plan!", "Your plan \"" +planName +"\" " + "is started!"  , "Alert");

    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert){

        // Define an Intent and an action to perform with it by another application
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        // Builds a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.hours)
                        .setContentTitle(msg)
                        .setTicker(msgAlert)
                        .setContentText(msgText);

        // Defines the Intent to fire when the notification is clicked
        mBuilder.setContentIntent(notificIntent);

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {350,250,350,250,350,250,350,250,350,250,350,250,350,250,350,250,350,250};

        v.vibrate(pattern,-1);

        // Auto cancels the notification when clicked on in the task bar
        mBuilder.setAutoCancel(true);

        // Gets a NotificationManager which is used to notify the user of the background event
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        mNotificationManager.notify(1, mBuilder.build());

    }
}