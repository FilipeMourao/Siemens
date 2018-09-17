package de.tum.ipraktikum.resources;
import de.tum.ipraktikum.model.Configuration;
import de.tum.ipraktikum.model.simulation.EvolutionConfiguration;
import de.tum.ipraktikum.model.simulation.FixedFurniture;
import de.tum.ipraktikum.model.simulation.Room;
import de.tum.ipraktikum.utils.ResettableTimer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.Gson;

public class Main {

    public static void main(String[] args) throws Exception {
        EvolutionConfiguration evolutionConfiguration = new EvolutionConfiguration();
        String path = Configuration.filePath;
	    BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	    Gson gson = new Gson();		   
	    Room room = gson.fromJson(bufferedReader, Room.class);
        //room = Configuration.defaultRoom;
        evolutionConfiguration.setInitialRoom(room);
        GenerationProcess gP = new GenerationProcess("1", evolutionConfiguration);
        gP.call();
        System.exit(0);
    }
}
