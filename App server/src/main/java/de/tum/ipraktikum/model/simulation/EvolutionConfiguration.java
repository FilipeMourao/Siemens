package de.tum.ipraktikum.model.simulation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvolutionConfiguration {

    @SerializedName("installed_switching_cabinet")
    private Room initialRoom;

    @SerializedName("initial_population")
    private List<Room> initialPopulation;




    public Room getInitialRoom() {
		return initialRoom;
	}

	public void setInitialRoom(Room initialRoom) {
		this.initialRoom = initialRoom;
	}

	public List<Room> getInitialPopulation() {
        return initialPopulation;
    }

    public void setInitialPopulation(List<Room> initialPopulation) {
        this.initialPopulation = initialPopulation;
    }
}
