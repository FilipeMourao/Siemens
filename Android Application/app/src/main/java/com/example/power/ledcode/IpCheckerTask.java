package com.example.power.ledcode;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IpCheckerTask  extends AsyncTask<List<String>, Void, List<String>> {


    private Context context;
    public IpCheckerTask(Context context) {
        this.context = context;
    }
    @Override
    protected List<String> doInBackground(List<String>... lists) {
        List<String> ipAdresses = lists[0];
        List<Color> colors = new ArrayList<Color>();
        Color violet  = new Color("rgb", 148, 0, 211); colors.add(violet);
        ColorSetting colorsetting = new ColorSetting("ON", 0, violet, "SOLID");
        int i = 0;
        ConfigureLed configureLed =  new ConfigureLed(ipAdresses, colorsetting, null);
        boolean incorrectIP = false;
        List<String>  newIpAdresses = new ArrayList<String>();
        int initialIpAdressSize = ipAdresses.size();
        while(i < initialIpAdressSize + 1)  {
            for (int j = 0 ; j < ipAdresses.size() ; j++){
                colorsetting.setColor(colors.get(i%colors.size()));
                try {
                    configureLed =  new ConfigureLed(ipAdresses, colorsetting, null);
                    if(!configureLed.configureColors(ipAdresses.get(j), colorsetting)) incorrectIP = true;
                } catch (IOException e) {
                    incorrectIP = true;
                    e.printStackTrace();
                }
                if (incorrectIP) {
                    newIpAdresses = new ArrayList<String>();
                    for (int k = j + 1 ; k < ipAdresses.size() ; k++ ) {
                        newIpAdresses.add(ipAdresses.get(k));
                    }
                    for (int k = 0 ; k < j ; k++ ) {
                        newIpAdresses.add(ipAdresses.get(k));
                    }
                    ipAdresses =  new ArrayList<String>(newIpAdresses);
                    break;
                }
                i++;
            }
            if(!incorrectIP) break;
            incorrectIP = false;
        }
        return ipAdresses;
    }
    @Override
    protected void onPostExecute(List<String> ipAdresses) {
        goToAlarmView(ipAdresses);
    }
    private void goToAlarmView(List<String> ipAdresses) {
        //handle value
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("validIpAdresses", (ArrayList<String>) ipAdresses);
        context.startActivity(intent);
    }
}

