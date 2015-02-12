
/**
 * Assignment 1
 * CPS209
 * 2014.03.10
 * @author Mitchell Mohorovich
 * 500563037
 */

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Font;

/**
 * The GamePanel class is a subclass of the JPanel, and is the class that runs and directs the Game, it holds all the 
 * MouseListeners, ActionListeners, Timers and Objects required to run the game.
 */
public class GamePanel extends JPanel 
{
   /**
    * The Predator object, this Creature is controlled by the user and "eats" the other Creatures of the game.
    */
   Creature user;

   /**
    * The ArrayList of non-Predator creatures that are contained in the game.
    */
   ArrayList<Creature> list;

   /**
    * The total number of times the arraylist is populated with all types of creatures.
    */
   public final int totalCreatures = 10;

   /**
    * The proportion of loops that would add a CreatureFast
    */
   public final int fastCreatureProportion = 3;

   /**
    * Clockwise and Counterclockwise are used in the changeDirection() method to have the direction modified by the mouse.
    */
   public final int clockwise = 1,
          counterclockwise =-1;

   /**
    * The delay of the timer, 16ms allows for 60 repaints a second.
    */
   final int DELAY = 16 ;


   /**
    * A boolean variable that checks if the initialTime of the on screen timer has been set.
    */
   private boolean initialTimeSet;
   /**
    * Stores the initial time as a string.
    */
   private long initialTime;

   /**
    * Stores the updated time.
    */
   private long time;

   /**
    * Stores the x and y coordinates of the on-screen timer.
    */
   private int timeStringX=10, 
           timeStringY=20;

   /**
    * The timer that is added to the ActionListner, and refreshes the screen at a given interval.
    */
   Timer t;

   /** A boolean statement to check if the panel has been clicked on once, to make sure that the direction doesn't change before starting the game. */
   private boolean clickedOnce;

   /** A boolean statement that is true when the game is over, so that the timer does not get updated after the game is over */
   private boolean gameOver;

   /**
    * The GamePanel constructor constructs a GamePanel with a an ArrayList of prey, an initial time of 0, a Predator, and an ActionListener and MouseListener.
    */
   public GamePanel()
   {
      clickedOnce=false;
      gameOver=false;
      initialTime = 0;
      initialTimeSet = false;
      list = new ArrayList<Creature>();

      for(int i = 0; i<totalCreatures; i++){
         list.add(new CreatureSlow());
         if(i%fastCreatureProportion == 0)
            list.add(new CreatureFast());
      }

      user = new Predator(); 

      ActionListener taskPerformer = new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            repaint();
         }
      };

      t = new Timer(DELAY, taskPerformer);

      MouseAdapter listener = new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            if(!clickedOnce){
               t.start();
               clickedOnce = true;
            }
            System.out.println("mousePressed");
            if(e.getButton()==1){
               if(clickedOnce)
                  user.changeDirection(counterclockwise);
               if(!initialTimeSet){
                  initialTime = System.currentTimeMillis();
                  initialTimeSet=true;
               }
            }
            if(e.getButton()==3){
               if(clickedOnce)
                  user.changeDirection(clockwise);
            }
         }
      };
      addMouseListener(listener);
   }

   /**
    * The paintComponent is inherited by extending JFrame.
    * The paintComponent draws all of the creatures in the game, and all of on screen text and colours.
    *
    * <p>
    *
    * It is also in charge of moving the creatures through their respective method calls and checking if they collide with one another.
    *
    * @param g The Graphics component that is part of the JFrame object.
    */
   public void paintComponent(Graphics g) 
   {
      super.paintComponent(g);
      setBackground(new Color(30, 30, 30));
      Graphics2D g2 = (Graphics2D) g;

      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

      Iterator<Creature> iter = list.iterator();
      while (iter.hasNext()) {
         Creature c = iter.next();
         c.move();
         c.draw(g2);
         c.evasionCheck(user);
         if (user.collide(c))
            iter.remove();
      }  
      /*
         for(Creature c : list){
         c.draw(g2);
         c.move();
         if(user.collide(c)){
         list.remove(c);
         continue;
         }
         }
         */

      user.draw(g2);
      user.move();
      if(list.size()>0) 
         time = System.currentTimeMillis() - initialTime;
      g2.setColor(Color.WHITE);
      if(list.size()==0)
         g2.setColor(Color.GREEN);
      String timeString = new SimpleDateFormat("mm:ss").format(new Date(time));
      //System.out.println(timeString);
      g2.setFont(new Font("Helvetica", Font.PLAIN, 18)); 
      g2.drawString(timeString, timeStringX, timeStringY);
      g2.drawString("Creatures Remaining: " + list.size(), 10, GameFrame.FRAME_HEIGHT-30  );
      if(list.size()==0)
         t.stop();
      //     System.out.println((System.currentTimeMillis()-initialTime) % 100000);
   }
}
