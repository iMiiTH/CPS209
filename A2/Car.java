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

/* The Car object which is a subclass of Vehicle, essentially just a Vehicle with extra graphics */
public class Car extends Vehicle
{
   /* The unique number that each Car has, which is drawn onto the Car */
   public int identifier;

   /* The default constructor for the Car isn't used; just creates a Car at (0,0) */
   public Car()
   {
      super();
      identifier = 0; 
   }

   /**The constructor for the Car that accepts two coordinates as arguments
    * @param x The x position of the top-left corner of the Car
    * @param y The x position of the top-left corner of the Car
    */
   public Car(int x, int y)
   {
      super(x, y);
      identifier = 0;
   }

   /**The draw component of the Car, draws a body, hood and two wheels, and if there is a hitched vehicle, draws a line from the readPoint to the frontPoint of the other Car. */
   public void draw(Graphics2D g2)
   {
      super.draw(g2);
      g2.drawString(Integer.toString(identifier), xLeft+25, yTop);
      Rectangle body 
         = new Rectangle(xLeft, yTop + 10, 60, 10);      
      Ellipse2D.Double frontTire 
         = new Ellipse2D.Double(xLeft + 10, yTop + 20, 5, 5);
      Ellipse2D.Double rearTire
         = new Ellipse2D.Double(xLeft + 40, yTop + 20, 5, 5);

      // The bottom of the front windshield
      Point2D.Double r1 
         = new Point2D.Double(xLeft + 10, yTop + 10);
      // The front of the roof
      Point2D.Double r2 
         = new Point2D.Double(xLeft + 20, yTop);
      // The rear of the roof
      Point2D.Double r3 
         = new Point2D.Double(xLeft + 40, yTop);
      // The bottom of the rear windshield
      Point2D.Double r4 
         = new Point2D.Double(xLeft + 50, yTop + 10);

      Line2D.Double frontWindshield 
         = new Line2D.Double(r1, r2);
      Line2D.Double roofTop 
         = new Line2D.Double(r2, r3);
      Line2D.Double rearWindshield
         = new Line2D.Double(r3, r4);

      g2.draw(body);
      g2.draw(frontTire);
      g2.draw(rearTire);
      g2.fill(frontTire);
      g2.fill(rearTire);
      g2.draw(frontWindshield);      
      g2.draw(roofTop);      
      g2.draw(rearWindshield);     
   }
   /**
    * This method sets the identifier of the vehicle that is drawn onto the vehicle.
    * @param value This is the number that will be drawn on the Car.
    */
   public void setValue(int value)
   {
      identifier = value;
      System.out.println("car"+identifier+" added @ "+xLeft+", "+yTop);
   }
   /**
    * This method replaces the Vehicle's toString by making it unique by adding the Car's name and identifier. 
    * @return The concatenated string of all the linked Cars
    */
   public String toString()
   {
      return ("Car"+identifier+"["+xLeft+","+yTop+"]-> "+ next);
   }
}
