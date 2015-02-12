
/**
 * Assignment 1
 * CPS209
 * 2014.03.10
 * @author Mitchell Mohorovich
 * 500563037
 */
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The GameFrame object is a subclass of JFrame.<p>
 * It is the window that contains the GamePanel.
 */
public class GameFrame extends JFrame
{
   /**
    * The width of the window.
    */
   public static final int FRAME_WIDTH = 800;
   /**
    * The height of the window.
    */
   public static final int FRAME_HEIGHT = 600;

   /**
    * The panel of which the game will be drawn on.
    */
   JPanel panel;

   /**
    * The constructor of the GameFrame produces a window and a panel.
    */
   public GameFrame()
   {
      createPanel();
      setSize(FRAME_WIDTH, FRAME_HEIGHT);
   }
}
