
/**
 * assignment 1
 * cps209
 * 2014.03.10
 * 500563037
 * @author mitchell mohorovich
 */

import java.awt.*;

/**
 * The MoveableShape interface contains methods common to all MoveableShape objects.
 */
public interface MoveableShape 
{ 
   /**
    * The move method, movement is common to all MoveableShapes.
    */
   void move(); 

   /**
    * The collide method checks if a MoveableShape collides with another.
    */
   boolean collide(MoveableShape other); 

   /**
    * The method required for a MoveableShape to be drawn on a JPanel.
    */
   void draw(Graphics2D g2); 

   /**
    * All moveable shapes have a shape common to them, whether drawn or not that can be accessed.
    */
   Rectangle getBox();
}
