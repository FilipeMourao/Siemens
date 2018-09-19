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

public class RealRoomDrawing {		
	public void paintComponent(Graphics g) {
	
//	       String path = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\OptimizedForArtificialLight.txt";
//		       String path = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\OptimizedForNaturalLight.txt";
//		       String path = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\OptimizedForEqualLight.txt";
//		       String path = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\JsonRoomFormat16Tables.txt";
		       String path = "D:\\FilipeSiemens\\Siemens\\OptimizedForEqualLight.txt";
		       BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(path));
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
		       Gson gson = new Gson();
		        Room room = gson.fromJson(bufferedReader, Room.class);
		        room.setWidth(1200);
		        room.setHeight(600);
		        int x =  (int) ((2000 - room.getWidth())/2);
				int y =  (int) ((1000 - room.getHeight())/2);
				List<FixedFurniture> fixedFurnitures = new ArrayList<FixedFurniture>();
				List<Furniture> furnitures = new ArrayList<Furniture>();
				
				Furniture f = null;
				FixedFurniture ff = null;
				
				// drawing the walls
			    g.setColor(Color.BLACK);
			    int numberOfWalls = 5;
			    int wallBold = 1;
				 g.fillRect(x, 100 + 250, 30, wallBold);fixedFurnitures.add(new FixedFurniture(0, 250, 30, wallBold,0));
				 g.fillRect(x, 100, room.getWidth(), wallBold);fixedFurnitures.add(new FixedFurniture(0, 0, room.getWidth(), wallBold,0));
				 g.fillRect(x, 100 + 350, 60, wallBold);fixedFurnitures.add(new FixedFurniture(0, 350, 60, wallBold,0));
				
				 g.fillRect(x + 60, 100 + 250, 400, wallBold);fixedFurnitures.add(new FixedFurniture(60,250, 400, wallBold,0));
				 g.fillRect(x , 100 + 600, 1130, wallBold);fixedFurnitures.add(new FixedFurniture(0, 600, 1130, wallBold,0));
			     g.fillRect(x  + 100, 100 + 350, 410, wallBold);fixedFurnitures.add(new FixedFurniture(100, 350, 410, wallBold,0));
				 
			     g.fillRect(x + 500, 100 + 250, 620, wallBold);fixedFurnitures.add(new FixedFurniture(500, 250, 620, wallBold,0));
				 g.fillRect(x  + 650, 100 + 350, 480, wallBold);fixedFurnitures.add(new FixedFurniture(650, 350, 480, wallBold,0));
				 g.fillRect(x  + 1180, 100 + 350, 20, wallBold);fixedFurnitures.add(new FixedFurniture(1180, 350, 20, wallBold,0));
				 
				 g.fillRect(x + 1160, 100 + 250, 40, wallBold);fixedFurnitures.add(new FixedFurniture(1160, 250, 40, wallBold,0)); 			 
				 g.fillRect(x + 1130, 100 + 450, 50, wallBold);fixedFurnitures.add(new FixedFurniture(1130, 450, 50, wallBold,0));
					
			    // vertical walls
				 g.fillRect(x, 100, wallBold, 600);fixedFurnitures.add(new FixedFurniture(0, 0,  wallBold,600,0));
				 g.fillRect(x + 80, 100 , wallBold, 250);fixedFurnitures.add(new FixedFurniture(80, 0,  wallBold,250,0));
				 g.fillRect(x + 200, 100 + 350, wallBold, 250);fixedFurnitures.add(new FixedFurniture(200, 350,  wallBold,250,0));				 
				 g.fillRect(x  + 510, 100 + 350, wallBold, 50);fixedFurnitures.add(new FixedFurniture(510, 350,  wallBold,50,0));		 
				 g.fillRect(x  + 510, 100 + 450, wallBold, 150);fixedFurnitures.add(new FixedFurniture(510, 450,  wallBold,150,0));	
				 g.fillRect(x  + 820, 100 + 450, wallBold, 150);fixedFurnitures.add(new FixedFurniture(820, 450,  wallBold,150,0));
				 g.fillRect(x  + 820, 100 + 350, wallBold, 50);fixedFurnitures.add(new FixedFurniture(820, 350,  wallBold,50,0));
				 g.fillRect(x + 1000, 100, wallBold, 250);fixedFurnitures.add(new FixedFurniture(1000, 0,  wallBold,250,0));
				 g.fillRect(x + room.getWidth(), 100, wallBold, 350);fixedFurnitures.add(new FixedFurniture(room.getWidth(), 0,  wallBold,350,0));
				 g.fillRect(x + 1130, 100 + 350, wallBold, 250);fixedFurnitures.add(new FixedFurniture(1130, 350,  wallBold,250,0));
				 g.fillRect(x + 1180, 100 + 350, wallBold, 100);fixedFurnitures.add(new FixedFurniture(1180, 350,  wallBold,100,0));
						
				   
				 
				
		  
				 // drawing the doors
				 g.setColor(new Color(102,51,0));//Brown color
				 g.fillRect(x, 100 +  280, 3, 40);fixedFurnitures.add(new FixedFurniture(0, 280, 3, 40,3));
				 g.fillRect(x + 1199, 100 +  280, 3, 40);fixedFurnitures.add(new FixedFurniture(1199, 280, 3, 40,3));
				 
					// drawing the windows
					g.setColor(Color.gray);
					g.fillRect(x  + 100 , 100  ,50, 3);fixedFurnitures.add(new FixedFurniture(100, 0, 50, 3,2));
					g.fillRect(x  + 400 , 100  ,150, 3);fixedFurnitures.add(new FixedFurniture(400, 0, 150, 3,2));
					g.fillRect(x  + 700 , 100  ,50, 3);fixedFurnitures.add(new FixedFurniture(700, 0, 50, 3,2));
					g.fillRect(x  + 900 , 100  ,50, 3);fixedFurnitures.add(new FixedFurniture(900, 0, 50, 3,2));
					g.fillRect(x  + 1100 , 100  ,50, 3);fixedFurnitures.add(new FixedFurniture(1100, 0, 50, 3,2));
					
					g.fillRect(x  + 50 , 100 + 600  ,50, 3);fixedFurnitures.add(new FixedFurniture(50, 600,50, 3,2));
					g.fillRect(x  + 330 , 100 + 600 ,100, 3);fixedFurnitures.add(new FixedFurniture(330, 600,50, 3,2));
					g.fillRect(x  + 650 , 100 + 600  ,100, 3);fixedFurnitures.add(new FixedFurniture(650, 600,50, 3,2));
					g.fillRect(x  + 950 , 100 + 600  ,100, 3);fixedFurnitures.add(new FixedFurniture(950, 600,50, 3,2));

//					// drawing lamps
					g.setColor(Color.yellow);
					
					g.fillOval(x + 35, 100 + 70, 10, 10);fixedFurnitures.add(new FixedFurniture(35, 70, 10));
					g.fillOval(x + 35, 100 + 180, 10, 10);fixedFurnitures.add(new FixedFurniture(35, 180, 10));
					for (int i = 0; i < 9; i++) {
						g.fillOval(x + 150 + 100*i, 100 + 70, 10, 10);fixedFurnitures.add(new FixedFurniture(150 + 100*i, 70, 10));
						g.fillOval(x + 150 + 100*i, 100 + 180, 10, 10);fixedFurnitures.add(new FixedFurniture(150 + 100*i, 180, 10));
					}
					g.fillOval(x + 1050, 100 + 70, 10, 10);fixedFurnitures.add(new FixedFurniture(1050, 70, 10));
					g.fillOval(x + 1050, 100 + 180, 10, 10);fixedFurnitures.add(new FixedFurniture(1050, 180, 10));
					g.fillOval(x + 1150, 100 + 70, 10, 10);fixedFurnitures.add(new FixedFurniture(1150, 70, 10));
					g.fillOval(x + 1150, 100 + 180, 10, 10);fixedFurnitures.add(new FixedFurniture(1150, 180, 10));
					
					g.fillOval(x + 35, 100 + 420, 10, 10);fixedFurnitures.add(new FixedFurniture(35, 420, 10));
					g.fillOval(x + 35, 100 + 530, 10, 10);fixedFurnitures.add(new FixedFurniture(35, 530, 10));
					g.fillOval(x + 135, 100 + 420, 10, 10);fixedFurnitures.add(new FixedFurniture(135, 420, 10));
					g.fillOval(x + 135, 100 + 530, 10, 10);fixedFurnitures.add(new FixedFurniture(135, 530, 10));
					
					for (int i = 1; i < 7; i++) {
						g.fillOval(x + 150 + 100*i, 100 + 420, 10, 10);fixedFurnitures.add(new FixedFurniture(150 + 100*i, 420, 10));
						g.fillOval(x + 150 + 100*i, 100 + 530, 10, 10);fixedFurnitures.add(new FixedFurniture(150 + 100*i, 530, 10));
					}
					for (int i = 1; i <4; i++) {
						g.fillOval(x + 780 + 100*i, 100 + 420, 10, 10);fixedFurnitures.add(new FixedFurniture(780 + 100*i, 420, 10));
						g.fillOval(x + 780 + 100*i, 100 + 530, 10, 10);fixedFurnitures.add(new FixedFurniture(780 + 100*i, 530, 10));
					}
	
					
					room.setFixedFurniture(fixedFurnitures);
					//21 tables that are put together
					for (int i = 0; i < 21; i++) {
						furnitures.add(new Furniture(0, 0, 20, 40, 0));
						furnitures.add(new Furniture(0, 0, 20, 40, 1));
					}
					// 4  tiny tables
					for (int i = 0; i < 4; i++) {
						furnitures.add(new Furniture(0, 0, 10, 40, 0));
						furnitures.add(new Furniture(0, 0, 10, 40, 1));
					}
					room.setFurniture(furnitures);
					System.out.println("Real Room");
					System.out.println(gson.toJson(room));
					System.out.println(gson.toJson(fixedFurnitures));

				
//			
//				for (int i = 0; i < room.getFixedFurniture().size(); i++) {
//					ff = room.getFixedFurniture().get(i);
//					switch (ff.getType()) {
//					case 0:
//						g.setColor(Color.BLACK);
//						g.fillRect( x + ff.getCoordinateX(),y +  ff.getCoordinateY(), ff.getWidth(), ff.getHeight());
//						break;
//					case 1:
//						g.setColor(Color.YELLOW);
//						 g.fillOval(x/2 + ff.getCoordinateX()  , y/2 + ff.getCoordinateY() ,ff.getRadius(),ff.getRadius());
//						break;
//
//					case 2:
//						g.setColor(Color.gray);
//						g.fillRect(x + ff.getCoordinateX(), y + ff.getCoordinateY(), ff.getWidth(), ff.getHeight());
//						break;
//					case 3:
//						g.setColor(new Color(102,51,0));//Brown color
//						g.fillRect(x + ff.getCoordinateX(),y + ff.getCoordinateY(),ff.getWidth(),ff.getHeight());
//						break;
//					default:
//						break;
//					}
//					
//				}
//				for (int i = 0; i < room.getFurniture().size(); i++) {
//					g.setColor(new Color(102,51,0));//Brown color
//					f = room.getFurniture().get(i);
//
//					switch (f.getType()) {					
//					case 0:
//						g.fillRect(x + f.getCoordinateX(),y + f.getCoordinateY(), f.getWidth(), f.getHeight());
//						break;
//					case 1:
//						g.drawRect(x + f.getCoordinateX(),y + f.getCoordinateY(), f.getWidth(), f.getHeight());
//						break;
//
//
//					default:
//						break;
//					}
//					
//				}
//				
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
