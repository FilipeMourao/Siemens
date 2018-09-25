package com.example.power.ledcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shiva on 8/4/17.
 */

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ColorSetting colorsetting = getPendingIntent().getStringArrayListExtra("Color Configuration");
        Toast.makeText(context,"Wake up",Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(10000);
    }
}