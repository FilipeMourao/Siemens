package com.example.power.blingmeapp.Handlers;

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

import com.example.power.blingmeapp.Objects.*;

import java.io.ByteArrayOutputStream;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService {
    Context context;
    Database db;
    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        db = new Database(context);

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
    public void handlingFacebookNotification(){ // create a notidication if a facebook notification appears
        db.getReadableDatabase();
        if (db.getNotification("facebook") != null){
            IpAdress ipAdress= ((IpAdress)context.getApplicationContext());
            ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(db.getNotification("facebook").getColorString()))) ;
            //ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("facebook").getColorString()))) ;
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
    public void handlingTwitterNotification(){// create a notidication if a twitter notification appears
        db.getReadableDatabase();
        if (db.getNotification("twitter") != null){
            IpAdress ipAdress= ((IpAdress)context.getApplicationContext());
            ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(db.getNotification("twitter").getColorString()))) ;
            //ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("twitter").getColorString()))) ;
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
    public void handlingWhatsappNotification(){// create a notidication if a whatsapp notification appears
        db.getReadableDatabase();
        if (db.getNotification("whatsapp") != null){
            IpAdress ipAdress= ((IpAdress)context.getApplicationContext());
            ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(db.getNotification("whatsapp").getColorString()))) ;
            //ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("whatsapp").getColorString()))) ;
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
    public void handlingInstagramNotification(){// create a notidication if an instagram notification appears
        db.getReadableDatabase();
        if (db.getNotification("instagram") != null){
            IpAdress ipAdress= ((IpAdress)context.getApplicationContext());
            ConfigureLed configureLed =  new ConfigureLed(ipAdress.getIPADRESS(),new ColorSetting(new ColorCustomized(db.getNotification("instagram").getColorString()))) ;
//            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("instagram").getColorString()))) ;
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
