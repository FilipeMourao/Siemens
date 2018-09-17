package de.tum.ipraktikum.model;

import java.util.ArrayList;
import java.util.List;

import de.tum.ipraktikum.model.simulation.FixedFurniture;
import de.tum.ipraktikum.model.simulation.Room;

/**
 * Configuration for evolution Process
 */
public abstract class Configuration {

    //Genetic Algorithm configuration
    public static int totalPopulation = 100;
    public static int totalGeneration = 1000;
    
    //Parameters of the room
    public static int numberOfFurnitures = 8;
    public static List<FixedFurniture> fixedFurnitures = null; 
    public static Room defaultRoom = new Room(1500, 800,fixedFurnitures);
    public static int differenceFromNaturalAndArtificialLight = 3;
    public static int minimumDistanceBetweenChairs = 100;
    public static int minimumDistanceFromTheDoor = 300;
    public static int maximumDistanceBetweenChairAndTable = 5;


    //Weights
    public static double windowsProximityWeight = 0;
    public static double lampProximityWeight = 0;
    public static double equalLightDistributionWeight = 1;
    
    // Penalties
    public static double penaltyForChairProximity = 10000;
    public static double penaltyForTableWithoutChair = 10000;
    public static double penaltyForIncorrectProximity = 100000;
    public static double penaltyForDoorProximity = 10000000;
    // Based Room
    public static String filePath = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\JsonRoomFormat16Tables.txt";
    public static String fixedFurnitureFilePath = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\CoordinatesOfTheFixedFurnitures.txt";
    
    
}
