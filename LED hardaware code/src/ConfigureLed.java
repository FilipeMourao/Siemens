import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

import sun.net.www.http.HttpClient;
public class ConfigureLed {	
	
	public void configureNetwork(String ipAdress,NetworkConfiguration networkConfiguration ) throws IOException {
		Gson gson = new Gson();
		String jsonString = gson.toJson( networkConfiguration);
		String url = "http://"+ ipAdress + "/api/v1/state";
		 URL obj = new URL(url);
	     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	     // optional default is GET
	     con.setRequestMethod("POST");
	     //add request header
        con.setConnectTimeout(3000);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        OutputStream os = con.getOutputStream();
        os.write(jsonString.getBytes("UTF-8"));
        os.close(); 
	     System.out.println(jsonString);
	     
	     int responseCode = con.getResponseCode();
	     System.out.println("\nSending 'POST' request to URL : " + url);
	     System.out.println("Response Code : " + responseCode);
	     BufferedReader in = new BufferedReader(
	             new InputStreamReader(con.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }

	}
	public void configureColors(String ipAdress, CollorSetting colorStetting ) throws IOException {
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
         OutputStream os = con.getOutputStream();
         os.write(jsonString.getBytes("UTF-8"));
         os.close(); 
	     System.out.println(jsonString);
	     
	     int responseCode = con.getResponseCode();
	     System.out.println("\nSending 'POST' request to URL : " + url);
	     System.out.println("Response Code : " + responseCode);
	     BufferedReader in = new BufferedReader(
	             new InputStreamReader(con.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }
	     
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
		CollorSetting colorsetting = new CollorSetting("ON", 75, violet, "SOLID");
		int i = 0;
		while(true)  {
			for (String ip : ipAdresses) {
				colorsetting.setColor(colors.get(i%colors.size()));
				configureColors(ip, colorsetting);
				i++;
			}
			Thread.sleep(2000);
		}
	}
	
}
