import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.newdawn.slick.SlickException;

public class Entry {
	 static GameLogic L;
	 static Bomber Elemento;
	
	 public static void main(String[] args) throws SlickException {
	        /* Use an appropriate Look and Feel */
	        try {
	            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
	            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	        } catch (UnsupportedLookAndFeelException ex) {
	            ex.printStackTrace();
	        } catch (IllegalAccessException ex) {
	            ex.printStackTrace();
	        } catch (InstantiationException ex) {
	            ex.printStackTrace();
	        } catch (ClassNotFoundException ex) {
	            ex.printStackTrace();
	        }
	        /* Turn off metal's use of bold fonts */
	        UIManager.put("swing.boldMetal", Boolean.FALSE);
	        
	        //Schedule a job for event dispatch thread:
	        //creating and showing this application's GUI.
	        Elemento=new Bomber(1,1);
	        L=new GameLogic(Elemento);
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	createAndShowGUI();
	            }
	        });
	    }
	 
	 public static void createAndShowGUI() {
		 //Create and set up the window.
	        KeyPresses frame = new KeyPresses("BOMBERBMAN",L);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        //Set up the content pane.
	        frame.addComponentsToPane();
	        
	        
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	 }
}
