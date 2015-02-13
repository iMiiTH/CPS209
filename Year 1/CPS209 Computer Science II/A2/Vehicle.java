/**
 * Assignment 2
 * CPS209
 * 2014.04.08
 * @author Mitchell Mohorovich
 */

import java.awt.Rectangle ;
import java.awt.geom.Ellipse2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/** 
 * Vehicle is an abstract superclass that contains all of the non-visual, linked-list methods common to all Vehicles.
 */
public abstract class Vehicle
{
   /** This is the frontPoint that the Line2D connects to when linked. */
   protected Point2D.Double frontPoint;
   /** This is the rearPoint that the Line2D connects to when linked. */
   protected Point2D.Double rearPoint;
   /** This is the line that represents the hitch when two vehicles are linked. */
   protected Line2D.Double hitch;
   /** This is the x coordinate of the top-left point of the Vehicle. */
   protected int xLeft;
   /** This is the y coordinate of the top-left point of the Vehicle. */
   protected int yTop;
   /** This is an integer representing the difference between the xLeft and x coordinate of the mouse. */
   protected int xCursorDifference;
   /** This is an integer representing the difference between the yTop and the y coordinate of the mouse. */
   protected int yCursorDifference;

   /** This is the height constant of the bounding box. */
   protected final int HEIGHT = 30;
   /** This is the width constant of the bounding box. */
   protected final int WIDTH = 75;
   /** This is the NORTH constant, used when moving the trailer by the menu. */
   public final int NORTH = 0;
   /** This is the EAST constant, used when moving the trailer by the menu. */
   public final int EAST = 1;
   /** This is the SOUTH constant, used when moving the trailer by the menu. */
   public final int SOUTH = 2;
   /** This is the WEST constant, used when moving the trailer by the menu. */
   public final int WEST = 3;

   /** This is the next vehicle in the linked list. */
   public Vehicle next;
   /** This boolean variable makes the Vehicle be drawn RED if the Vehicle is selected. */
   protected boolean isSelected;

   /** This is the bounding box of the Vehicle. */
   protected Rectangle rect;

   /** The default constructor of the Vehicle creates a Vehicle at (0,0). */
   public Vehicle()
   {
      xLeft=0;
      yTop=0;
      rect = new Rectangle(xLeft, yTop, HEIGHT, WIDTH);
      frontPoint = new Point2D.Double(0, 0);
      rearPoint = new Point2D.Double(0, 0);
      next = null;
   }
   /** This constructor creates a Vehicle at a certain position on screen.
    * @param x The xLeft coordinate.
    * @param y the yTop coordinate. 
    */
   public Vehicle(int x, int y)
   {
      next = null;
      xLeft = x;
      yTop = y;
      frontPoint = new Point2D.Double(x, y+10);
      rearPoint = new Point2D.Double(x+WIDTH-10, y+10);
      rect = new Rectangle(xLeft, yTop, HEIGHT, WIDTH);
   }
   /** This method is empty, it is used by some subclasses.
    * @param direction The direction in which to move the Vehicle in.
    * @param increment The amount of pixels to move the Vehicle by.
    */
   public void move(int direction, int increment)
   {
   }
   /**
    * The draw method of the Vehicle which is only responsible in drawing the hitch between other Vehicles. 
    * @param g2 The Graphics2D object that is used to draw all of the shapes.
    */
   public void draw(Graphics2D g2)
   {
      if(isSelected)
         g2.setColor(Color.RED);
      else
         g2.setColor(Color.BLACK);
      rect = new Rectangle(xLeft, yTop, WIDTH, HEIGHT);
      //g2.draw(rect);
      //g2.drawString(Integer.toString(xLeft) +" "+Integer.toString(yTop), xLeft, yTop+10);


      //The following two lines create the points at the front and rear of the vehicles.
      frontPoint.setLocation(xLeft, yTop+20);
      rearPoint.setLocation(xLeft+WIDTH-15, yTop+20);

      if(next!=null){
         next.setPosition(xLeft+WIDTH, yTop);
         next.draw(g2);
         g2.draw(new Line2D.Double(rearPoint, next.getFrontPoint())); //Draws the hitch between the linked list Vehicles.
      }
   }
   /**
    * This recursively sets the "next" vehicle to the parameter.
    * @param v The vehicle to add to the linked list of Vehicles.
    */
   public void setNext(Vehicle v)
   {
      if(next!=null){
         next.setNext(v);
      }
      else 
         next = v;
   }
   /**
    * This method recursively updates all of the postitions in the linked list, useful when two vehicles interset, to prevent ghosting of other vehicles being drawn in one position, and then recursively drawn behind another vehicle.
    */
   public void setNextPosition()
   {
      if(next!=null){
         next.setNextPosition(xLeft+WIDTH, yTop);
      }
   }
   /**
    * This method recursively updates all of the postitions in the linked list, useful when two vehicles interset, to prevent ghosting of other vehicles being drawn in one position, and then recursively drawn behind another vehicle.
    * @param x The xLeft position of the Vehicle.
    * @param y The yTop position of the Vehicle.
    */
   public void setNextPosition(int x, int y)
   {
      xLeft=x;
      yTop=y;
      if(next!=null){
         next.setNextPosition(xLeft+WIDTH, yTop);
      }
   }
   /**
    * This method checks if the Vehicle contains the coordinates given.
    * @param x The x coordinate of the mouse press.
    * @param y The y coordinate of the mouse press.
    * @return True if the rectangle of the Vehicle contains the points, else otherwise.
    */
   public boolean contains(int x, int y)
   {
      if(rect.contains(x,y))
         return true;
      return false; 
   }
   /**
    * This method checks if this Vehicle intersects another by using the rects common to each vehicle.
    * @param v The other Vehicle being checked for collision. 
    * @return true if the two Vehicles intersect, else otherwise. 
    */
   public boolean intersects(Vehicle v)
   {
      if(rect.intersects(v.getRect()))
         return true;
      else 
         return false;
   }
   /**
    * This method returns the position of the Vehicle. 
    * @return The xLeft variable.
    */
   public int getX()
   {
      return xLeft;
   }
   /**
    * This method returns the postition of the Vehicle. 
    * @return The yTop variable. 
    */
   public int getY()
   {
      return yTop;
   }
   /**
    * This method returns the string containin the position of the Vehicle.
    * @return A string containing the xLeft and yTop coordinates.
    */
   public String coordinates()
   {
      return ("("+xLeft+","+yTop+")");
   }
   /**
    * This method sets the postion of the Vehicle.
    * @param x The xLeft positon to be set to.
    * @param y The yTop position to be set to.
    */
   public void setPosition(int x, int y)
   {
      xLeft = x;
      yTop = y;
   }
   /**
    * This method checks the difference between the coordinates of the Vehicle's top-left point and the coordinates of the mouse click/drag.
    * @param x The x coordinate of the Mouse press/drag.
    * @param y The y coordinate of the Mouse press/drag.
    */
   public void setCursorDifference(int x, int y)
   {
      xCursorDifference = x;
      yCursorDifference = y;
   }
   /**
    * This method returns the xCursorDifference so that the vehicle doesnt "jump" when dragged.
    * @return the xcursordifference variable.
    */
   public int getXCursorDifference()
   {
      return xCursorDifference;
   }
   /**
    * This method returns the yCursorDifference so that the vehicle doesnt "jump" when dragged.
    * @return the xcursordifference variable.
    */
   public int getYCursorDifference()
   {
      return yCursorDifference;
   }
   /**
    * This method returns the bounding rectangle of the Vehicle. 
    * @return The rect (bounding box) of the Vehicle.
    */
   public Rectangle getRect()
   {
      return rect;
   }
   /**
    * This method recursively creates a String of the linked list of the Vehicle.
    * @return The concatenated String containing all Vehicles after this.
    */
   public String toString()
   {
      return ("Vehicle["+xLeft+", "+yTop+"] Next: "+next);
   }
   /**
    * This method sets this Vehicle's isSelected variable to be true, then calls setNextSelected for the rest of the linked list.
    */
   public void setSelected()
   {
      //System.out.println(this+" isSelected");
      isSelected = true;
      setNextSelected();
   }
   /**
    * This method sets the isSelected variable of the whole linked list to be true. 
    */
   public void setNextSelected()
   {
      //System.out.println(this+" isSelected");
      isSelected=true;
      if(next!=null){
         next.setNextSelected();
      }
   }
   /**
    * This method sets the entire linked list's isSelected variable to be false.
    */
   public void setNextUnselected()
   {
      if(next!=null){
         next.setNextUnselected();
      }
      isSelected=false;
   }
   /**
    * This method sets only this vehicle's isSelected variable to be false.
    */
   public void setUnselected()
   {
      isSelected = false;
   }
   /**
    * This method makes the next Vehicle to be null.
    */
   public void removeNext()
   {
      next = null;
   }
   /**
    * This method checks if the Vehicle has a next variable.
    * @return True if there is a Vehicle linked to this Vehicle, false otherwise.
    */
   public boolean hasNext()
   {
      return !(next==null);
   }
   /**
    * This method recursively gets the last element of a linked list.
    * @return The last Vehicle of this linked list.
    */
   public Vehicle getLast()
   {
      if(next==null){
         System.out.println(this+" is the last Vehicle");
         return this;
      }
      else
         return next.getLast();
   }
   /**
    * This method checks if this Vehicle isSelected. 
    * @return True if it is selected, false otherwise. 
    */
   public boolean isSelected()
   {
      return isSelected;
   }
   /**
    * This method gets the next Vehicle in the linked list.
    * @return the next Vehicle of this Vehicle. 
    */
   public Vehicle getNext()
   {
      return next;
   }
   /**
    * This method gets the width of the bounding box. 
    * @return The WIDTH constant. 
    */
   public int getWidth()
   {
      return WIDTH;
   }
   /**
    * This method gets the frontPoint of the Vehicle, it is used to recursively draw the hitch between Vehicles.
    * @return The Point2D corresponding to where the hitch should be "connected" to at the front of this Vehicle. 
    */
   public Point2D getFrontPoint()
   {
      return frontPoint;
   }
}
