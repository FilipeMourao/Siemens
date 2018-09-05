package de.tum.ipraktikum.model.simulation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BestResult {

    @SerializedName("generation_number")
    @Expose
    private int generationNumber;

    @SerializedName("switching_cabinets")
    @Expose
    private Room[] rooms;

    public BestResult(int generationNumber, Room[] rooms) {
        super();
        this.generationNumber = generationNumber;
        this.rooms = rooms;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public void setGenerationNumber(int generationNumber) {
        this.generationNumber = generationNumber;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }
}
