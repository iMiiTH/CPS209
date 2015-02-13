/**
 * Assignment 2
 * CPS209
 * 2014.04.08
 * @author Mitchell Mohorovich
 */

import java.awt.Rectangle ;
import java.awt.Color;
import java.awt.geom.*;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
  * Truck is a vehicle that can pull a chain of cars.
  */
public class Truck extends Vehicle
{

   /**
     Constants
     */
   private static final double WIDTH = 35 ;
   private static final double UNIT = WIDTH / 7 ;
   private static final double LENGTH_FACTOR = 14 ; // length is 14U
   private static final double HEIGHT_FACTOR = 8 ; // height is 5U
   private static final double U_3 = 0.3 * UNIT ; 
   private static final double U2_5 = 2.5 * UNIT ; 
   private static final double U3 = 3 * UNIT ; 
   private static final double U4 = 4 * UNIT ; 
   private static final double U5 = 5 * UNIT ; 
   private static final double U10 = 10 * UNIT ; 
   private static final double U10_7 = 10.7 * UNIT ; 
   private static final double U12 = 12 * UNIT ; 
   private static final double U13 = 13 * UNIT ; 
   private static final double U14 = 14 * UNIT ; 
   private Rectangle2D.Double hood;
   private Rectangle2D.Double body;
   private Rectangle2D.Double window;
   private Ellipse2D.Double wheel1;
   private Ellipse2D.Double wheel2;
   private Ellipse2D.Double wheel3;
   private Ellipse2D.Double wheel4;
   private Ellipse2D.Double wheel5;
   ArrayList<RectangularShape> list;

   /**
     Constructs truck at position
     @param x the x position
     @param y the y position
     */
   public Truck(int x, int y)
   {
      super();
      xLeft = x;
      yTop = y;
      list = new ArrayList<RectangularShape>();
      list.add(hood);
      list.add(body);
      list.add(wheel1);
      list.add(wheel2);
      list.add(wheel3);
      list.add(wheel4);
      list.add(wheel5);
      System.out.println("Truck added @["+xLeft+", "+yTop+"]");
   }
   /**
    * This method is called by the Frame's move menu.
    * @param direction The direction as defined by the constants in Vehicle.
    * @param increment The number of pixels to move by.
    */
   public void move(int direction, int increment)
   {
      if(xLeft < 0) return;
      if(direction == NORTH)
         yTop-=increment;
      if(direction == EAST)
         xLeft += increment;
      if(direction == SOUTH)
         yTop += increment;
      if(direction == WEST)
         xLeft -= increment;
      //checkBounds();
   }
   /**
    * Checks the bounds of the Trailer.
    */
   public void checkBounds()
   {
      if(yTop < 0)
         yTop = 0;
      if(yTop > A2Viewer.HEIGHT)
         yTop = A2Viewer.HEIGHT - 50;
      if(xLeft < 0)
         xLeft = 0;
      if(xLeft > A2Viewer.WIDTH)
         xLeft = A2Viewer.HEIGHT;
   }

   /**
     Draws the truck
     @param g2 the graphics context
     */
   public void draw(Graphics2D g2)
   {
      super.draw(g2);
      int x1 = xLeft;
      int y1 = yTop;
      g2.setColor(Color.BLACK);
      hood = new Rectangle2D.Double(x1, y1 + UNIT, 
            U3, U3 ) ;
      g2.setColor(Color.red) ;
      g2.fill(hood) ;

      body = new Rectangle2D.Double(x1 + U3,y1-5,
            U10, U4+5) ;
      g2.setColor(Color.blue) ;
      g2.fill(body) ;


      wheel1 = new Ellipse2D.Double(x1 + U_3, 
            y1 + U4, 
            UNIT, UNIT) ;
      g2.setColor(Color.black) ;
      g2.fill(wheel1) ;

      wheel2 = new Ellipse2D.Double(x1 + U3, 
            y1 + U4, 
            UNIT, UNIT) ;
      g2.setColor(Color.black) ;
      g2.fill(wheel2) ;

      wheel3 = new Ellipse2D.Double(x1 + 4 * UNIT, 
            y1 + 4 * UNIT, 
            UNIT, UNIT) ;
      g2.setColor(Color.black) ;
      g2.fill(wheel3) ;

      wheel4 = new Ellipse2D.Double(x1 + U10_7, 
            y1 + U4, 
            UNIT, UNIT) ;
      g2.setColor(Color.black) ;
      g2.fill(wheel4) ;

      wheel5 = new Ellipse2D.Double(x1 + U12, 
            y1 + U4, 
            UNIT, UNIT) ;
      g2.setColor(Color.black) ;
      g2.fill(wheel5) ;
      g2.setColor(Color.WHITE);
      window = new Rectangle2D.Double(x1+1, y1+6, 10, 5);
      g2.fill(window);
      if(isSelected)
         g2.setColor(Color.RED);
      g2.drawString("CPS209", xLeft+16, yTop+14);
      if(next != null)
         next.draw(g2);
   }
   /**
    * This method replaces the toString method of the Vehicle class which returns a string of the entire chain attached to the Vehicle.
    * @return The concatenated String of Vehicles attached to this truck.
    */
   public String toString()
   {
      return ("Truck["+xLeft+","+yTop+"]-> "+next);
   }
}
