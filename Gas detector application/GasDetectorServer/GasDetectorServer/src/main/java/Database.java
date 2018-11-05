import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
	final static String URL =  "jdbc:sqlite:D:/FilipeSiemens/Siemens/Gas detector application/GasDetectorServer/GasMeasureDatabase.db";
    private  static final String TABLE_GAS_SENSOR  = "GasSensor";
    private  static final String KEY_ID = "id";
    private  static final String KEY_ID1 = "ID1";
    private  static final String KEY_ID2 = "ID2";
    private  static final String KEY_ID3 = "ID3";
    private  static final String KEY_SENSOR1 = "sensor1";
    private  static final String KEY_SENSOR2 = "sensor2";
    private  static final String KEY_SENSOR3 = "sensor3";
    private  static final String KEY_THERMISTOR = "thermistor";
    public static void createNewDatabase() {
        String url = URL;
//        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
 
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = URL;
//          String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;
            conn = DriverManager.getConnection(url);
            //System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return conn;
    }
    
    public static void createNewTable( ) {
    	Connection connection = connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_GAS_SENSOR  + 
        		"(\n"
                + KEY_ID + " INTEGER PRIMARY KEY," 
                + KEY_ID1 + " TEXT,"
                + KEY_ID2 + " TEXT," 
                + KEY_ID3 + " TEXT,"
                + KEY_SENSOR1 + " TEXT," 
                + KEY_SENSOR2 + " TEXT,"
                + KEY_SENSOR3 + " TEXT," 
                + KEY_THERMISTOR + " TEXT" + ")";
        
        try (Statement stmt = connection.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("New table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void insert(GasSensorMeasure measure) {
        //String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";
        String sql = "INSERT INTO " + TABLE_GAS_SENSOR  + "(" +
        		KEY_ID1  + "," +
        		KEY_ID2 + "," +
        		KEY_ID3 + "," +
        		KEY_SENSOR1 + "," +
        		KEY_SENSOR2 + "," +
        		KEY_SENSOR3 + "," +
        		KEY_THERMISTOR 
        		+ ")"
        		+ " VALUES("
        		+ "?,"
        		+ "?"
        		+ ",?"
        		+ ",?"
        		+ ",?"
        		+ ",?"
        		+ ",?"
        		+ ")";
        Connection connection = connect();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1, Integer.toString(measure.getID1()));
           	pstmt.setString(2, Integer.toString(measure.getID2()));
           	pstmt.setString(3, Integer.toString(measure.getID3()));
           	pstmt.setString(4, Integer.toString(measure.getSensor1()));
          	pstmt.setString(5, Integer.toString(measure.getSensor2()));
          	pstmt.setString(6, Integer.toString(measure.getSensor3()));
          	pstmt.setString(7, measure.getThermistor());
            pstmt.executeUpdate();
            System.out.println("Value added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static String selectElementById( GasSensorMeasure measure ){
    	String sql = "SELECT id, name, capacity "
                + "FROM warehouses WHERE id = ?";
        String result = null;
        try (Connection conn = connect();
                PreparedStatement pstmt  = conn.prepareStatement(sql)){
               
               // set the value
               pstmt.setInt(1,measure.getId());
               //
               ResultSet rs  = pstmt.executeQuery();
               
               // loop through the result set
               while (rs.next()) {
            	   result = rs.getInt("id") +  "\t" + 
                           rs.getString("name") + "\t" +
                           rs.getDouble("capacity");
               }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    
    public static List<GasSensorMeasure> selectAll(){
        String sql = "SELECT " +
        		KEY_ID1  + "," +
        		KEY_ID2 + "," +
        		KEY_ID3 + "," +
        		KEY_SENSOR1 + "," +
        		KEY_SENSOR2 + "," +
        		KEY_SENSOR3 + "," +
        		KEY_THERMISTOR 
        		+ " FROM "
        		+ TABLE_GAS_SENSOR;
        List<GasSensorMeasure> measureList= new ArrayList<>();
        GasSensorMeasure measure;
        
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
            	measure = new GasSensorMeasure(
            			Integer.parseInt(rs.getString(KEY_ID1)),
            			Integer.parseInt(rs.getString(KEY_ID2)),
            			Integer.parseInt(rs.getString(KEY_ID3)),
            			Integer.parseInt(rs.getString(KEY_SENSOR1)),
            			Integer.parseInt(rs.getString(KEY_SENSOR2)),
            			Integer.parseInt(rs.getString(KEY_SENSOR3)),
            			rs.getString(KEY_THERMISTOR));
            	measureList.add(measure);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return measureList;
    }
    public static void deleteAllElements() {
        String sql = "DELETE FROM " + TABLE_GAS_SENSOR ;
 
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
 
    
}
