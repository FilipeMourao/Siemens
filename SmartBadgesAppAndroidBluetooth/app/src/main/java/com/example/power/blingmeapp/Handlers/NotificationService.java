package com.example.power.blingmeapp.Handlers;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.power.blingmeapp.Objects.ColorCustomized;
import com.example.power.blingmeapp.Objects.ColorSetting;
import com.example.power.blingmeapp.Objects.ConfigureLed;
import com.example.power.blingmeapp.Objects.Database;
import com.example.power.blingmeapp.Objects.configureColorIndividually;

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
    public void handlingFacebookNotification(){// create a notidication if a facebook notification appears
        Database db = new Database(context);
        db.getReadableDatabase();
        if (db.getNotification("facebook") != null){
            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("facebook").getColorString()))) ;
            configureColorIndividually myTask = new configureColorIndividually(context);
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myTask.cancel(true);
            myTask = new configureColorIndividually(context);
            configureLed.getColorStetting().setBrightness(0);
            myTask.execute(configureLed);
        }
    }
    public void handlingTwitterNotification(){// create a notidication if a twitter notification appears
        Database db = new Database(context);
        db.getReadableDatabase();
        if (db.getNotification("twitter") != null){
            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("twitter").getColorString()))) ;
            configureColorIndividually myTask = new configureColorIndividually(context);
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            myTask.cancel(true);
            myTask = new configureColorIndividually(context);
            configureLed.getColorStetting().setBrightness(0);
            myTask.execute(configureLed);
        }
    }
    public void handlingWhatsappNotification(){// create a notidication if a whatsapp notification appears
        Database db = new Database(context);
        db.getReadableDatabase();
        if (db.getNotification("whatsapp") != null){
            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("whatsapp").getColorString()))) ;
            configureColorIndividually myTask = new configureColorIndividually(context);
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myTask.cancel(true);
            myTask = new configureColorIndividually(context);
            configureLed.getColorStetting().setBrightness(0);
            myTask.execute(configureLed);
        }
    }
    public void handlingInstagramNotification(){// create a notidication if an instagram notification appears
        Database db = new Database(context);
        db.getReadableDatabase();
        if (db.getNotification("instagram") != null){
            ConfigureLed configureLed =  new ConfigureLed(new ColorSetting(new ColorCustomized(db.getNotification("instagram").getColorString()))) ;
            configureColorIndividually myTask = new configureColorIndividually(context);
            myTask.execute(configureLed);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myTask.cancel(true);
            myTask = new configureColorIndividually(context);
            configureLed.getColorStetting().setBrightness(0);
            myTask.execute(configureLed);
        }
    }
}
