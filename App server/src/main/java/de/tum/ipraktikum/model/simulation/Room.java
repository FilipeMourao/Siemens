package de.tum.ipraktikum.model.simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import de.tum.ipraktikum.model.Configuration;
import io.jenetics.Chromosome;
import io.jenetics.util.ISeq;

public class Room implements Chromosome<Table> {

	int width = 1500;
	int height = 800;
	List<Table> tables = new ArrayList<Table>();
	public Room(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		Table t = null;
		int constantDivisor = 30;
		Random rand = new Random();
		int definedWidth = 50;
		int defindHeight = 50;
		for(int i = 0 ; i < Configuration.numberOfTables; i++) {
			switch (rand.nextInt(2)) {
			case 0:
				 t = new Table(rand.nextInt(width - definedWidth*2 ) + definedWidth/2,
						rand.nextInt(height - defindHeight*2 ) + defindHeight/2,
						definedWidth,
						defindHeight);
				break;
			case 1:
				 t = new Table(rand.nextInt(width - definedWidth*2) + definedWidth/2,
						rand.nextInt(height - defindHeight*2) + defindHeight/2
						, definedWidth);
				break;
			default:
				break;
			}


			tables.add(t);

			
		}


	}
	// changed here
	public Room(int width, int height, List<Table> tables) {
		super();
		this.width = width;
		this.height = height;
		int numberOfRectangleTables = Math.toIntExact(tables.stream().filter(c -> c.getType() == 0).count()) ;
		int numberOfCircleTables = Math.toIntExact(tables.stream().filter(c -> c.getType() == 1).count());
		Table t = null;
		int constantDivisor = 30;
		Random rand = new Random();
		int definedWidth = 50;
		int defindHeight = 50;
		tables = new ArrayList<>();
		for(int i = 0 ; i < numberOfRectangleTables; i++) {
				 t = new Table(rand.nextInt(width - definedWidth*2 ) + definedWidth/2,
						rand.nextInt(height - defindHeight*2 ) + defindHeight/2,
						definedWidth,
						defindHeight);
			tables.add(t);
			
		}
		for(int i = 0 ; i < numberOfCircleTables; i++) {
			 t = new Table(rand.nextInt(width - definedWidth*2) + definedWidth/2,
						rand.nextInt(height - defindHeight*2) + defindHeight/2
						, definedWidth);
		tables.add(t);
		}
		this.tables = tables;

	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}	
// changed here
    public static Room createRandomRoomFromInitialRoom(Room initialRoom) {
		Room newRoom = new Room(initialRoom.getWidth(), initialRoom.getHeight(),initialRoom.getTables());
//		Room newRoom = new Room(initialRoom.getWidth(), initialRoom.getHeight());
    	return newRoom;
    }

    //Crossover
    @Override
    public Chromosome<Table> newInstance(ISeq<Table> genes) {
        List<Table> tables = genes.asList();
        Room newRoom = new Room(getWidth(),getHeight(),tables);
        return newRoom;

    }

    //Mutation
    @Override
    public Chromosome<Table> newInstance() {
       
        String path = Configuration.filePath;
	       BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	       Gson gson = new Gson();
	        Room room = gson.fromJson(bufferedReader, Room.class);
	        Room newRoom = createRandomRoomFromInitialRoom(room);
	        newRoom = createRandomRoomFromInitialRoom(Configuration.defaultRoom);

        return newRoom;
    }

    @Override
    public Table getGene(int i) {
        return tables.get(i);
    }

    @Override
    public ISeq<Table> toSeq() {
        return ISeq.of(tables);
    }


    @Override
    public boolean isValid() {
        //Check if two tables are in the same position
      for(int i = 0; i < tables.size(); i++) {
    	  for(int j = i + 1; j < tables.size(); j++) {
    		  if (tables.get(i).equals(tables.get(j)) ) return false;
    	  }
      }
      return true;
    }

    public static Room copyRoom(Room room) {
    	Room copyRoom = new Room(room.getWidth(), room.getHeight(), room.getTables());
    	return copyRoom;
    }

    public double calculateEuclidianDistance(double x1, double y1, double x2, double y2) {
        return (Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
    }
    public double getSumOfTablesDistances() {
    	double sumOfDistances = 0;
    	double distance = 0;
    	double atan;
    	double angle;
        for(int i = 0; i < tables.size(); i++) {
      	  for(int j = i + 1; j < tables.size(); j++) {
      		  distance = calculateEuclidianDistance(tables.get(i).getCoordinateX()
      				  , tables.get(i).getCoordinateY(), tables.get(j).getCoordinateX(),
      				  tables.get(j).getCoordinateY());
      		  if(distance < 100)  sumOfDistances -= Configuration.penaltyForProximity;
      		  else sumOfDistances += distance;
 
      		switch (tables.get(i).getType()) {// subtract the inside part of the table i
  			case 0:
  				if(tables.get(i).getCoordinateX() == tables.get(j).getCoordinateX()) sumOfDistances -= tables.get(i).getHeight();
  				else {
  					atan = ((double)(tables.get(i).getCoordinateY() - tables.get(j).getCoordinateY()))/(tables.get(i).getCoordinateX()-tables.get(j).getCoordinateX());
  	  				angle = Math.atan(atan);
  	  				angle = Math.abs(angle);
  	  				if(angle >= Math.PI/4) sumOfDistances -= tables.get(i).getHeight()/Math.sin(angle);
  	  				else sumOfDistances -= tables.get(i).getWidth()/Math.cos(angle);
  					
  				}
			break;

      		  case 1:
				sumOfDistances -= tables.get(i).getRadius();
				break;

			default:
				break;
			}
      		switch (tables.get(j).getType()) { // subtract the inside part of the table j
  			case 0:
  				if(tables.get(i).getCoordinateX() == tables.get(j).getCoordinateX()) sumOfDistances -= tables.get(j).getHeight();
  				else {
  	  				angle = Math.atan((double)((tables.get(i).getCoordinateY() - tables.get(j).getCoordinateY())
  	  		  				/(tables.get(i).getCoordinateX()-tables.get(j).getCoordinateX())));
  	  				angle = Math.abs(angle);
  	  				if(angle >= Math.PI/4) sumOfDistances -= tables.get(j).getHeight()/Math.sin(angle);
  	  				else sumOfDistances -= tables.get(j).getWidth()/Math.cos(angle);
  				}
			break;

      		  case 1:
				sumOfDistances -= tables.get(j).getRadius();
				break;

			default:
				break;
			}
      	  }
      	  
      	  
      	}
     return sumOfDistances; 
    }
	@Override
	public Iterator<Table> iterator() {
		// TODO Auto-generated method stub
		return tables.iterator();
	}
	@Override
	public int length() {
		// TODO Auto-generated method stub
		return tables.size();
	}



}
