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
	public ConfigureLed(ColorSetting colorStetting) {
//		this.ipAdress = "192.168.1.117";
		this.ipAdress = "192.168.1.116";
		this.colorStetting = colorStetting;
	}

	public void configureColors(String ipAdress, ColorSetting colorStetting )  {
		try {
			Gson gson = new Gson();
			boolean testingConnectionFlag = false;
			if (colorStetting.getBrightness() < 0){
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
				}
//				return true;
			} else {
				JavaScriptInterface.callJSMethod("app.IpErrorConnection();");
			}

		}catch (Exception e){
			JavaScriptInterface.callJSMethod("app.IpErrorConnection();");
			e.getMessage();
		}
//		return false;
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
