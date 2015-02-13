
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
 * This is the CreatureSlow object, an extension of the Creature Object and an implementation of MoveableShape.
 * <p>
 * This Creature is randomply placed, its initial direction is randomized, and the distance in which it travels is randomized as well.
 * <p>
 * This creature is slow, large and features extremely simple back and forth movement. It's x and y positions are randomized, based on the frame height and width, the distance that it will travel is also taken in account, to prevent the Creature from travelling outside the Frame.
 */

public class CreatureSlow extends Creature implements MoveableShape 
{
   /** 
    * A boolean variable that is true if it is moving forward away from its initial x and y positions, 
    * false if it has reached the moveDistance threshhold and is now moving back to its initial x and y position. 
    */
   public boolean forward;          

   /**
    * This constant is randomly generated in the Constructor, it is the maximum extent to which a CreatureSlow will
    * travel away from its initial x and y position.
    */
   public final int moveDistance;

   /**
    * This counter is what enables the program to track how far the Creature has travelled.
    */
   public int moveCounter;

   /**
    * At 60 repaints a second, the minimum movement speed of 1 is still too fast at 60 pixels a second.<p>
    * Thus the movement is slowed using this integer, which delays movement until evey nth repaint, where n is 
    * the value of the delay variable.
    */
   public int delay; 
   /**
    * This counter is incremented each repaint, and is used to control the movement delay of the CreatureSlow
    */
   public int delayCounter; 

   /**
    * This is the consturctor of the CreatureSlow object.
    * This constructor takes no parameters as all the simple CreatureSlow objects are simple.
    * <p>
    *
    * The constructor randomizes their X and Y positions, sets their movespeed and randomizes their
    * moveDistance, which is the extend that they will travel from their initial xy position.
    *
    * <p>
    * 
    * The constructor also creates the body objects of the Creature, and outputs their position when
    * inialized to StdOut. 
    */
   public CreatureSlow()
   {
      super(); //this default constructor sets the qualities global to all Creatures, direction, and their Bounding Box
      moveSpeed = 1; //the minimum movement speed (still too fast at 60px/s)
      radius = 15; 
      moveDistance = 30 + (int)(Math.random()* (150-10) +1);
      //Min + (int)(Math.random() * ((Max - Min) + 1))
      topY = radius+moveDistance + (int) (Math.random() * ( ( (GameFrame.FRAME_HEIGHT-moveDistance-radius*2) - (radius+moveDistance) +1 )));
      leftX = radius+moveDistance + (int)(Math.random() * ( ( (GameFrame.FRAME_WIDTH-moveDistance-radius*2) - (radius+moveDistance) + 1)));
      System.out.println(leftX + " " + topY);
      body = new Ellipse2D.Double (leftX, topY, radius, radius); 
      forward = (Math.random() < 0.5); 
      System.out.println(topY + " " + leftX);
      delay = 2;
      delayCounter = 0;
   }

   /**
    * The method that is called each repaint() call. The basic algorithm for movement is as follows.<p>
    * <li> Check if the delayCounter is equal to the set delay
    * <li> Check if the movement will be vertical or horizontal.
    * <li> Check if the CreatureSlow is moving forwards or backwards.
    * <li> Incremembt the movement by the moveSpeed.
    */
   public void move()
   {
      if(delayCounter==delay){                  //if it is time to update the movement
         delayCounter=0;
         if(direction%2==0){                    //if the direction is north(0) or south(2) then move horizonntally
            if(forward){
               if(moveCounter==moveDistance){   //checks if the moveCounter has reached the threshhold yet.
                  moveCounter=0;                //if so, set it to zero and
                  forward=false;                //reverse the direction. 
               }
               topY += moveSpeed ;              //increment the y position. 
               moveCounter++;                   //increment the number of pixels moved.
            }
            else{
               if(moveCounter==moveDistance){
                  moveCounter=0;
                  forward=true;
               }
               topY -= moveSpeed;
               moveCounter++;
            }
         }
         if(direction%2!=0){                    //if the direction is west(3) or east(1) then move vertically
            if(forward){
               if(moveCounter==moveDistance){   
                  moveCounter=0;
                  forward=false;
               }
               leftX += moveSpeed ;
               moveCounter++ ;
            }
            else{
               if(moveCounter==moveDistance){
                  moveCounter=0;
                  forward=true;
               }
               leftX -= moveSpeed;
               moveCounter++;
            }
         }
      }
      else{                                     //if the delay has not been reached yet, simply increment it.
         delayCounter++;
      }

   }

   /**
    * Required by the MoveableShape Interface, but is not utilized because the CreatureSlow does not check if it collides with others, but others may check if this collides with them.
    */
   public boolean collide(MoveableShape other)
   {
      return false;
   }
   /**
    * The draw method of the CreatureSlow.<p>
    * First reinitializes the parts of the object, then draws the body of the CreatureSlow.
    */
   public void draw (Graphics2D g2)
   {  
      reinitializeParts();
      g2.setColor(Color.RED);
      g2.fill(body);
      g2.setColor(Color.WHITE); 
      g2.draw(body);
      //g2.draw(boundingBox);
   }
   /**
    * This method reinitializes each of the Creature's body parts with the recently incremented x and y positions. It exists to make the draw method less cluttered.
    */
   public void reinitializeParts()
   {
      body = new Ellipse2D.Double (leftX, topY, radius, radius); 
      boundingBox = new Rectangle (leftX, topY, radius, radius);
   }
}
