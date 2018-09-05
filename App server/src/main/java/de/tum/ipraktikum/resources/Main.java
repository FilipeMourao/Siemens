package de.tum.ipraktikum.resources;
import de.tum.ipraktikum.model.Configuration;
import de.tum.ipraktikum.model.simulation.EvolutionConfiguration;
import de.tum.ipraktikum.model.simulation.Room;
import de.tum.ipraktikum.utils.ResettableTimer;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        EvolutionConfiguration evolutionConfiguration = new EvolutionConfiguration();
        Room room = Configuration.defaultRoom;
        evolutionConfiguration.setInitialRoom(room);
        GenerationProcess gP = new GenerationProcess("1", evolutionConfiguration);
        gP.call();
        System.exit(0);
    }
}
