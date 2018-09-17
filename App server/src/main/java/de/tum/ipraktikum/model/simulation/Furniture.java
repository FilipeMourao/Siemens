package de.tum.ipraktikum.model.simulation;

import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.jenetics.Gene;
import java.util.Random;
import java.util.stream.Collectors;
public class Furniture implements Gene<Furniture, Furniture> {

	int coordinateX;
	int coordinateY;
	int width;
	int height;
	int radius;
	int type; // 0  is a rectangle table, 1 is a rectangular chair
	

	public Furniture(int coordinateX, int coordinateY, int width, int height, int type) {
		super();
		this.type = type;
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.width = width;
		this.height = height;
	}
//	public Furniture(int coordinateX, int coordinateY, int radius) {
//		super();
//		this.type = 1;
//		this.coordinateX = coordinateX;
//		this.coordinateY = coordinateY;
//		this.radius = radius;
//	}

	public int getCoordinateX() {
		return coordinateX;
	}
	public void setCoordinateX(int coordinateX) {
		this.coordinateX = coordinateX;
	}
	public int getCoordinateY() {
		return coordinateY;
	}
	public void setCoordinateY(int coordinateY) {
		this.coordinateY = coordinateY;
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
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean touchedAWall(Room room) {
		int xLeft;
		int xRight;
		int yDown;
		int yUp;
		List<FixedFurniture > walls = room.getFixedFurniture().stream().filter(c -> c.getType() == 0).collect(Collectors.toList());
		xLeft = this.getCoordinateX() - this.getWidth();
		xRight = this.getCoordinateX() + this.getWidth();
	    yDown = this.getCoordinateY() - this.getHeight();
		yUp = this.getCoordinateY() + this.getHeight();
		if(  (xLeft < room.getWidth()) && ( xRight > room.getWidth()))  return true;
		if(  (yDown < room.getHeight()) && (yUp > room.getHeight())   ) return true;
		if(  (xLeft < 0) && ( xRight > 0))  return true;
		if(  (yDown < 0) && (yUp > 0)   ) return true;
		for(FixedFurniture wall: walls) {
			//vertical walll
			if(wall.getHeight() > wall.getWidth()) {
				// furniture has a wall in the y axis
				if(  (xLeft < wall.getCoordinateX()) && (xRight > wall.getCoordinateX())   ) return true;
			}
			//horizontal walll
			if(wall.getHeight() < wall.getWidth()) {
				// furniture has a wall in the x axis
				if(  (yDown < wall.getCoordinateY()) && (yUp > wall.getCoordinateY())   ) return true;
				
			}
		}
		
		return false;
	}

    @Override
    public Furniture getAllele() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public Furniture newInstance() {
		Furniture f = null;
    	Random rand = new Random();
		switch (rand.nextInt(2)) {
		case 0:
			f = new Furniture(rand.nextInt(1000), rand.nextInt(1000), 50, 50, 0 );// rectangle table
			
			break;
		case 1:
			f = new Furniture(rand.nextInt(1000), rand.nextInt(1000), 50, 50, 1 );// rectangle chair
			
			break;

		case 2:
			//f = new Furniture(rand.nextInt(1000), rand.nextInt(1000), width);// oval table
			break;
		default:
			//f =  new Furniture(rand.nextInt(1000), rand.nextInt(1000), width);// oval table
				break;
				
		}
		return f;
    }

    @Override
    public Furniture newInstance(Furniture value) {
		Furniture f = null;
    	Random rand = new Random();
		switch (rand.nextInt(2)) {
		case 0:
			f = new Furniture(rand.nextInt(1000), rand.nextInt(1000), 50, 50, 0 );// rectangle table
			
			break;
		case 1:
			f = new Furniture(rand.nextInt(1000), rand.nextInt(1000), 50, 50, 1 );// rectangle chair
			
			break;

		case 2:
			//f = new Furniture(rand.nextInt(1000), rand.nextInt(1000), width);// oval table
			break;
		default:
			//f =  new Furniture(rand.nextInt(1000), rand.nextInt(1000), width);// oval table
				break;
				
		}
		return f;
    }
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		if(this.coordinateX > 0 && this.coordinateY > 0 && this.height > 0 && this.width > 0 ) return true;
		if(this.coordinateX > 0 && this.coordinateY > 0 && this.radius > 0 ) return true;
		return false;
	}
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Furniture)) {
            return false;
        }
        Furniture furniture = (Furniture) obj;
        boolean equal = true;

        //check if all the attributes are equal
        equal &= furniture.getType() == this.getType();
        equal &= furniture.getCoordinateX() == this.getCoordinateX();
        equal &= furniture.getCoordinateY() == this.getCoordinateY();
        if(equal && this.getType() == 0) {
        	equal &= furniture.getWidth() == this.getWidth();
        	equal &= furniture.getHeight() == this.getHeight();
        } else equal &= furniture.getRadius() == this.getRadius(); 
        return equal;
    }





}
