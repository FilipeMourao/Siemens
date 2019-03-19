package com.example.power.gasdetectorappFirebase.ObjectsAndDatabase;

import java.util.UUID;

public class ClassificationGasMeasure {
    int id;
    int sensor1;
    int sensor2;
    int sensor3;
    private String classification;
    public ClassificationGasMeasure(){//Empty constructor for the firbase

    }
    public ClassificationGasMeasure(int sensor1, int sensor2, int sensor3, String classification) {
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.classification = classification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
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
}
