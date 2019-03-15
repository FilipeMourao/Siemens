package com.example.power.gasdetectorappFirebase.ObjectsAndDatabase;

import java.util.UUID;

public class GasSensorMeasure {

    int id;
    int ID1;
    int ID2;
    int ID3;
    int sensor1;
    int sensor2;
    int sensor3;
    String thermistor;
    private String uniqueID;// the measure ID needs to be a string because the UUID generator generates a unique key bigger than a long
   // int measureTime;

    public GasSensorMeasure(int ID1, int ID2, int ID3, int sensor1, int sensor2, int sensor3, String thermistor) {
        this.ID1 = ID1;
        this.ID2 = ID2;
        this.ID3 = ID3;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.thermistor = thermistor;
        this.uniqueID = UUID.randomUUID().toString();
       // this.measureTime = 5;
    }
    public GasSensorMeasure(int ID1, int ID2, int ID3, int sensor1, int sensor2, int sensor3, String thermistor,String uniqueID) {
        this.ID1 = ID1;
        this.ID2 = ID2;
        this.ID3 = ID3;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.thermistor = thermistor;
        this.uniqueID = uniqueID;
    }

    public GasSensorMeasure(GasSensorMeasure measure) {
        this.ID1 = measure.getID1();
        this.ID2 = measure.getID2();
        this.ID3 = measure.getID3();
        this.sensor1 = measure.getSensor1();
        this.sensor2 = measure.getSensor2();
        this.sensor3 = measure.getSensor3();
        this.thermistor = measure.getThermistor();
        this.uniqueID = UUID.randomUUID().toString();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getID1() {
        return ID1;
    }

    public void setID1(int ID1) {
        this.ID1 = ID1;
    }

    public int getID2() {
        return ID2;
    }

    public void setID2(int ID2) {
        this.ID2 = ID2;
    }

    public int getID3() {
        return ID3;
    }

    public void setID3(int ID3) {
        this.ID3 = ID3;
    }

    public int getSensor1() {
        return sensor1;
    }

    public void setSensor1(int sensor1) {
        this.sensor1 = sensor1;
    }

    public int getSensor2() {
        return sensor2;
    }

    public void setSensor2(int sensor2) {
        this.sensor2 = sensor2;
    }

    public int getSensor3() {
        return sensor3;
    }

    public void setSensor3(int sensor3) {
        this.sensor3 = sensor3;
    }

    public String getThermistor() {
        return thermistor;
    }

    public String getUniquID(){
        return uniqueID;
    }

    public void setThermistor(String thermistor) {
        this.thermistor = thermistor;
    }

    @Override
    public String toString() {
        String stringToReturn = new String();
        stringToReturn += "ID1: " +Integer.toString(ID1) + "\n";
        stringToReturn += "ID2: " +Integer.toString(ID2) + "\n";
        stringToReturn += "ID3: " +Integer.toString(ID3) + "\n";
        stringToReturn += "Sensor1: " +Integer.toString(sensor1) + "\n";
        stringToReturn += "Sensor2: " +Integer.toString(sensor2) + "\n";
        stringToReturn += "Sensor3: " +Integer.toString(sensor3) + "\n";
     //   stringToReturn += "Thermistor " + thermistor + "\n";
        return stringToReturn;
    }
}
