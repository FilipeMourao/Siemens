
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.google.gson.*;

public class ShapeDrawing extends JPanel {
		public void paintComponent(Graphics g) {
/*		super.paintComponent(g);
	    	Room newRoom = new Room(1500, 800);
	        GsonBuilder builder = new GsonBuilder();
	        Gson gson = builder.create();
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
					g.fillRect(t.getCoordinateX() + x ,t.getCoordinateY() + y, (int)(t.getWidth()*scalerX), (int)(t.getHeight()*scalerY));
				else 
					g.fillOval(t.getCoordinateX() + x, t.getCoordinateY()+ y, (int)(t.getRadius()*scalerOval), (int)(t.getRadius()*scalerOval));
			} 

*/			
/**/			super.paintComponent(g);
//           String path = "D:\\FilipeSiemens\\Siemens\\JsonRoomFormat16.txt";
           String path = "D:\\filipe\\Meu computador\\TUM\\Siemens estagio\\GitHub\\Siemens\\OptimizedJsonRoomFormat16.txt";
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

			
/**/		
			
		}
      public static void main(String[] args) throws FileNotFoundException {
//    	  ShapeDrawing dR = new ShapeDrawing();
//       	   JFrame jf = new JFrame();
//    	  for (int i = 0; i < 20; i++) {
//         	   dR = new ShapeDrawing();
//    	   jf = new JFrame();
//       	   jf.setTitle("Optimal room "+ Integer.toString(i));
//       	   jf.setSize(2000,1000);
//       	   jf.setVisible(true);
//       	   jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//       	   jf.add(dR);
//    		  
//    	  }
//      }
      	   ShapeDrawing dR = new ShapeDrawing();      	
      	   JFrame jf = new JFrame();
      	   jf.setTitle("Optimal room ");
      	   jf.setSize(2000,1000);
      	   jf.setVisible(true);
      	   jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      	   jf.add(dR);
   		  
           
       }
}