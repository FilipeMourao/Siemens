package de.tum.ipraktikum.model;

import java.util.ArrayList;

import de.tum.ipraktikum.model.simulation.Room;

/**
 * Configuration for evolution Process
 */
public abstract class Configuration {

    //Genetic Algorithm configuration
    public static int totalPopulation = 20000;
    public static int totalGeneration = 10000;
    public static String host = "18.197.115.38:8081/gen2_testserver/";
    public static int numberOfTables = 64;
    public static Room defaultRoom = new Room(1500, 800);
    // Penalties
    public static double penaltyForProximity = Double.MAX_VALUE ;
}
