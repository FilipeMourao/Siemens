import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
	int width = 1500;
	int height = 800;
	List<Table> tables = new ArrayList<Table>();
	public Room(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		int constantDivisor = 30;
		int numberOfTables = 4;
		Random rand = new Random();
		for(int i = 0 ; i < numberOfTables; i++) {
			Table ts = new Table(rand.nextInt(width), rand.nextInt(height), width/constantDivisor, height/constantDivisor);
			Table tc = new Table(rand.nextInt(width),  rand.nextInt(height), width/constantDivisor);
			tables.add(ts);
			tables.add(tc);
		}


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
}
