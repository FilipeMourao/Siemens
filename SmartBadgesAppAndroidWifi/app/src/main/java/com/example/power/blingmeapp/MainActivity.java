package com.example.power.blingmeapp;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import static android.support.v4.content.ContextCompat.getSystemService;
public class MainActivity extends AppCompatActivity {
    public static String CHANNEL_ID = "TEST";
    public static  int NOTIFICATION_COUNTER = 1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String myUrl = "file:///android_asset/newIndex.html";// the path of the source file
        WebView webView = (WebView) findViewById(R.id.webView);
        WebView.setWebContentsDebuggingEnabled(true); // set the possibility to debug using chrome://inspec
        webView.getSettings().setJavaScriptEnabled(true);// enable javascript communication between front end and back ends
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);// add the javascript for the projcet
        webView.addJavascriptInterface(jsInterface, "JSInterface");// add the javascript interface for the project
        webView.loadUrl(myUrl);// load the front end in the  project in the webview
        createNotificationChannel();
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void increaseNotificationCounting() {
        NOTIFICATION_COUNTER += 1;
    }
}