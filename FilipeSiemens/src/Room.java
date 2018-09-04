import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
	int width = 1500;
	int height = 800;
	List<RectangleTable> rectangleTableList = new ArrayList<RectangleTable>();

	List<CircleTable> circleTableList = new ArrayList<CircleTable>();
	public Room(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		int constantDivisor = 30;
		int constantPosition = 100000;
		int numberOfRectangleTables = 16;
		int numberOfCircleTables = 16;
		Random rand = new Random();
		for(int i = 0 ; i < numberOfRectangleTables; i++) {
			RectangleTable ts = new RectangleTable(width/constantPosition + rand.nextInt(width),height/constantPosition + rand.nextInt(height), width/constantDivisor, height/constantDivisor);
			rectangleTableList.add(ts);
		}
		for(int i = 0 ; i < numberOfCircleTables; i++) {
			CircleTable tc = new CircleTable(width/constantPosition + rand.nextInt(width), height/constantPosition +  rand.nextInt(height), width/constantDivisor);
			circleTableList.add(tc);
		}

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

	public List<RectangleTable> getRectangleTableList() {
		return rectangleTableList;
	}

	public void setRectangleTableList(List<RectangleTable> rectangleTableList) {
		this.rectangleTableList = rectangleTableList;
	}

	public List<CircleTable> getCircleTableList() {
		return circleTableList;
	}

	public void setCircleTableList(List<CircleTable> circleTableList) {
		this.circleTableList = circleTableList;
	}
	
}
