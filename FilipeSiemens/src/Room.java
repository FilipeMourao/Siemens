import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
	int width = 1500;
	int height = 800;
	List<Furniture> furniture = new ArrayList<Furniture>();
	List<FixedFurniture> fixedFurniture = new ArrayList<FixedFurniture>();
	public Room(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		int constantDivisor = 30;
		int numberOfTables = 16;
		Random rand = new Random();
		for(int i = 0 ; i < numberOfTables; i++) {
			Furniture ts = new Furniture(rand.nextInt(width), rand.nextInt(height), width/constantDivisor, height/constantDivisor, 0);
			Furniture tc = new Furniture(rand.nextInt(width),  rand.nextInt(height), width/constantDivisor, 1, 2);
			furniture.add(ts);
			furniture.add(tc);
		}


	}



	public List<Furniture> getFurniture() {
		return furniture;
	}



	public void setFurniture(List<Furniture> furniture) {
		this.furniture = furniture;
	}



	public List<FixedFurniture> getFixedFurniture() {
		return fixedFurniture;
	}



	public void setFixedFurniture(List<FixedFurniture> fixedFurniture) {
		this.fixedFurniture = fixedFurniture;
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
}
