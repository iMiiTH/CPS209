
/**
 * Assignment 1
 * CPS209
 * 2014.03.10
 * @author Mitchell Mohorovich
 * 500563037
 */

import java.awt.*;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Color;

/**
 * This class is the Creature class; the superclass of the CreatureSlow and CreatureFast Objects.
 * This class implements MoveableShape.
 *
 * This class acts as a template for all subclasses of it.
 * It holds all the variables that are common to each Creature, such as their x and y positions 
 * as well as their movement direction, speed and creature radius.
 * <p>
 * This class also holds all of the constants used in direction calculations. Finals for NORTH, SOUTH, EAST and WEST.
 * <p>
 * This class also draws a boundingBox for each creature, that is a common rectangle common to all, to 
 * make for simple calculations of collisions and distance.
 */

public class Creature implements MoveableShape
{
   /** Store the initial x position of the creature for use in relative movement. */
   protected int intialX;      
   /** Stores the initial y position of the creature for use in relative movement. */
   protected int initialY;    
   /** Stores the current x position of the Creature */
   protected int leftX;       
   /** Stores the current y position of the Creature */
   protected int topY;        
   /** The current direction of the Creature */
   protected int direction;   
   /** The radius of the main body of the creature, it is also the radius of the bounding box. */
   protected int radius;      
   /** The movement speed of the creature */
   protected int moveSpeed;   

   /** The integer constant for the north direction. */
   public final int NORTH = 0; 
   /** The integer constant for the East direction. */
   public final int EAST = 1; 
   /** The integer constant for the south direction. */
   public final int SOUTH = 2;
   /** The integer constant for the west direction. */
   public final int WEST = 3;

   /** The bounding box of the creature, that allows for collisions ect.
    * This is not drawn */
   public Rectangle boundingBox; // This is the bounding box of the Creature this is not drawn.

   /** This is the main body of the creature. */
   Ellipse2D.Double body;        // This is the body of the Creature, this is drawn.

   /**
    * This method holds all the globalCreatureSetting that are the same no matter the parameters.
    */
   private void globalCreatureSettings()
   {
      direction = NORTH + (int)(Math.random() * ((WEST - NORTH) +1));
      boundingBox = new Rectangle(leftX, topY, radius, radius);
      moveSpeed = 1;
   }
   /**
    * This is the default constructor for Creature objects, just sets the radius as 20, and calls for the globalCreatureSettings Method.
    */
   public Creature()
   {
      radius = 20;
      globalCreatureSettings();
      body = new Ellipse2D.Double(leftX, topY, radius, radius);
   }
   /**
    * This Creature mthod allows for the modification of the default radius.
    * @param radius the integer radius of the main body of the creature and boundingBox
    */
   public Creature(int radius)
   {
      this.radius = radius;
      globalCreatureSettings();
      body = new Ellipse2D.Double(leftX, topY, radius, radius);
   }
   /**
    * The default movement of a creature is just South-East.
    */
   public void move()
   {
      leftX++;
      topY++;
   }
   /**
    * The default Creature does not collide with other objects.
    */
   public boolean collide(MoveableShape other)
   {
      return false;
   }
   /**
    * By default, creatures are drawn with their boundingBox 
    * @param g2 The Graphics 2D object that is used to draw the components of Creature
    */
   public void draw(Graphics2D g2)
   {
      reinitializeParts();
      g2.setColor(Color.WHITE);
      g2.draw(boundingBox);
      g2.draw(body);
   }
   /**
    * This method returns the Rectangle object that is the boundingBox of the Creature.
    */
   public Rectangle getBox()
   {
      return boundingBox;
   }
   /**
    * Returns the Object's coordinates as a String
    */
   public String toString()
   {
      return "Creature@[" +  leftX +", "+topY+"]";
   }
   /**
    * Holds the algorithm necessary for checking if a creature should start to engage in evasion tactics.
    */
   public void evasionCheck(Creature other)
   {
   }
   /**
    * Returns the current direction of the Creature as an integer.
    */
   public int getDirection()
   {
      return direction;
   }
   /**
    * Reinitializes the various parts of the Creature, including the body and the bounding box.
    */
   private void reinitializeParts()
   {
      body = new Ellipse2D.Double(leftX, topY, radius, radius);
      boundingBox = new Rectangle(leftX, topY, radius, radius);
   }

   /**
    * Changes the direction of the Creature based on the value given. <p>
    * In the superclass, this has no purpose because under normal circumstances, the Creature is not controlled by a user.
    */
   public void changeDirection(int change)
   {
   }
}
