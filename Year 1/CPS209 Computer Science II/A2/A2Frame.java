/**
 * Assignment 2
 * CPS209
 * 2014.04.08
 * @author Mitchell Mohorovich
 */

import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;

/*
 * The A2Frame class contains all of the buttons and the panel, in which it iteracts with.
 */
public class A2Frame extends JFrame
{
   /** The constant sent by the UP move menu to move the truck north. */
   public final int NORTH = 0;
   /** The constant sent by the RIGHT move menu to move the truck east. */
   public final int EAST = 1;
   /** The constant sent by the DOWN move menu to move the truck south. */
   public final int SOUTH = 2;
   /** The constant sent by the LEFT move menu to move the truck west. */
   public final int WEST = 3;
   public final int MOVE_INCREMENT = 20;

   /** A boolean statement to check if the simulation has been started, this allowed the randomize and move commands to only be called when this was true (although I ended up removing it, I'm scared to delete this just in case!) */
   private boolean simulationStarted;
   /** The component that holds all of the Vehicles of the program. */
   private A2Component component;
   /** The random object that is used to randomize the position of the Truck. */
   private Random ran;
   /** A string used to check the name of buttons. */
   private String facename;

   /**
    * The constructor of the A2Frame that creates the main menus and populates them, and creates the component and adds it to itself. 
    */
   public A2Frame()
   {
      simulationStarted = false;
      component = new A2Component();
      this.add(component);
      ran = new Random();
      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);
      menuBar.add(createFileMenu());
      menuBar.add(createEditMenu());
      menuBar.add(createListMenu());
      ran = new Random();
   }

   /**
    * This method starts the simulation by calling it in the panel. 
    */
   void startSimulation()
   {
      simulationStarted = true;
      component.startSimulation();
   }

   /**
    * This method creates a populated File menu. 
    * @return The File menu populated with JMenuItems.
    */
   public JMenu createFileMenu()
   {
      JMenu menu = new JMenu("File");
      menu.add(createNewMenuItem("New"));
      menu.add(createNewExitItem("Exit"));

      return menu;
   }

   /**
    * This method creates a populates Edit menu.
    * @return The Edit menu populated with JMenuItems and nested JMenus.
    */
   public JMenu createEditMenu()
   {
      JMenu menu = new JMenu("Edit");
      menu.add(createNewRandomizeItem("Randomize"));
      menu.add(createMoveMenu("Move"));

      return menu;
   }
   
   /**
    * This method creates a populated List menu.
    * @return The List menu populated with JMenuItems.
    */
   public JMenu createListMenu()
   {
      JMenu menu = new JMenu("List");
      menu.add(createListMenuItem("Add First"));
      menu.add(createListMenuItem("Add Last"));
      menu.add(createListMenuItem("Remove First"));
      menu.add(createListMenuItem("Remove Last"));

      return menu;
   }
   
   /**
    * This method creates a menu item for the List menu.
    * @param name The name of the JMenuItem.
    */
   public JMenuItem createListMenuItem(final String name)
   {
      /**
       * This class implements ActionListener that performs an action based on the button type.
       */
      class FaceItemListener implements ActionListener
      {
         /**
          * This method performs an action based on the button name.
          * @param event The ActionEvent object.
          */
         public void actionPerformed(ActionEvent event)
         {
            facename = name;
            if(facename == "Add First"){
               component.addFirst();
            }
            if(facename == "Add Last"){
               component.addLast();
            }
            if(facename == "Remove First"){
               component.removeFirst();
            }
            if(facename == "Remove Last"){
               component.removeLast();
            }
         }
      }      

      JMenuItem menu = new JMenuItem(name);      
      ActionListener listener = new FaceItemListener();
      menu.addActionListener(listener);
      return menu;
   }

   /**
    * This method creates a populate Move Menu.
    * @param name The name of the JMenuItem
    * @return A populated JMenu.
    */
   public JMenu createMoveMenu(final String name)
   {
      JMenu menu = new JMenu(name);      
      menu.add(createNewMoveItem("Up"));
      menu.add(createNewMoveItem("Down"));
      menu.add(createNewMoveItem("Left"));
      menu.add(createNewMoveItem("Right"));

      return menu;
   }

   /**
    * Creates a New JMenuItem for the File Menu.
    * @param name This is the name of the menu item
    * @return A populated JMenu.
    */
   public JMenuItem createNewMenuItem(final String name)
   {
      /**
       * This class implements ActionListener that performs an action based on the button type.
       */
      class FaceItemListener implements ActionListener
      {
         /**
          * This method performs the action when the button is pressed.
          * @param event The ActionEvent object.
          */
         public void actionPerformed(ActionEvent event)
         {
            System.out.println("New simulation started");
            startSimulation();
         }
      }      

      JMenuItem menu = new JMenuItem(name);      
      ActionListener listener = new FaceItemListener();
      menu.addActionListener(listener);
      return menu;
   }
   /**
    * This method creates the Exit JMenuItem that will exit the program.
    * @param name The name of the JMenuItem
    * @return A JMenuItem with an ActionListener.
    */
   public JMenuItem createNewExitItem(final String name)
   {
      /**
       * This class implements ActionListener that performs an action based on the button type.
       */
      class FaceItemListener implements ActionListener
      {
         /**
          * This method performs the action when the button is pressed.
          * @param event The ActionEvent object.
          */
         public void actionPerformed(ActionEvent event)
         {
            System.out.println("Exiting the program.");
            System.exit(0);
         }
      }      

      JMenuItem menu = new JMenuItem(name);      
      ActionListener listener = new FaceItemListener();
      menu.addActionListener(listener);
      return menu;
   }
   /**
    * This creates a JMenuItem for the Move JMenu.
    * @param name The name of the JMenuItem.
    * @return A named JMenuItem with an ActionListener.
    */
   public JMenuItem createNewMoveItem(final String name)
   {
      /**
       * This class implements ActionListener that performs an action based on the button name.
       */
      class FaceItemListener implements ActionListener
      {
         /**
          * This method performs the action when the button is pressed.
          * @param event The ActionEvent object.
          */
         public void actionPerformed(ActionEvent event)
         {
               if(name.equals("Up")){
                  System.out.println("Trailer moved up.");
                  component.trailer.move(0, MOVE_INCREMENT);
               }
               if(name.equals("Down")){
                  System.out.println("Trailer moved down.");
                  component.trailer.move(2, MOVE_INCREMENT);
               }
               if(name.equals("Right")){
                  System.out.println("Trailer moved Right(EAST).");
                  component.trailer.move(1, MOVE_INCREMENT);
               }
               if(name.equals("Left")){
                  System.out.println("Trailer moved left(WEST).");
                  component.trailer.move(3, MOVE_INCREMENT);
               }
               component.repaint();
         }
      }      

      JMenuItem menu = new JMenuItem(name);      
      ActionListener listener = new FaceItemListener();
      menu.addActionListener(listener);
      return menu;
   }
   /**
    * This method creates the Randomize JMenuItem that will randomize the position of the Truck.
    * @param name The name of the JMenuItem.
    * @return A named JMenuItem with an ActionListener.
    */
   public JMenuItem createNewRandomizeItem(final String name)
   {
      /**
       * This class implements ActionListener that performs an action based on the button type.
       */
      class FaceItemListener implements ActionListener
      {
         /**
          * This method performs the action when the button is pressed.
          * @param event The ActionEvent object.
          */
         public void actionPerformed(ActionEvent event)
         {
            component.randomizeTruckPosition();
         }
      }      

      JMenuItem menu = new JMenuItem(name);      
      ActionListener listener = new FaceItemListener();
      menu.addActionListener(listener);
      return menu;

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
    * This method returns a random y coordinate that is within the frame.
    * @param padding This is the distance from the border of the Frame.
    * @return The random x coordinate.
    */
   public int randomXPosition()
   {
      return ran.nextInt(A2Viewer.WIDTH);
   }
   /**
    * This method returns a random x coordinate that is within the frame.
    * @return The random x coordinate.
    */
   public int randomXPosition(int padding)
   {
      return ran.nextInt(A2Viewer.WIDTH - padding) + padding;
   }
}
