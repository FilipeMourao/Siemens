package com.example.power.ledcode;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RainbomTask extends AsyncTask< List<String> , Void, Void> {

    @Override
    protected Void doInBackground(List<String>... lists) {
        List<String> ipAdresses = lists[0];
        List<Color> colors = new ArrayList<Color>();
        Color violet  = new Color("rgb", 148, 0, 211); colors.add(violet);
        Color indigo  = new Color("rgb", 75, 0, 130);colors.add(indigo);
        Color blue  = new Color("rgb", 0, 0, 255);colors.add(blue);
        Color green  = new Color("rgb", 0, 255, 0);colors.add(green);
        Color yellow  = new Color("rgb", 255, 255, 0);colors.add(yellow);
        Color orange  = new Color("rgb", 255, 127, 0);colors.add(orange);
        Color red  = new Color("rgb", 255, 0, 0);colors.add(red);
        ColorSetting colorsetting = new ColorSetting("ON", 75, violet, "SOLID");
        int i = 0;
        ConfigureLed configureLed =  new ConfigureLed(ipAdresses, colorsetting, null);
        boolean incorrectIP = false;
       List<String>  newIpAdresses = new ArrayList<String>();
       int initialIpAdressSize = ipAdresses.size();
        while(i < 2*initialIpAdressSize || i < 10)  {
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
                   incorrectIP = false;
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
        }
        colorsetting = new ColorSetting("ON", 0, violet, "SOLID");
        configureLed =  new ConfigureLed(ipAdresses, colorsetting, null);
        for (int j = 0; j< ipAdresses.size(); j++){
            try {
                configureLed.configureColors(ipAdresses.get(j), colorsetting);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
