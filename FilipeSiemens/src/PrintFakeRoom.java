
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
/**/		super.paintComponent(g);
			int boldness = 5;
			int size = 60;
			List<FixedFurniture> fixedFurnitures = new ArrayList<FixedFurniture>();
	    	Room newRoom = new Room(1500, 800);
	        GsonBuilder builder = new GsonBuilder();
	        Gson gson = builder.create();
 //           System.out.println(gson.toJson(newRoom));
			g.setColor(Color.BLACK);
			int x,y,width,height;
			width = newRoom.getWidth();
			height = newRoom.getHeight();
			double scalerX,scalerY,scalerOval;
            
			scalerX = scalerY = 1;
			if( width > 2000) scalerX = 2000*0.9/width;
			if( height > 1000) scalerY = 1000*0.9/height;
			x =  (int) ((2000 - width*scalerX)/2);
			y =  (int) ((1000 - height*scalerY)/2);
			g.drawRect(x ,y , (int) (width*scalerX), (int)(height*scalerY));
			
			// drawing the door
			g.setColor(new Color(102,51,0));//Brown color
			g.fillRect((int)(x + width*scalerX/2  - size/2), (int)(height*scalerY) + y - 2 ,size, boldness);
			
			fixedFurnitures.add(new FixedFurniture((int)( width*scalerX/2  - size/2),
					(int)(height*scalerY)  - 2, 
					size, 
					boldness, 
					3));
			// drawing the windows
			g.setColor(Color.gray);//Yellow color
			
			int numberOfWindowsY = 4;
			int constant = 100;
			int initialPositionY = (height - constant)/numberOfWindowsY;
			for(int i = 0; i < numberOfWindowsY; i++) {
				g.fillRect(x - 2 , (int)(initialPositionY*(i+1) + y - size/4 )  ,boldness, size/2);
				g.fillRect((int)(x + width*scalerX - 2), (int)(initialPositionY*(i+1) + y - size/4)  ,boldness, size/2);
				
				fixedFurnitures.add(new FixedFurniture(
						 - 2,
						(int)(initialPositionY*(i+1)  - size/4 ), 
						boldness, 
						size/2,
						2));
				fixedFurnitures.add(new FixedFurniture(
						(int)( width*scalerX - 2),
						 (int)(initialPositionY*(i+1)  - size/4), 
						boldness, 
						size/2,
						2));
				
								
			}
			int numberOfWindowsX = 4;
			constant = 300;
			int initialPositionX = (width - constant)/numberOfWindowsX;
			for(int i = 0; i < numberOfWindowsX; i++) {	
				g.fillRect( (int)(initialPositionX*(i+1) + x - size/4), y - 2  , size/2 ,boldness);
				g.fillRect( (int)(initialPositionX*(i+1) + x - size/4), (int)(y - 2 + height*scalerY), size/2  ,boldness);
				fixedFurnitures.add(new FixedFurniture(
						(int)(initialPositionX*(i+1)  - size/4), 
						 - 2  , 
						size/2 ,
						boldness,
						2));
				fixedFurnitures.add(new FixedFurniture(
						(int)(initialPositionX*(i+1) - size/4), 
						(int)( - 2 + height*scalerY), 
						size/2  ,
						boldness,
						2));
			}
			// drawing the lamps
			g.setColor(Color.YELLOW);//Yellow color
			
			int numberOfRows = 8;
			int numberOfColums = 8;
			int radius = 10;
			int initialRowPosition = (height - 200)/numberOfRows;
			int initialColumPosition = (width - 300)/numberOfColums;
			for (int i = 0; i < numberOfRows; i++) {
				for (int j = 0; j < numberOfColums; j++) {
					if((i+j)%2 == 0) {
					 g.fillOval((int)(1.3*initialColumPosition*( i+ 1) + x/2)
							 , (int)(1.3*initialRowPosition*( j+ 1) + y/2)
							 , radius,
							 radius);
					 fixedFurnitures.add(new FixedFurniture((int)(1.3*initialColumPosition*( i+ 1)),
							 (int)(1.3*initialRowPosition*( j+ 1) ),
							 radius));
					}
					
				}
				
			}
			// drawing the walls
		    g.setColor(Color.BLACK);
		    int numberOfWalls = 5;
		    int wallBold = 1;
			 g.fillRect(x, y  + 575, 202, wallBold);fixedFurnitures.add(new FixedFurniture(0,  575, 202, wallBold,0));
			 g.fillRect(x+ 200, y  + 400, 500, wallBold);fixedFurnitures.add(new FixedFurniture( 200, 400, 500, wallBold,0));
			 g.fillRect(x+ 730, y  + 400, 271, wallBold);fixedFurnitures.add(new FixedFurniture( 730,  400, 271, wallBold,0));
			 g.fillRect(x+ 1000, y  + 400, 300, wallBold);fixedFurnitures.add(new FixedFurniture( 1000,  400, 300, wallBold,0));
			 g.fillRect(x+ 1330, y  + 400, 170, wallBold);fixedFurnitures.add(new FixedFurniture( 1330,  400, 170, wallBold,0));
			 g.fillRect(x+ 1000, y  + 480, 300, wallBold);fixedFurnitures.add(new FixedFurniture( 1000,  480, 300, wallBold,0));
			 g.fillRect(x+ 1330, y  + 480, 170, wallBold);fixedFurnitures.add(new FixedFurniture( 1330,  480, 170, wallBold,0));
			 g.fillRect(x+ 200, y  + 480, 370, wallBold);fixedFurnitures.add(new FixedFurniture( 200,  480, 370, wallBold,0));
			 g.fillRect(x+ 600, y  + 480, 400, wallBold);fixedFurnitures.add(new FixedFurniture( 600,  480, 400, wallBold,0));
		     
			 g.fillRect(x + 200, y  , wallBold, 430);fixedFurnitures.add(new FixedFurniture(200, 0  , wallBold, 430,0));
			 g.fillRect(x + 200, y + 450  , wallBold, 230);fixedFurnitures.add(new FixedFurniture( 200,  450  , wallBold, 230,0));
			 g.fillRect(x+ 200, y  + 700, wallBold, 100);fixedFurnitures.add(new FixedFurniture(200,  700, wallBold, 100,0));
			 g.fillRect(x + 1000, y  , wallBold, 400);fixedFurnitures.add(new FixedFurniture( 1000, 0  , wallBold, 400,0));
			 g.fillRect(x + 1000, y + 480, wallBold, 320);fixedFurnitures.add(new FixedFurniture( 1000, 480, wallBold, 320,0));
			 
			
		    
						
			
			System.out.println("FixedFurniture");
			System.out.println(gson.toJson(fixedFurnitures));


            
/*			g.setColor(Color.gray);
			scalerOval = scalerY;
			if(scalerX < scalerY) scalerOval = scalerX;
			for (Table t: newRoom.getTables()) {
				if (t.type == 0)
					g.fillRect(t.getCoordinateX() + x ,t.getCoordinateY() + y, (int)(50*scalerX), (int)(50*scalerY));
				else 
					g.fillOval(t.getCoordinateX() + x, t.getCoordinateY()+ y, (int)(t.getRadius()*scalerOval), (int)(t.getRadius()*scalerOval));
			} */

/**/			
/*			super.paintComponent(g);
//           String path = "D:\\FilipeSiemens\\Siemens\\JsonRoomFormat16.txt";
           String path = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\OptimizedJsonRoomFormat64Tables.txt";
//           String path = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\JsonRoomFormat16Tables.txt";
	       BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	       Gson gson = new Gson();
	        Room newRoom = gson.fromJson(bufferedReader, Room.class);
	        System.out.println(gson.toJson(newRoom));
			g.setColor(Color.BLACK);
			int x,y,width,height;
			width = newRoom.getWidth();
   		  height = newRoom.getHeight();
			double scalerX,scalerY,scalerOval;
			scalerX = scalerY = 1;
			if( width > 2000) scalerX = 2000*0.9/width;
			if(height > 1000) scalerY = 1000*0.9/height;
			x =  (int) ((2000 - width*scalerX)/2);
			y =  (int) ((1000 - height*scalerY)/2);
			g.drawRect(x ,y , (int) (width*scalerX), (int)(height*scalerY));
		
			g.setColor(Color.gray);
			scalerOval = scalerY;
			if(scalerX < scalerY) scalerOval = scalerX;
			for (Table t: newRoom.getTables()) {
				if (t.type == 0)
					g.fillRect(x + t.getCoordinateX(),y + t.getCoordinateY(), (int)(t.getWidth()*scalerX), (int)(t.getHeight()*scalerY));
				else 
					g.fillOval(x + t.getCoordinateX(),y +  t.getCoordinateY(), (int)(t.getRadius()*scalerOval), (int)(t.getRadius()*scalerOval));
			} 

			
*/		
			
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
      //	  ShapeDrawing dR = new ShapeDrawing();
      //	   JFrame jf = new JFrame();
      // for (int i = 0; i < 100; i++) {
      //	   dR = new ShapeDrawing();
      //  jf = new JFrame();
      //	   jf.setTitle("Optimal room "+ Integer.toString(i));
      //	   jf.setSize(2000,1000);
      //	   jf.setVisible(true);
      //	   jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //	   jf.add(dR);
	  
      // }
      //}

 }
    	  
      

    	  

       
