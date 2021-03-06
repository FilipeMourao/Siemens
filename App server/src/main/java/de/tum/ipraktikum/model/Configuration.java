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
    public static int totalPopulation = 10;
    public static int totalGeneration = 10000;
    
    //Parameters of the room
    public static int numberOfFurnitures = 8;
    public static List<FixedFurniture> fixedFurnitures = null; 
    public static Room defaultRoom = new Room(1500, 800,fixedFurnitures);
    public static int differenceFromNaturalAndArtificialLight = 3;
    public static int minimumDistanceBetweenChairsAndTables = 50;
    public static int minimumDistanceFromTheDoor = 0;
    public static int maximumDistanceBetweenChairAndTable = 5;


    //Weights
    public static double windowsProximityWeight = 0.5f;//natural light 
    public static double lampProximityWeight = 0.2F;// artificial light
    public static double equalLightDistributionWeight = 0.3f; // equal light
    
    // Based Room
//    public static String filePath = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\JsonRoomFormat16Tables.txt";
//    public static String fixedFurnitureFilePath = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\CoordinatesOfTheFixedFurnitures.txt";
//    public static String filePath = "D:\\FilipeSiemens\\Siemens\\JsonRoomFormat16Tables.txt";
//    public static String filePath = "D:\\FilipeSiemens\\Siemens\\RealRoomJson.txt";
//    public static String fixedFurnitureFilePath = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\CoordinatesOfTheFixedFurnitures.txt";

    public static String filePath = "/Users/sunshine/Documents/Filipe/Siemens/RealRoomJson.txt";
    
    public static String fixedFurnitureFilePath = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\CoordinatesOfTheFixedFurnitures.txt";
}
