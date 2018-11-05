package com.example.power.blingmeapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService {
    Context context;

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {
        //https://github.com/kpbird/NotificationListenerService-Example/
        String pack = sbn.getPackageName();
        if (pack.toLowerCase().contains("whatsapp")) handlingWhatsappNotification();
        if (pack.toLowerCase().contains("facebook")) handlingFacebookNotification();
        if (pack.toLowerCase().contains("instagram")) handlingInstagramNotification();
        if (pack.toLowerCase().contains("twitter")) handlingTwitterNotification();

    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
    public void handlingFacebookNotification(){
        NotificationDataBase db = new NotificationDataBase(context);
        db.getReadableDatabase();
        if (db.getNotification("facebook") != null){
            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("facebook").getColorString()))) ;
            configureColorIndividually myTask = new configureColorIndividually();
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myTask.cancel(true);
            myTask = new configureColorIndividually();
            configureLed.getColorStetting().setBrightness(0);
            myTask.execute(configureLed);
        }
    }
    public void handlingTwitterNotification(){
        NotificationDataBase db = new NotificationDataBase(context);
        db.getReadableDatabase();
        if (db.getNotification("twitter") != null){
            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("twitter").getColorString()))) ;
            configureColorIndividually myTask = new configureColorIndividually();
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            myTask.cancel(true);
            myTask = new configureColorIndividually();
            configureLed.getColorStetting().setBrightness(0);
            myTask.execute(configureLed);
        }
    }
    public void handlingWhatsappNotification(){
        NotificationDataBase db = new NotificationDataBase(context);
        db.getReadableDatabase();
        if (db.getNotification("whatsapp") != null){
            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("whatsapp").getColorString()))) ;
            configureColorIndividually myTask = new configureColorIndividually();
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            myTask.cancel(true);
            myTask = new configureColorIndividually();
            configureLed.getColorStetting().setBrightness(0);
            myTask.execute(configureLed);
        }
    }
    public void handlingInstagramNotification(){
        NotificationDataBase db = new NotificationDataBase(context);
        db.getReadableDatabase();
        if (db.getNotification("instagram") != null){
            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("instagram").getColorString()))) ;
            configureColorIndividually myTask = new configureColorIndividually();
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            myTask.cancel(true);
            myTask = new configureColorIndividually();
            configureLed.getColorStetting().setBrightness(0);
            myTask.execute(configureLed);
        }
    }
}
