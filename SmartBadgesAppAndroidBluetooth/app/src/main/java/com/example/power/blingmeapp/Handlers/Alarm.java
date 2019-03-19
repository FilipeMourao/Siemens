package com.example.power.blingmeapp.Handlers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.power.blingmeapp.MainActivity;
import com.example.power.blingmeapp.Objects.ColorSetting;
import com.example.power.blingmeapp.Objects.ConfigureLed;
import com.example.power.blingmeapp.Objects.configureColorIndividually;
import com.example.power.blingmeapp.R;
import com.google.gson.Gson;



public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Gson gson = new Gson();
        String description = extras.getString("Description");
        ColorSetting colorsetting = gson.fromJson(extras.getString("Color Configuration"), ColorSetting.class);
        ConfigureLed configureLed = new ConfigureLed(colorsetting);
        configureColorIndividually myTask = new configureColorIndividually(context);
        myTask.execute(configureLed);// execute the asynctask to change the badge color
        if (!description.isEmpty()) {
            // Create notification
            Intent intent2 = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,MainActivity.CHANNEL_ID) // create notificication that will be showed
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                    .setContentTitle("Reminder!")
                    .setContentText(description)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(MainActivity.NOTIFICATION_COUNTER, mBuilder.build());// add notification in the notification queue
            MainActivity.increaseNotificationCounting();// increase the counting of the notification queue
        }
        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);// set vibration to 1 second
        v.vibrate(1000);
    }


}
