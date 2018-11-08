package com.example.power.gasdetectorapp;

public class GasSensorMeasure {

    int id;
    int ID1;
    int ID2;
    int ID3;
    int sensor1;
    int sensor2;
    int sensor3;
    String thermistor;
   // int measureTime;

    public GasSensorMeasure(int ID1, int ID2, int ID3, int sensor1, int sensor2, int sensor3, String thermistor) {
        this.ID1 = ID1;
        this.ID2 = ID2;
        this.ID3 = ID3;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.thermistor = thermistor;
       // this.measureTime = 5;
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
/*
{
 "ID1": "valID1",
 "ID2": "valID2",
 "ID3": "valID3",
 "sensor1":"valSens1",
 "sensor2":"valSens2",
 "sensor3":"valSens3",

"thermistor":"therm_RAW"
}

* */