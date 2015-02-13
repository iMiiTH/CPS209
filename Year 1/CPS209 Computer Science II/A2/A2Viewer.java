/**
 * Assignment 2
 * CPS209
 * 2014.04.08
 * @author Mitchell Mohorovich
 */

import javax.swing.*;

public class A2Viewer
{
   /* The height constant for the initial frame width */
   final static int HEIGHT = 600;
   /* The width constant for the initial frame width */
   final static int WIDTH = 800;

   /* The main method that creates and configures the frame */
   public static void main(String[] args)
   {
      JFrame frame = new A2Frame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(WIDTH, HEIGHT);
      frame.setTitle("CPS209: Assignment 2");
      frame.setVisible(true);
   }
}
