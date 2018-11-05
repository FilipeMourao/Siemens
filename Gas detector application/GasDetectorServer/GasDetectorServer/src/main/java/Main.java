import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
    	Database.createNewDatabase();
    	Database.createNewTable();
    	GasSensorMeasure measure1 = new GasSensorMeasure(1, 2, 3, 1, 2, 3, "1");
    	GasSensorMeasure measure2 = new GasSensorMeasure(8, 7, 2, 8, 7, 4, "2");
    	GasSensorMeasure measure3 = new GasSensorMeasure(9, 0, 4, 9, 6, 5, "3");
    	Database.deleteAllElements();
    	Database.insert(measure1);
    	Database.insert(measure2);
    	Database.insert(measure3);
    	List<GasSensorMeasure> list = Database.selectAll();
    	for(GasSensorMeasure measure : list) {
    		System.out.println(measure.toString());
    	}

    }

}
