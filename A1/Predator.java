
/**
 * assignment 1
 * cps209
 * 2014.03.10
 * 500563037
 * @author mitchell mohorovich
 */

import java.awt.Color;
import java.util.*;
import java.awt.geom.*;
import java.awt.Rectangle;
import java.awt.Graphics2D;

/**
 * The Predator Class is a subclass of the Creature Class, and is an implementation of MoveableShape. <p>
 * The Predator is a user controlled Creature, features an animated sprite. Direction is changed via the user's mouse, and the Predator cannnot leave the GameFrame.
 */
public class Predator extends Creature implements MoveableShape
{
   /** This is the movement speed of the Predator object. */
   public final int moveSpeed = 3;

   /** This is the radius of the Arc2D of the Predator. */
   public final int radius = 35;

   /** The x position of the Predator. */
   private int leftX = 50;
   /** The y position of the Predator. */
   private int topY = 50;

   /** Every one in N frames is drawn, where n is the value of frameChangeRate. */
   private final int frameChangeRate = 3;

    /** Various counters used to measure distance and positioning. */
   private int frameCounter;

   /** A counter used to store the current frame of the animation. */
   private int frameNumber;

   /**
    * This boolean value is used to check if the frames of the animation are increasing or decreasing. A boolean variable is used because it oscilates between frames.
    */
   private boolean frameIncreasing;

   /** The body Arc2D of the the Predator. */
   Arc2D.Float body;

   /** The north direction constant */
   public final int NORTH = 0;
   /** The East direction constant. */
   public final int EAST = 1;
   /** The South direction constant. */
   public final int SOUTH = 2; 
   /** The West direction constant. */
   public final int WEST = 3;


   public Predator()
   {
      super();
      direction=EAST;

      frameCounter = 0;
      //body = new Ellipse2D.Double(leftX, topY, 0, 0);
      body = new Arc2D.Float(Arc2D.PIE);
      body.setFrame(leftX, topY, radius, radius);
      body.setAngleStart(45);
      body.setAngleExtent(270);

   }
   /**
    * The Predator is perpetually moving in either a NORTH[0], EAST[1], SOUTH[2] or WEST[3].<p>
    * The rate at which it moves is based on the moveSpeed variable.
    */
   public void move()
   {
      if((direction==NORTH) && (topY > 0)) {
         //System.out.println("NORTH");
         topY-=moveSpeed;
      }
      if(direction==SOUTH && topY < GameFrame.FRAME_HEIGHT-(radius+20)){
         //System.out.println("SOUTH");
         topY+=moveSpeed;
      }
      if(direction==EAST && leftX < GameFrame.FRAME_WIDTH-radius){
         //System.out.println("EAST");
         leftX+=moveSpeed;
      }
      if(direction==WEST && leftX > 0) {
         //System.out.println("WEST");
         leftX-=moveSpeed;
      }
   }
   public void changeDirection(int clockDirection)
   {
      if(clockDirection < NORTH) {
         System.out.println("counterclockwise");
         if(direction==NORTH){
            direction=WEST;
            System.out.println(directionToString());
         }
         else{
            direction+=clockDirection;
            System.out.println(directionToString());
         }
      }
      if(clockDirection > NORTH){
         System.out.println("clockwise");
         if(direction==WEST){
            direction=NORTH;
            System.out.println(directionToString());
         }
         else{
            direction+=clockDirection;
            System.out.println(directionToString());
         }
      }
   }
   /**
    * Checks if the Predator object collides with another MoveableShape object. <p>
    * This is achieved by checking if the centre X and centre Y of the boundingBox of the other object are contained in the body of the Predator.
    * @param other The object that the Predator is checking for collision.
    * @return True if the centre point of the other object is within the Predator, false otherwise.
    */
   public boolean collide(MoveableShape other)
   {
      if(body.contains(other.getBox().getCenterX(), other.getBox().getCenterY())){
         System.out.println(other);
         return true;
      }
      else{
         return false;
      }
   }
   /**
    * Draws the components of the Predator object.
    * @param g2 The Graphics2D object passed by the GameFrame.
    */
   public void draw(Graphics2D g2)
   {
      reinitializeParts();
      g2.setColor(Color.YELLOW);
      //g2.fill(body);
      //g2.setColor(Color.WHITE);
      //g2.draw(boundingBox);
      g2.fill(body);
      g2.setColor(Color.WHITE);
      g2.draw(body);

   }
   /**
    * Reinitializes the various objects that make up the Predator object. 
    * This method also changes the frame of the Predator, dependin on the frame and direction the Arc2D is drawn differently.
    */
   public void reinitializeParts()
   {
      frameCounter++;
      boundingBox = new Rectangle(leftX, topY, radius, radius);
      body = new Arc2D.Float(Arc2D.PIE);
      body.setFrame(leftX, topY, radius, radius);
      if(direction==NORTH){
         if(frameNumber == 0){
            body.setAngleStart(135);
         }
         if(frameNumber == 1){
            body.setAngleStart(120);
         }
         if(frameNumber == 2){
            body.setAngleStart(105);
         }
         if(frameNumber == 3){
            body.setAngleStart(90);
         }

      }
      if(direction==EAST){
         if(frameNumber == 0)
            body.setAngleStart(45);
         if(frameNumber == 1)
            body.setAngleStart(30);
         if(frameNumber == 2)
            body.setAngleStart(15);
         if(frameNumber == 3)
            body.setAngleStart(0);
      }
      if(direction==SOUTH){
         if(frameNumber == 0)
            body.setAngleStart(315);
         if(frameNumber == 1)
            body.setAngleStart(300);
         if(frameNumber == 2)
            body.setAngleStart(285);
         if(frameNumber == 3)
            body.setAngleStart(270);
      }
      if(direction==WEST){
         if(frameNumber == 0)
            body.setAngleStart(225);
         if(frameNumber == 1)
            body.setAngleStart(210);
         if(frameNumber == 2)
            body.setAngleStart(195);
         if(frameNumber == 3)
            body.setAngleStart(180);
      }

      if(frameNumber == 0)
         body.setAngleExtent(270);
      if(frameNumber == 1)
         body.setAngleExtent(300);
      if(frameNumber == 2)
         body.setAngleExtent(330);
      if(frameNumber == 3)
         body.setAngleExtent(360);

      if(frameCounter%frameChangeRate == 0){
         if(frameIncreasing){
            frameNumber++;
            if(frameNumber > 3){
               frameNumber = 3;
               frameIncreasing = false;
            }
         }
            
         if(!frameIncreasing){
            frameNumber--;
            if(frameNumber < 0){
               frameNumber = 0;
               frameIncreasing = true;
            }
         }
      }
   }
   /**
    * Returns the current direction of the Predator as a string.
    * @return The current direction of the Predator as a string.
    */
   public String directionToString()
   {
      String monthString;
      switch(direction){
         case 0: return "NORTH";
         case 1: return "EAST";
         case 2: return "SOUTH";
         case 3: return "WEST";
         default: return "INVALID";
      }
   }
}
