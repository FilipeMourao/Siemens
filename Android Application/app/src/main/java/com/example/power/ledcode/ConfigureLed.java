package com.example.power.ledcode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
public class ConfigureLed {
	List<String> ipAdresses;
	ColorSetting colorStetting;
	NetworkConfiguration networkConfiguration;

	public ConfigureLed(List<String> ipAdresses, ColorSetting colorStetting, NetworkConfiguration networkConfiguration) {
		this.ipAdresses = ipAdresses;
		this.colorStetting = colorStetting;
		this.networkConfiguration = networkConfiguration;
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
	public void rainbomColor(List<String> ipAdresses) throws InterruptedException, IOException {
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
		List<String> ipAdressesList = new ArrayList<String>();
		while(true)  {
			for (String ip : ipAdresses) {
				ipAdressesList = new ArrayList<String>();
				ipAdressesList.add(ip);
				colorsetting.setColor(colors.get(i%colors.size()));
				configureColors(ip, colorsetting);
				i++;
			}
			Thread.sleep(2000);
		}
	}

	public List<String> getIpAdresses() {
		return ipAdresses;
	}

	public void setIpAdresses(List<String> ipAdresses) {
		this.ipAdresses = ipAdresses;
	}

	public ColorSetting getColorStetting() {
		return colorStetting;
	}

	public void setColorStetting(ColorSetting colorStetting) {
		this.colorStetting = colorStetting;
	}

	public NetworkConfiguration getNetworkConfiguration() {
		return networkConfiguration;
	}

	public void setNetworkConfiguration(NetworkConfiguration networkConfiguration) {
		this.networkConfiguration = networkConfiguration;
	}
	
}
