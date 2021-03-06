

import java.util.Collections;
import java.util.List;

import java.util.Random;
public class FixedFurniture  {

	int coordinateX;
	int coordinateY;
	int width;
	int height;
	int radius;
	int type; // 0  walls, 1 lamps, 2 windows, 3 door
	

	public FixedFurniture(int coordinateX, int coordinateY, int width, int height, int type) {// initiate the walls,windows and door
		super();
		this.type = type;
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.width = width;
		this.height = height;
	}
	public FixedFurniture(int coordinateX, int coordinateY, int radius) {// initiate the lamps
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




}
