
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.google.gson.*;

public class ShapeDrawing extends JPanel {
		public void paintComponent(Graphics g) {
			   String path = "/Users/sunshine/Documents/Filipe/Siemens/OptimizedForNaturalLightGen10_becca.txt";
		    BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(path));
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
		    Gson gson = new Gson();
		     Room room = gson.fromJson(bufferedReader, Room.class);
		     g.setColor(Color.BLACK);
				int x =  (int) ((2000 - room.getWidth())/2);
				int y =  (int) ((1000 - room.getHeight())/2);
				g.drawRect( x,y , room.getWidth(), room.getHeight());
				Furniture f = null;
				FixedFurniture ff = null;
			
				for (int i = 0; i < room.getFixedFurniture().size(); i++) {
					ff = room.getFixedFurniture().get(i);
					switch (ff.getType()) {
					case 0:
						g.setColor(Color.BLACK);
						g.fillRect( x + ff.getCoordinateX(),y +  ff.getCoordinateY(), ff.getWidth(), ff.getHeight());
						break;
					case 1:
						g.setColor(Color.YELLOW);
						 g.fillOval(x + ff.getCoordinateX()  , y + ff.getCoordinateY() ,ff.getRadius(),ff.getRadius());
						break;

					case 2:
						g.setColor(Color.gray);
						g.fillRect(x + ff.getCoordinateX(), y + ff.getCoordinateY(), ff.getWidth(), ff.getHeight());
						break;
					case 3:
						g.setColor(new Color(102,51,0));//Brown color
						g.fillRect(x + ff.getCoordinateX(),y + ff.getCoordinateY(),ff.getWidth(),ff.getHeight());
						break;
					default:
						break;
					}
					
				}
				for (int i = 0; i < room.getFurniture().size(); i++) {
					g.setColor(new Color(102,51,0));//Brown color
					f = room.getFurniture().get(i);

					switch (f.getType()) {					
					case 0:
						g.fillRect(x + f.getCoordinateX(),y + f.getCoordinateY(), f.getWidth(), f.getHeight());
						break;
					case 1:
						g.drawRect(x + f.getCoordinateX(),y + f.getCoordinateY(), f.getWidth(), f.getHeight());
						break;


					default:
						break;
					}
					
				}
				
			}
		public static void main(String[] args) throws FileNotFoundException {
			   ShapeDrawing dR = new ShapeDrawing();      	
			   JFrame jf = new JFrame();
			   jf.setTitle("Optimal room ");
			   jf.setSize(2000,1000);
			   jf.setVisible(true);
			   jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			   jf.add(dR);
				  
		}
			

 }
	  
      

    	  

       
