package com.example.power.blingmeapp;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

	public boolean configureColors(String ipAdress, ColorSetting colorStetting ) throws IOException {
		Gson gson = new Gson();
		String jsonString = gson.toJson(colorStetting);
		String url = "http://"+ ipAdress + "/api/v1/state";
		 URL obj = new URL(url);
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	     // optional default is GET
	     con.setRequestMethod("POST");
	     //add request header
         con.setConnectTimeout(5000);
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
//	     BufferedReader in = new BufferedReader(
//	             new InputStreamReader(con.getInputStream()));
//	     String inputLine;
//	     StringBuffer response = new StringBuffer();
//	     while ((inputLine = in.readLine()) != null) {
//	     	response.append(inputLine);
//	     }
		if(responseCode == 200) return true;
	     return false;
	     
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
