
/**
 * assignment 1
 * cps209
 * 2014.03.10
 * 500563037
 * @author mitchell mohorovich
 */

import javax.swing.*;
/**
 * The GameViewer class contains the main method of the Assignment, and creates the JFrame which contains the JPanel, which the game is drawn on and which contains all necessary ActionListeners and MouseListeners.
 */
public class GameViewer
{
   public static void main(String[] args)
   {  
      JPanel panel = new GamePanel();
      JFrame frame = new GameFrame();
      frame.add(panel);
      frame.setTitle("Assignment 1: Mitchell Mohorovich");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
      frame.setResizable(false);
   }
}
