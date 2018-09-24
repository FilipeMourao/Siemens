package com.example.power.ledcode;

import java.util.ArrayList;
import java.util.List;

;

public class Main {
	   public static void main(String[] args) throws Exception {
			Color color = new Color("rgb", 0, 0, 200);
			CollorSetting colorsetting = new CollorSetting("ON", 75, color, "SOLID");
			ConfigureLed cl = new ConfigureLed();
			NetworkConfiguration superBecca = new NetworkConfiguration("SuperBecca", "beccabecca");
			List<String> ipAdresses = new ArrayList<>();
			ipAdresses.add("192.168.1.109");
			ipAdresses.add("192.168.1.102");
			ipAdresses.add("192.168.1.111");
			ipAdresses.add("192.168.1.112");
			cl.rainbomColor(ipAdresses);

	    }

}
