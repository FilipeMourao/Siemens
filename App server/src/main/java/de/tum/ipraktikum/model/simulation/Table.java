package de.tum.ipraktikum.model.simulation;

import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.jenetics.Gene;
import java.util.Random;
public class Table implements Gene<Table, Table> {

	int coordinateX;
	int coordinateY;
	int width;
	int height;
	int radius;
	int type; // 0  is a rectangle table, 1 is a oval table
	

	public Table(int coordinateX, int coordinateY, int width, int height) {
		super();
		this.type = 0;
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.width = width;
		this.height = height;
	}
	public Table(int coordinateX, int coordinateY, int radius) {
		super();
		this.type = 1;
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.radius = radius;
	}
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

    @Override
    public Table getAllele() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public Table newInstance() {
		Table t;
    	Random rand = new Random();
		int randomType = rand.nextInt(2);
		if(randomType == 0 ) t = new Table(rand.nextInt(1000), rand.nextInt(1000), 50, 50 ); 
		else  t = new Table(rand.nextInt(1000), rand.nextInt(1000), width);

        return t; 
        }

    @Override
    public Table newInstance(Table value) {
		Table t;
    	Random rand = new Random();
		int randomType = rand.nextInt(2);
		if(randomType == 0 ) t = new Table(rand.nextInt(1000), rand.nextInt(1000), 50, 50 ); 
		else  t = new Table(rand.nextInt(1000), rand.nextInt(1000), width);
        return t;
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
        if (!(obj instanceof Table)) {
            return false;
        }
        Table table = (Table) obj;
        boolean equal = true;

        //check if all the attributes are equal
        equal &= table.getType() == this.getType();
        equal &= table.getCoordinateX() == this.getCoordinateX();
        equal &= table.getCoordinateY() == this.getCoordinateY();
        if(equal && this.getType() == 0) {
        	equal &= table.getWidth() == this.getWidth();
        	equal &= table.getHeight() == this.getHeight();
        } else equal &= table.getRadius() == this.getRadius(); 
        return equal;
    }





}
