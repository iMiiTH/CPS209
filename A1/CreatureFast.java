
/**
 * Assignment 1
 * CPS209
 * 2014.03.10
 * @author Mitchell Mohorovich
 * 500563037
 */

import java.awt.Color;
import java.util.*;
import java.awt.geom.*;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Color;

/**
 * This is the CreatureFast object, an extension of the Creature class and an implementation of MoveableShape.<p>
 *
 * This Creature is smaller, and faster than CreatureSlow and features more randomized movement, edge detection and 
 * primitive evasive tactics.
 */

public class CreatureFast extends Creature implements MoveableShape 
{
   /** The main body of the Creature */
   Ellipse2D.Double body;             
   /** an Ellipse2D.Double object to show the user the evasion detection radius */
   Ellipse2D.Double evasionRange;  
   /**
    * The maximum distance between the predator and the Creature in order for evasive tactics to be enabled.
    */
   final int evasionRadius;                      
   
   /**
    * The buffer between the actual edge of the GameFrame.
    * This is used so that the Creature does not exactly touch or go outside the GameFrame.
    */
   final int edgePadding;                       

   /**
    * This counter is used to meaure how often the movement is randomized.
    */
   int randomDirectionCounter;                  

   /**
    * The frequency at which the direciton is randomized.
    */
   final int randomDirectionInterval;

   /**
    * A constant variable used to periodically check if the distance between the Predator and Creature is changing.
    */
   final int evasionDistanceCheckInterval = 30; 

   /**
    * True if there is a predator whos distance is less than the evasionRadius. 
    * Enables evasive tactics if true.
    */
   boolean evading; 

   /**
    * The distance between the Predator and the CreatureFast
    */
   double distance;

   /**
    * The initial saved distance between the Predator and CreatureFast. 
    * Used to check if the distance is increasing or decreasing over time.
    */
   double savedDistance;

   /**
    * The interval at which the Creature moves.
    */
   private final int slowMod = 4;

   /**
    * The Counter used to count the number of pixels moved.
    * Used to slow down the Creature.
    */
   private int distanceCounter; 


   /**
    * The default constuctor for the CreatureFast object.
    * <p>
    * Initializes a CreatureFast with a 10 pixel radius at a ranom x y position. 
    */

   public CreatureFast()
   {
      super(10);

      evading = false;
      evasionRadius = 250;
      evasionRange = new Ellipse2D.Double(leftX, topY, evasionRadius, evasionRadius);

      edgePadding = 10;
      randomDirectionCounter = 0; 
      randomDirectionInterval = 500;
      distanceCounter = 0;

      topY = radius+ (int) (Math.random() * ( ( (GameFrame.FRAME_HEIGHT-radius*2) - (radius) +1 )));
      leftX = radius+ (int)(Math.random() * ( ( (GameFrame.FRAME_WIDTH-radius*2) - (radius) + 1)));
      body = new Ellipse2D.Double(leftX, topY, radius, radius);
   }
   /**
    * The method that modifies the x and y positions of the CreatureFast.
    * <p>
    * Also includes edge detection, which will modify the direction if true.
    */
   public void move()
   {
      if(moveSpeed>1){
         if(distanceCounter % slowMod == 0)
            return;
      }
      //System.out.println("CreatureFast moved");
      randomDirectionCounter++;
      if(randomDirectionCounter % randomDirectionInterval == 0){
         System.out.println("Direction Randomized");
         randomizeDirection(direction);
      }
      if(nearEdge()){
         randomizeDirection(whichEdge());
      }
      if(direction==NORTH){
         topY-=moveSpeed;
      }
      if(direction==EAST){
         leftX+=moveSpeed;
      }
      if(direction==SOUTH){
         topY+=moveSpeed; 
      }
      if(direction==WEST){
         leftX-=moveSpeed;
      }
   }

   /**
    * This method checks if the creature should begin evasive maneuvers.
    * <p>
    * The method checks if the centre coordinates of the other Creature are within the Ellipse2D that is the evasion limit for CreatureFast
    * <p>
    * Using Pythagoreom Theorem, the distance between the centre of each objects is calculated.
    * @param other The other Creature that CreatureFast tries to evade.
    */
   public void evasionCheck(Creature other)
   {
      double x = boundingBox.getCenterX();
      double y = boundingBox.getCenterY();
      double xOther = other.getBox().getCenterX();
      double yOther = other.getBox().getCenterY();
      
      System.out.println(x +", "+y+"  |  " + xOther + ", "+yOther);
      distanceCounter++;
      distance =  Math.sqrt((x-xOther)*(x-xOther) + (y-yOther)*(y-yOther));
      if(distance<evasionRadius/2){
         moveSpeed=2;
      }
      else{
         moveSpeed=1;
      }
      if(distanceCounter % evasionDistanceCheckInterval == 0){
         savedDistance = distance;
         if(savedDistance > distance){
            randomizeDirection(direction);
            return;
         }
         if(distance < evasionRadius/2){
            direction = other.getDirection();
         }

      }
      System.out.println(distance);
   }

   /**
    * Although required by the MoveableShape interface, it is not used by CreatureFast. <p>
    * CreatureFast does not destroy any other objects when it touches them.
    * @return Always returns false because collision is not checked.
    */
   public boolean collide(MoveableShape other)
   {
      return false;
   }

   /**
    * The draw method of the Creature. <p>
    * First reinitializes the parts of the Creature, then draws them.
    * @param g2 The Graphics2D object passed by the GamePanel.
    */
   public void draw(Graphics2D g2)
   {
      reinitializeParts();
      move();
      g2.setColor(Color.GREEN);
      //g2.draw(boundingBox);
      g2.draw(body);
      g2.setColor(Color.BLACK);
      g2.draw(evasionRange);
      //g2.fill(body);
   }

   /**
    * This method reinitializes the various parts that the CreatureFast composes of. <p>
    * this includes the body of CreatureFast, as well as its boundingBox.
    */
   public void reinitializeParts()
   {
      //System.out.println("CreatureFast parts drawn.");
      boundingBox = new Rectangle(leftX, topY, radius, radius);
      body = new Ellipse2D.Double(leftX, topY, radius, radius);
      evasionRange = new Ellipse2D.Double(leftX+radius/2-evasionRadius/2, topY+radius/2-evasionRadius/2, evasionRadius, evasionRadius);
   }

   /**
    * Returns a random direction; essentially a random integer from 0 to 3.
    * @return A random direction from NORTH to WEST. (0-3)
    */
   private int randomDirection()
   {
      return NORTH + (int)(Math.random() * ((WEST - NORTH) +1));
   }

   /**
    * Returns the boundingBox to be used for various calculations.
    * @return Returns the boundingBox of the Creature
    */
   public Rectangle getBox()
   {
      return boundingBox;
   }

   /**
    * Returns a boolean statement if the Creature is near an edge or not.
    * @return True if the Creature is near an edge, False if it is not near an edge.
    */
   private boolean nearEdge()
   {
      if(topY <= edgePadding || leftX <= edgePadding || leftX >= GameFrame.FRAME_WIDTH-edgePadding || topY >= GameFrame.FRAME_HEIGHT - (edgePadding + radius*2)){
         //System.out.println("near an edge!");
         return true;
      }
      return false;
   }

   /**
    * Returns which edge the Creature is closest to.
    * @return An int from 0 to 3, corresponding to the NORTH edge (0) to the WEST edge (3), Numbering increasing clockwise.
    */
   private int whichEdge()
   {
      if(topY <= edgePadding){
         return NORTH;
      }
      if(topY >= GameFrame.FRAME_HEIGHT-(edgePadding + radius*2)){
         return SOUTH;
      }
      if(leftX <= edgePadding){
         return WEST;
      }
      if(leftX >= GameFrame.FRAME_WIDTH-edgePadding){
         return EAST;
      }
      return 0;
   }

   /**
    * Changes the direction to a random direction.
    */
   private void randomizeDirection()
   {
      direction = NORTH + (int)(Math.random() * ((WEST - NORTH) + 1));
   }
   /**
    * Changes the direction to a random direction, with an exception.
    * @param exception Any direction can be returned except for this one.
    */
   private void randomizeDirection(int exception)
   {
      int randomDirection = exception;
      while(randomDirection == exception){
         randomDirection = NORTH + (int)(Math.random() * ((WEST - NORTH) +1));
      }
      direction = randomDirection;
   }
}
