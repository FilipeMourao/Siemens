package de.tum.ipraktikum.model.simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import de.tum.ipraktikum.model.Configuration;
import de.tum.ipraktikum.utils.Tuple;
import io.jenetics.Chromosome;
import io.jenetics.util.ISeq;

public class Room implements Chromosome<Furniture> {

	int width = 1500;
	int height = 800;
	 List<Furniture> furniture = new ArrayList<Furniture>();
	 List<FixedFurniture> fixedFurniture = new ArrayList<FixedFurniture>();
	public Room(int width, int height, List<FixedFurniture> fixedFurnitures) {
		super();
		this.width = width;
		this.height = height;
		this.fixedFurniture = fixedFurnitures;
		Furniture f = null;
		int constantDivisor = 30;
		Random rand = new Random();
		int definedWidth = 50;
		int defindHeight = 50;
		for(int i = 0 ; i < Configuration.numberOfFurnitures; i++) {
				f = new Furniture(rand.nextInt(1000), rand.nextInt(1000), 50, 50, 0 );// rectangle table
				furniture.add(f);
				f = new Furniture(rand.nextInt(1000), rand.nextInt(1000), 50, 50, 1 );// rectangle chair		
			    furniture.add(f);

			
		}


	}
	public  List<FixedFurniture> getFixedFurniture() {
		return fixedFurniture;
	}
	public void setFixedFurniture(List<FixedFurniture> fixedFurniture) {
		this.fixedFurniture = fixedFurniture;
	}
	// changed here
	public Room(int width, int height, List<Furniture> furniture, List<FixedFurniture> fixedFurnitures) {
		///TO DO ADAPT TO GET WIDTH AND HEIGHT FROM EACH TABLE 
		super();
		this.width = width;
		this.height = height;
		this.fixedFurniture = fixedFurnitures;
		List<FixedFurniture> walls =  fixedFurniture.stream().filter(c -> c.getType() == 0).collect(Collectors.toList());
		List<Furniture> rectangleTables = furniture.stream().filter(c -> c.getType() == 0).collect(Collectors.toList());
		//int numberOfCircleTables = Math.toIntExact(furniture.stream().filter(c -> c.getType() == 2).count());
		int numberOfRectangleChairs = Math.toIntExact(furniture.stream().filter(c -> c.getType() == 1).count());
		Furniture f1 = null;
		Furniture f2 = null;
		int constantDivisor = 30;
		Random rand = new Random();
		int definedChairWidth = 15;
		int defindChairHeight = 15;
		int xTable = 0; 
		int xChair = 0;
		int yTable = 0;
		int yChair = 0;
		furniture = new ArrayList<>();
		while(!this.isValid()) {
			furniture = new ArrayList<>();
			for(Furniture table : rectangleTables) {
				xTable = rand.nextInt(width - table.getWidth() ) + table.getWidth();
				yTable = rand.nextInt(height - table.getHeight() ) + table.getHeight();
				switch (rand.nextInt(4)) {
				case 0://chair in the right 
					xChair = xTable + rand.nextInt(Configuration.maximumDistanceBetweenChairAndTable  ) +  table.getWidth() ;
					yChair = yTable;
					break;

				case 1:// chair in the left
					xChair = xTable - rand.nextInt(Configuration.maximumDistanceBetweenChairAndTable  ) -  table.getWidth() ;
					yChair = yTable;
					
					break;

				case 2: //chair down 
					xChair = xTable;
					yChair = yTable - rand.nextInt(Configuration.maximumDistanceBetweenChairAndTable  ) - table.getHeight();
					
					break;

				case 3:// chair in the top 
					xChair = xTable;
					yChair = yTable + rand.nextInt(Configuration.maximumDistanceBetweenChairAndTable  ) + table.getHeight();
					
					break;

				
				default:
					break;
				}
				f1 = new Furniture(xTable,yTable,table.getWidth(),	table.getHeight(),0);
				f2 = new Furniture(xChair,yChair,definedChairWidth,	defindChairHeight,1);
				while (f1.touchedAWall(this) || f2.touchedAWall(this)) {
					xTable = rand.nextInt(width - table.getWidth() ) + table.getWidth();
					yTable = rand.nextInt(height - table.getHeight() ) + table.getHeight();
					switch (rand.nextInt(4)) {
					case 0://chair in the right 
						xChair = xTable + rand.nextInt(Configuration.maximumDistanceBetweenChairAndTable  ) +  table.getWidth() ;
						yChair = yTable;
						break;

					case 1:// chair in the left
						xChair = xTable - rand.nextInt(Configuration.maximumDistanceBetweenChairAndTable  ) -  table.getWidth() ;
						yChair = yTable;
						
						break;

					case 2: //chair down 
						xChair = xTable;
						yChair = yTable - rand.nextInt(Configuration.maximumDistanceBetweenChairAndTable  ) - table.getHeight();
						
						break;

					case 3:// chair in the top 
						xChair = xTable;
						yChair = yTable + rand.nextInt(Configuration.maximumDistanceBetweenChairAndTable  ) + table.getHeight();
						
						break;

					
					default:
						break;
					}
					f1 = new Furniture(xTable,yTable,table.getWidth(),	table.getHeight(),0);
					f2 = new Furniture(xChair,yChair,definedChairWidth,	defindChairHeight,1);
				}
				furniture.add(f1);
				furniture.add(f2);
			}
			this.furniture = furniture;

			
		}

	}

	public  List<Furniture> getFurnitures() {
		return furniture;
	}

	public void setFurnitures(List<Furniture> tables) {
		this.furniture = tables;
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
		Room newRoom = new Room(initialRoom.getWidth(), initialRoom.getHeight(),initialRoom.getFurnitures(), initialRoom.getFixedFurniture());
   	    return newRoom;
    }

    //Crossover
    @Override
    public Chromosome<Furniture> newInstance(ISeq<Furniture> genes) {
        List<Furniture> furnitures = genes.asList();
        Room newRoom = new Room(getWidth(),getHeight(),furnitures,this.getFixedFurniture());
        return newRoom;

    }

    //Mutation
    @Override
    public Chromosome<Furniture> newInstance() {
       
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
	        //newRoom = createRandomRoomFromInitialRoom(Configuration.defaultRoom);

        return newRoom;
    }

    @Override
    public Furniture getGene(int i) {
        return furniture.get(i);
    }

    @Override
    public ISeq<Furniture> toSeq() {
        return ISeq.of(furniture);
    }


    @Override
    public boolean isValid() {
    	// check it there is a null elements
    	if(this.getFurnitures() == null || this.getFixedFurniture() == null || 
    			this.getFixedFurniture().isEmpty() || this.getFurnitures().isEmpty()) return false;
        
    	
        //check coordinateOutOfBounds:
        for(Furniture f : getFurnitures()) {
        	
        	if(f.getCoordinateX() > this.getWidth() ||f.getCoordinateX() < 0 ) return false;
        	if(f.getCoordinateY() > this.getHeight() ||f.getCoordinateY() < 0) return false;
        	
        }
        
    	//Check if there is a table and a chair in the same position
        List<Furniture> listOfTables =  furniture.stream().filter(c -> c.getType() == 0).collect(Collectors.toList());
        List<Furniture> listOfChairs =  furniture.stream().filter(c -> c.getType() == 1).collect(Collectors.toList());
        double euclidianDistance = 0;
        double minimumEuclidianDistance = 0;
        
//        for(Furniture table : listOfTables) {
//        	for(Furniture chair: listOfChairs) {
//        		if(chair.getCoordinateX() - table.getCoordinateX() > 0) {// chair is on the right
//        			if(chair.getCoordinateX() - chair.getWidth()/2 < table.getCoordinateX() + table.getWidth()/2) return false; // chair and table touch each other
//        		}
//        		if(chair.getCoordinateX() - table.getCoordinateX() < 0) {// chair is on the left
//        			if(chair.getCoordinateX() + chair.getWidth()/2 > table.getCoordinateX()/2 - table.getWidth()/2) return false; // chair and table touch each other
//        		}
//        		if(chair.getCoordinateY() - table.getCoordinateY() > 0) {// chair is on top
//        			if(chair.getCoordinateY() - chair.getHeight()/2 < table.getCoordinateY() + table.getHeight()/2) return false; // chair and table touch each other
//        		}
//        		if(chair.getCoordinateY() - table.getCoordinateY() < 0) {// chair is on bottom
//        			if(chair.getCoordinateY() + chair.getHeight()/2 > table.getCoordinateY() - table.getHeight()/2) return false; // chair and table touch each other
//        		}
//        		
//        		
//        		
//        		
//        	}
//        }
        	
        //Check if the chairs follow the minimum distance 
        for(int i = 0; i < listOfChairs.size(); i++) {
    	  for(int j = i + 1; j < listOfChairs.size(); j++) {
    		   
    		  euclidianDistance = calculateEuclidianDistance(listOfChairs.get(i).getCoordinateX(),
    				  listOfChairs.get(i).getCoordinateY(),
    				  listOfChairs.get(j).getCoordinateX(),
    				  listOfChairs.get(j).getCoordinateY());
    		  if(euclidianDistance < Configuration.minimumDistanceBetweenChairs)  return false;
    	  }
      }
        
      // check if there is free space in the door  
        List<FixedFurniture> doors = ( fixedFurniture.stream().filter(c -> c.getType() == 3).collect(Collectors.toList())); 
        for(FixedFurniture door: doors) {
            for (int i = 0; i < listOfTables.size(); i++) {
            	if(calculateEuclidianDistance(
            			listOfTables.get(i).getCoordinateX(),
            			listOfTables.get(i).getCoordinateY(), 
            			door.getCoordinateX(), 
            			door.getCoordinateY()) < Configuration.minimumDistanceFromTheDoor ||
            			calculateEuclidianDistance(
                    			listOfChairs.get(i).getCoordinateX(),
                    			listOfChairs.get(i).getCoordinateY(), 
                    			door.getCoordinateX(), 
                    			door.getCoordinateY()) < Configuration.minimumDistanceFromTheDoor
            			) 	return false;
            	//Horizontal door
            	if(door.getWidth() > door.getHeight()) {
            		if(listOfTables.get(i).getCoordinateX() < door.getCoordinateX() + door.getWidth() && 
            				listOfTables.get(i).getCoordinateX() > door.getCoordinateX() - door.getWidth()) return false;
            	} 
            	// vertical door 
            	else {
            		if(listOfTables.get(i).getCoordinateY() < door.getCoordinateY() + door.getHeight() && 
            				listOfTables.get(i).getCoordinateY() > door.getCoordinateY() - door.getHeight()) return false;
            	}
            }
        }
    
		  
      		  
      //check if every table has a chair  
      Furniture table;
      Furniture chair;
      listOfChairs = furniture.stream().filter(c -> c.getType() == 1).collect(Collectors.toList());
      for (int i = 0; i < listOfTables.size(); i++) {
    	   table = listOfTables.get(i);
    	   chair = null;
    	  for (Furniture c:listOfChairs) {
    		  euclidianDistance = calculateEuclidianDistance(table.getCoordinateX(), table.getCoordinateY()
    				  , c.getCoordinateX(), c.getCoordinateY() );
    		  minimumEuclidianDistance = calculateEuclidianDistance(0, 0,
    				  c.width/2 + table.width/2, c.height/2 + table.height/2);
    		  if(euclidianDistance > minimumEuclidianDistance) chair = c;
    		  
		 }
    	 if(chair != null) listOfChairs.remove(chair); 
	}
      if(!listOfChairs.isEmpty() ) return false;		  
       return true;
      
    }

    public static Room copyRoom(Room room) {
    	Room copyRoom = new Room(room.getWidth(), room.getHeight(), room.getFurnitures(),room.getFixedFurniture());
    	return copyRoom;
    }

    public static double calculateEuclidianDistance(double x1, double y1, double x2, double y2) {
        return (Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
    }

    public double getSumOfTablesDistances() {
    	double sumOfDistances = 0;
    	double distance = 0;
    	double atan;
    	double angle;
        for(int i = 0; i < furniture.size(); i++) {
      	  for(int j = i + 1; j < furniture.size(); j++) {
      		  distance = calculateEuclidianDistance(furniture.get(i).getCoordinateX()
      				  , furniture.get(i).getCoordinateY(), furniture.get(j).getCoordinateX(),
      				  furniture.get(j).getCoordinateY());
      		  if(distance < 100)  sumOfDistances -= Configuration.penaltyForIncorrectProximity;
      		  else sumOfDistances += distance;
 
  				if(furniture.get(i).getCoordinateX() == furniture.get(j).getCoordinateX()) sumOfDistances -= furniture.get(i).getHeight();
  				else {
  					atan = ((double)(furniture.get(i).getCoordinateY() - furniture.get(j).getCoordinateY()))/(furniture.get(i).getCoordinateX()-furniture.get(j).getCoordinateX());
  	  				angle = Math.atan(atan);
  	  				angle = Math.abs(angle);
  	  				if(angle >= Math.PI/4) sumOfDistances -= furniture.get(i).getHeight()/Math.sin(angle);
  	  				else sumOfDistances -= furniture.get(i).getWidth()/Math.cos(angle);
  					
  				}
  				if(furniture.get(i).getCoordinateX() == furniture.get(j).getCoordinateX()) sumOfDistances -= furniture.get(j).getHeight();
  				else {
  	  				angle = Math.atan((double)((furniture.get(i).getCoordinateY() - furniture.get(j).getCoordinateY())
  	  		  				/(furniture.get(i).getCoordinateX()-furniture.get(j).getCoordinateX())));
  	  				angle = Math.abs(angle);
  	  				if(angle >= Math.PI/4) sumOfDistances -= furniture.get(j).getHeight()/Math.sin(angle);
  	  				else sumOfDistances -= furniture.get(j).getWidth()/Math.cos(angle);
  				}

      	  }
      	  
      	  
      	}
     return sumOfDistances; 
    }
 
   public  Tuple<Double, Double> getDistancePerLampMeanAndVariance() {
    	double variance = 0;
        double mean = 0;
        double distance = 0;
        List<Double> minEqualDistance = new ArrayList<Double>();
        List<FixedFurniture> lamps;
        List<FixedFurniture> walls = getFixedFurniture().stream().filter(c -> c.getType() == 0).collect(Collectors.toList());
        
        
        for (Furniture furniture: getFurnitures()) {
            final FixedFurniture wallToTheRigt = getFixedFurniture().stream().filter(c -> c.getCoordinateX() > furniture.getCoordinateX()).collect(Collectors.toList()).get(0);
            final FixedFurniture wallToTheLeft = getFixedFurniture().stream().filter(c -> c.getCoordinateX() < furniture.getCoordinateX()).collect(Collectors.toList()).get(0);
            final FixedFurniture wallToTheTop = getFixedFurniture().stream().filter(c -> c.getCoordinateY() > furniture.getCoordinateY()).collect(Collectors.toList()).get(0);
            final FixedFurniture wallToTheBottom = getFixedFurniture().stream().filter(c -> c.getCoordinateY() < furniture.getCoordinateY()).collect(Collectors.toList()).get(0);getClass();
        	lamps = getFixedFurniture().stream()
        			.filter(c -> c.getType() == 1)
        			.filter(c -> c.getCoordinateX() < wallToTheRigt.getCoordinateX())
        			.filter(c -> c.getCoordinateX() > wallToTheLeft.getCoordinateX())
        			.filter(c -> c.getCoordinateY() < wallToTheTop.getCoordinateY())
        			.filter(c -> c.getCoordinateY() > wallToTheBottom.getCoordinateY())
        			.collect(Collectors.toList());
        	for(FixedFurniture lamp: lamps) {
        		minEqualDistance.add( Room.calculateEuclidianDistance(lamp.getCoordinateX(),
        				lamp.getCoordinateY(), furniture.getCoordinateX(), furniture.getCoordinateY()));
        		
        	}
        }
        	
        mean =  minEqualDistance.stream().reduce(0d, (x,y) -> x+y)/minEqualDistance.size();		
        for (int i = 0; i < minEqualDistance.size(); i++) {
            	variance = (minEqualDistance.get(i) - mean ) * (minEqualDistance.get(i) - mean ); 	
    	}
        variance = variance/minEqualDistance.size(); 
        return new Tuple<>(mean,variance);

    }
 
   public  Tuple<Double, Double> getDistancePerWindowsMeanAndVariance() {
    	double variance = 0;
        double mean = 0;
        double distance = 0;
        List<Double> minEqualDistance = new ArrayList<Double>();
        List<FixedFurniture> windows;
        List<FixedFurniture> walls = getFixedFurniture().stream().filter(c -> c.getType() == 0).collect(Collectors.toList());
        
        
        for (Furniture furniture: getFurnitures()) {
            final FixedFurniture wallToTheRigt = getFixedFurniture().stream().filter(c -> c.getCoordinateX() > furniture.getCoordinateX()).collect(Collectors.toList()).get(0);
            final FixedFurniture wallToTheLeft = getFixedFurniture().stream().filter(c -> c.getCoordinateX() < furniture.getCoordinateX()).collect(Collectors.toList()).get(0);
            final FixedFurniture wallToTheTop = getFixedFurniture().stream().filter(c -> c.getCoordinateY() > furniture.getCoordinateY()).collect(Collectors.toList()).get(0);
            final FixedFurniture wallToTheBottom = getFixedFurniture().stream().filter(c -> c.getCoordinateY() < furniture.getCoordinateY()).collect(Collectors.toList()).get(0);getClass();
        	windows = getFixedFurniture().stream()
        			.filter(c -> c.getType() == 2)
        			.filter(c -> c.getCoordinateX() < wallToTheRigt.getCoordinateX())
        			.filter(c -> c.getCoordinateX() > wallToTheLeft.getCoordinateX())
        			.filter(c -> c.getCoordinateY() < wallToTheTop.getCoordinateY())
        			.filter(c -> c.getCoordinateY() > wallToTheBottom.getCoordinateY())
        			.collect(Collectors.toList());
        	
        	for(FixedFurniture window: windows) {
        		minEqualDistance.add( Room.calculateEuclidianDistance(window.getCoordinateX(),
        				window.getCoordinateY(), furniture.getCoordinateX(), furniture.getCoordinateY()));

        	}
        }
        	mean =  minEqualDistance.stream().reduce(0d, (x,y) -> x+y)/minEqualDistance.size();		
            for (int i = 0; i < minEqualDistance.size(); i++) {
            	variance = (minEqualDistance.get(i) - mean ) * (minEqualDistance.get(i) - mean ); 	
    		}
            variance = variance/minEqualDistance.size();
	
        return new Tuple<>(mean,variance); 
    	
    }
  
  public  Tuple<Double, Double> getEqualLightMeanAndVariance() {
    	double variance = 0;
        double mean = 0;
        double distance = 0;
        List<Double> minEqualDistance = new ArrayList<Double>();
        List<FixedFurniture> windows;
        List<FixedFurniture> lamps;
        List<FixedFurniture> walls = getFixedFurniture().stream().filter(c -> c.getType() == 0).collect(Collectors.toList());
        
        
        for (Furniture furniture: getFurnitures()) {
            final FixedFurniture wallToTheRigt = getFixedFurniture().stream().filter(c -> c.getCoordinateX() > furniture.getCoordinateX()).collect(Collectors.toList()).get(0);
            final FixedFurniture wallToTheLeft = getFixedFurniture().stream().filter(c -> c.getCoordinateX() < furniture.getCoordinateX()).collect(Collectors.toList()).get(0);
            final FixedFurniture wallToTheTop = getFixedFurniture().stream().filter(c -> c.getCoordinateY() > furniture.getCoordinateY()).collect(Collectors.toList()).get(0);
            final FixedFurniture wallToTheBottom = getFixedFurniture().stream().filter(c -> c.getCoordinateY() < furniture.getCoordinateY()).collect(Collectors.toList()).get(0);getClass();
        	windows = getFixedFurniture().stream()
        			.filter(c -> c.getType() == 2)
        			.filter(c -> c.getCoordinateX() < wallToTheRigt.getCoordinateX())
        			.filter(c -> c.getCoordinateX() > wallToTheLeft.getCoordinateX())
        			.filter(c -> c.getCoordinateY() < wallToTheTop.getCoordinateY())
        			.filter(c -> c.getCoordinateY() > wallToTheBottom.getCoordinateY())
        			.collect(Collectors.toList());
        			
        	lamps = getFixedFurniture().stream()
        			.filter(c -> c.getType() == 1)
        			.filter(c -> c.getCoordinateX() < wallToTheRigt.getCoordinateX())
        			.filter(c -> c.getCoordinateX() > wallToTheLeft.getCoordinateX())
        			.filter(c -> c.getCoordinateY() < wallToTheTop.getCoordinateY())
        			.filter(c -> c.getCoordinateY() > wallToTheBottom.getCoordinateY())
        			.collect(Collectors.toList());
        	
        	
        	for(FixedFurniture window: windows) {
        		minEqualDistance.add( Room.calculateEuclidianDistance(window.getCoordinateX(),
        				window.getCoordinateY(), furniture.getCoordinateX(), furniture.getCoordinateY()));
        	}
        	for(FixedFurniture lamp: lamps) {
        		minEqualDistance.add( Configuration.differenceFromNaturalAndArtificialLight*Room.calculateEuclidianDistance(lamp.getCoordinateX(),
        				lamp.getCoordinateY(), furniture.getCoordinateX(), furniture.getCoordinateY()));
        		
        	}
        }
        	
        mean =  minEqualDistance.stream().reduce(0d, (x,y) -> x+y)/minEqualDistance.size();		
        for (int i = 0; i < minEqualDistance.size(); i++) {
            	variance = (minEqualDistance.get(i) - mean ) * (minEqualDistance.get(i) - mean ); 	
    	}
        variance = variance/minEqualDistance.size(); 
        return new Tuple<>(mean,variance);

    	
    }

  @Override
	public Iterator<Furniture> iterator() {
		// TODO Auto-generated method stub
		return furniture.iterator();
	}

  @Override
	public int length() {
		// TODO Auto-generated method stub
		return furniture.size();
	}



}
