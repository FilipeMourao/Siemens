package com.example.power.blingmeapp.Objects;

import android.app.Activity;

import com.example.power.blingmeapp.JavaScriptInterface;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConfigureLed {

	String ipAdress;
	ColorSetting colorStetting;

	public ConfigureLed(String ipAdress, ColorSetting colorStetting) {
		this.ipAdress = ipAdress;
		this.colorStetting = colorStetting;
	}

	public void configureColors(String ipAdress, ColorSetting colorStetting )  {
		try {// convert the object created by ColorSetting and ColorCustomized
			// and send this information to the wifi badge via post
			Gson gson = new Gson();
			boolean testingConnectionFlag = false;
			if (colorStetting.getBrightness() < 0){// if the brightness is negative means that we are trying
				// to connect with the badge for the first time so the flag is setted as true
				colorStetting.setBrightness(0);
				testingConnectionFlag = true;
			}
			String jsonString = gson.toJson(colorStetting);
			String url = "http://"+ ipAdress + "/api/v1/state";
//			String url = "http://google.com";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("POST");
			//add request header
//			con.setConnectTimeout(5000);
			con.setConnectTimeout(15000);
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("POST");
			DataOutputStream os = new DataOutputStream(con.getOutputStream());
			os.writeBytes(jsonString);
			os.flush();
			os.close();
			System.out.println(jsonString);
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			if(responseCode == 200){
				if (testingConnectionFlag ){ // trying to connect with the badgee
					JavaScriptInterface.callJSMethod("app.connectDevice();");
					// calls a function to update the frontend view when the app is connected
				}
			} else {// if the connection didn`t return 200 something wrong happen so send an error call to the front end
				JavaScriptInterface.callJSMethod("app.IpErrorConnection();");
			}

		}catch (Exception e){// if some exception appears send an error call to the front end
			JavaScriptInterface.callJSMethod("app.IpErrorConnection();");
			e.getMessage();
		}
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public ColorSetting getColorStetting() {
		return colorStetting;
	}

	public void setColorStetting(ColorSetting colorStetting) {
		this.colorStetting = colorStetting;
	}



}
