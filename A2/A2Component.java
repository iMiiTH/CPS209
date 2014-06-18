/**
 * Assignment 2
 * CPS209
 * 2014.04.08
 * @author Mitchell Mohorovich
 */

import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * The A2Component is a subclass of JComponent and contains all of the vehicles and all of the MouseListeners and MouseAdapters required to interact with the Vehicles.
 */
public class A2Component extends JComponent
{
   /** The current selected Vehicle */
   private Vehicle selected;
   /** A temporary Vehicle object used throughout the Component. */
   private Vehicle temp;
   /** The main Truck of the component */
   public Vehicle trailer;

   /** The six cars used in the application */
   private Car car1, car2, car3, car4, car5, car6;

   /** The random object used to randomize the positions */ 
   private Random ran;
   
   /** The ArrayList of all the Vehicles used in the application */
   ArrayList<Vehicle> list;

   /** Checks if when the mouse is clicked, a Vehicle was selected, if it wasnt then a new Car can be added. */
   private boolean aVehicleHasBeenSelected;

   /**
    * The Constructor of the A2Component adds all of the MouseListeners and MouseAdapters, and initializes the list of Vehicles, trailer and random objects.
    */
   public A2Component()
   {
      aVehicleHasBeenSelected = false;
      System.out.println("New A2Component created.");
      ran = new Random();
      trailer = new Truck(-200, -200);
      list = new ArrayList<Vehicle>();
      list.add(trailer);

      /**
       * The MouseAdapter class that contains the mousePressed and mouseReleased methods.
       */
      class MyListener extends MouseAdapter
      {
         /**
          * The mousePressed method's main purpose is to to check if a Vehicle has been selected.
          * @param event The MouseEvent object that contains the button type, position etc.
          */
         public void mousePressed(MouseEvent event)
         {  //put the coordinates into variables.
            int a = event.getX();
            int b = event.getY();
            System.out.println("MousePress@["+a+","+b+"]");

            //Makes all of the Vehicles unselected so that in the case no Vehicles contain the mouse,
            //no vehicles are left marked as selected.
            trailer.setUnselected();
            for(Vehicle v : list){
               v.setUnselected();
            }
            selected = null;

            for(Vehicle v : list){     // Loops through the Vehicle list to check if any contain the cursor.
               if(v.contains(a, b)){
                  selected = v;
                  v.setSelected();
                  aVehicleHasBeenSelected = true;
                  v.setCursorDifference(a-v.getX(), b-v.getY());
                  break;
               }
            }


            //If the cursor does not overlap a vehicle, then a vehicle can be added to the Component
            if(!aVehicleHasBeenSelected){
               if(list.size()<7){
               addCar(a-10,b-10);
               }
               selected=null;
            }

            getFirst();
            repaint();
         }
         /**
          * The mouseReleased method's main purpose is to check if the selected vehicle intersects with any other Vehicles in the list. 
          * @param event The MouseEvent object that contains the button type, position etc.
          */
         public void mouseReleased(MouseEvent event)
         {  //if there is no selected vehicle, then this method is pointless.
            if(selected==null) return;

            for(Vehicle v : list){
               if(selected==v)   //make sure the vehicle isn't linked to itself.
                  continue;
               if(selected==trailer)   //the trailer cannot be linked to something.
                  continue;
               if(selected.intersects(v)){
                  v.getLast().next=selected;
                  selected=null;
                  repaint();
                  //recursively sets the position of chain so the repaint(); method doesn't draw ghost vehicles. 
                  v.setNextPosition();    
                  repaint();
                  break;
               }
            }
            System.out.println("Mouse was released");
            repaint();
            aVehicleHasBeenSelected = false;
         }
      }
      addMouseListener(new MyListener());

      /**
       * The MyMouseListener is a class that implements MouseMotionListener to check for the mouse being Dragged and Moved.
       */
      class MyMouseListener implements MouseMotionListener
      {
         /**
          * The mouseDragged method moves the car based on the mouse's position
          * @param event The MouseEvent object that contains the button type, position etc.
          */
         public void mouseDragged(MouseEvent event)
         {
            repaint();
            if(selected != null){
               int a = event.getX();
               int b = event.getY();
               selected.setPosition(a,b);
               selected.setPosition(a-selected.getXCursorDifference(), b-selected.getYCursorDifference());
               repaint();
            }
            repaint();
         }
         /**
          * The mouseMoved method is not used in this program.
          * @param event The MouseEvent object that contains the button type, position etc.
          */
         public void mouseMoved(MouseEvent event)
         {
         }
      }
      addMouseMotionListener(new MyMouseListener());
   }
   /**
    * The startSimulation method resets the whole simulation and is called each time the "New" JMenuItem is pressed.
    */
   public void startSimulation()
   {
      trailer.setPosition(-200, -200);
      trailer.removeNext();
      resetVehicle(car1);
      resetVehicle(car2);
      resetVehicle(car3);
      resetVehicle(car4);
      resetVehicle(car5);
      resetVehicle(car6);
      car1=null;
      car2=null;
      car3=null;
      car4=null;
      car5=null;
      car6=null;
      repaint();
   }
   /**
    * This startSimulation method accepts two coordinates that will be the coordinates of the Trailer.
    * @param x The xLeft position of the Truck.
    * @param y The yTop position of the Truck.
    */
   public void startSimulation(int x, int y)
   {
      System.out.println("startSimulation() called by the trailer");
      startSimulation();
      trailer.setPosition(x, y);
      repaint();
   }
   /**
    * This method makes a vehicle's linked list null and removes it from the list. 
    * @param v The Vehicle to reset.
    */
   public void resetVehicle(Vehicle v)
   {
      if(v==null)
         return;
      v.next=null;
      list.remove(v);
      v=null;
   }
   /**
    * This method moves the Truck to a random position.
    */
   public void randomizeTruckPosition()
   {
      if(trailer.getX()<0) return;
      trailer.setPosition(randomXPosition(20), randomYPosition(20));
      repaint();
   }
   /**
    * This method draws all of the vehicles in the list, as well as all the vehicles they're linked to.
    * @param g The Graphics object.
    */
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

      g2.setColor(Color.BLACK);

      if(selected!=null)
         selected.draw(g2);
      for(Vehicle v : list){
         v.draw(g2);
      }
   }
   /**
    * This method returns a random y coordinate that is within the frame.
    * @return The random y coordinate.
    */
   public int randomYPosition()
   {
      return ran.nextInt(A2Viewer.HEIGHT);
   }
   /**
    * This method returns a random y coordinate that is within the frame.
    * @param padding This is the distance from the border of the Frame.
    * @return The random y coordinate.
    */
   public int randomYPosition(int padding)
   {
      return ran.nextInt(A2Viewer.HEIGHT - padding) + padding;
   }
   /**
    * This method returns a random x coordinate that is within the frame.
    * @return The random x coordinate.
    */
   public int randomXPosition()
   {
      return ran.nextInt(A2Viewer.WIDTH);
   }
   /**
    * This method returns a random y coordinate that is within the frame.
    * @param padding This is the distance from the border of the Frame.
    * @return The random x coordinate.
    */
   public int randomXPosition(int padding)
   {
      return ran.nextInt(A2Viewer.WIDTH - padding) + padding;
   }
   /**
    * This method (slopily) adds a car to the Component.
    * @param x The x coordinate of the new Car.
    * @param y The y coordinate of the new Car.
    */
   public void addCar(int x, int y)
   {
      // If the trailer is out of the frame (it initially is) then the user'll be able to add it to the Component. 
      if(trailer.getX()<0){
         startSimulation(x,y);
         repaint();
         return;
      }
      if(!list.contains(car1)){
         car1 = new Car(x,y);
         car1.setValue(1);
         list.add(car1);
         return;
      }
      if(!list.contains(car2)){
         car2 = new Car(x,y);
         car2.setValue(2);
         list.add(car2);
         return;
      }
      if(!list.contains(car3)){
         car3 = new Car(x,y);
         car3.setValue(3);
         list.add(car3);
         return;
      }
      if(!list.contains(car4)){
         car4 = new Car(x, y);
         car4.setValue(4);
         list.add(car4);
         return;
      }
      if(!list.contains(car5)){
         car5 = new Car(x,y);
         car5.setValue(5);
         list.add(car5);
         return;
      }
      if(!list.contains(car6)){
         car6 = new Car(x,y);
         car6.setValue(6);
         list.add(car6);
         return;
      }
      return;
   }
   /**
    * This method adds a car right after the trailer.
    * make the selected.next be the trailer.next
    * make the trailer.next be selected. 
    */
   public void addFirst()
   {
      if(selected==null||selected==trailer)
         return;
      System.out.println("addFirst() called.");
      selected.getLast().next=trailer.next;
      trailer.next=selected;
      getFirst();
      trailer.setNextPosition();
      repaint();
   }
   /**
    * This method adds a car to the end of the Trailer's linked list. 
    * get the last element of the list and make its next vehicle be selected.
    */
   public void addLast()
   {
      if(selected==null||selected==trailer)
         return;
      System.out.println("addLast() called.");
      if(trailer.next==null){
         trailer.next=selected;
         repaint();
         return;
      }
      trailer.next.getLast().next=selected;
      trailer.setNextPosition();
      repaint();
   }
   /**
    * This method removes the Trailer's next vehicle.
    * Let temp be the next Vehicle of the Truck
    * Make the next vehicle of the Truck be the temp vehicle's next vehicle.
    * Throw the old temp vehicle into the pit of fire. 
    */
   public void removeFirst()
   {
      System.out.println("removeFirst() called.");
      if(trailer.next==null) return;
      Vehicle temp = trailer.next;
      trailer.next = trailer.next.next;
      resetVehicle(temp);
      repaint();
   }
   /**
    * This method removes the last vehicle linked to the Truck.
    * Let secondLast be the second last vehicle.
    * Let temp be the second last vehicle's next vehicle.
    * Make secondLast.next be null.
    * Remove the temp(last) vehicle from the Component.
    */
   public void removeLast()
   {
      System.out.println("removeLast() called");
      if(trailer.next==null) return;

      Vehicle secondLast = null;
      Vehicle temp = trailer.next;

      if(temp.next==null){
         trailer.next=null;
         resetVehicle(temp);
         repaint();
         return;
      }

      while(temp.next!=null){
         if(temp.next.next==null)
            secondLast = temp;
         temp = temp.next;
      }
      resetVehicle(temp);
      secondLast.next = null;
      repaint();
   }
   /**
    * This method (inefficiently) finds the first element when any vehicle is selected.
    */
   public void getFirst()
   {
      if(aVehicleHasBeenSelected){
         for(int i = 0; i<6; i++){
            for(Vehicle v : list)
            {
               if(selected!=v){
                  if(v.next==selected){
                     selected=v;
                     v.setSelected();
                  }
               }
            }
         }
      }
      repaint();
      System.out.println(selected + " is selected.\n");
   }
}
