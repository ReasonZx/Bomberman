package GameLogic;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


@SuppressWarnings("serial")
/**
 * @author João Gomes up201506251@fe.up.pt
 */
public class Bomb extends Element{
	/**
	 * 
	 */
	public static short serialVersionUID = 3;
	transient protected Timer tt = new Timer();
	static protected String Bomb_Black = 	"sprites/normal_bomb.png";
	static protected String Bomb_Red = 	"sprites/bomb_explode.png";
	
	/**
	 * The number of times the bomb will change colors (200ms per change)
	 */
	static protected int blink_count=6;
	
	static protected int Bomb_TickOff=2000;
	
	protected int counter;
	
	/**
	 * Constructor for Bomb Class
	 * <p>
	 * The constructor creates a bomb for the give map in the (x,y) position and then flags it to the Image_Library
	 * </p>
	 * @param x - Position on the x axis of the map
	 * @param y - Position on the y axis of the map
	 * @param lib - The library where Image changes are to be flagged
	 * @param m - The map where the game is being played
	 */
	public Bomb(int x,int y,Image_Library lib,Map m){
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		this.lib=lib;
		this.m=m;
		lib.Flag_For_Change(this,Bomb_Black);
		img = Bomb_Black;
	}
	
	/**
	 * Used to set the number of times you want the color of the bomb to change before it explodes (200 milliseconds per blink)
	 * @param x The number of times the bomb will blink
	 */
	public void Set_Blink_Counts(int x) {
		Bomb_TickOff=x;
	}
	
	/**
	 * Used to set the time in milliseconds after the bomb is place at which it will start blinking
	 * @param x The value in milliseconds of the tickoff time
	 */
	public void Set_TickOff_Time(int x) {
		blink_count=x;
	}
	
	/**
	 * Initiates bomb countdown
	 * <p>
	 * The countdown for the bomb to start blinking, it calls the Task Start to run once
	 * </p>
	 */
	public void Start_Countdown(){
		Timer tt = new Timer();
		tt.schedule(new Start(this), Bomb_TickOff);
	}
	
	
	private class Start extends TimerTask{
		private Bomb b;
		
		/**
		 * Constructor for Start Task Class
		 * <p>
		 * Keeps the object bomb that invoked it
		 * </p>
		 * @param x - Bomb that initiated a countdown to blink
		 */
		Start(Bomb x){
			this.b=x;
		}
		
		/**
		 * Method of the Task, that runs after the given cooldown
		 * <p>
		 * It creates a periodic Task Blink that will process the blinking of the given Bomb b
		 * </p>
		 */
		public void run() {
			Timer tt = new Timer();
			tt.schedule(new Blink(b),0,200);
			counter=0;
		}
	}
	
	private class Blink extends TimerTask{
		private Bomb b;
		private boolean state=false;
		
		/**
		 * Constructor for Blink Task Class
		 * <p>
		 * Keeps the object bomb that invoked it
		 * </p>
		 * @param x - Bomb that initiated a blink sequence
		 */
		Blink(Bomb x){
			this.b=x;
		}

		/**
		 * Method of the Task, that runs after the given cooldown
		 * <p>
		 * This method increments the variable counter until the bomb has changed colors blink_count times \\
		 * It also uses a boolean state variable to swap between the 2 given colors: red and black \\
		 * After blink_count times it explodes the bomb, cancelling it's own timer and calling Explode method
		 * </p>
		 */
		public void run() {
			synchronized(m) {
				counter++;
				if(state) {
					state=!state;
					lib.Flag_For_Change(b,Bomb_Red);
				}
				else {
					state=!state;
					lib.Flag_For_Change(b,Bomb_Black);
				}
				
				if(blink_count==counter){
					this.cancel();
					Explode(b);
				}
			}
		}		
	}
	
	/**
	 * Explodes the bomb
	 * <p>
	 * Removes the Bomb b from the map and propagates an explosion starting at the position (x,y) of the bomb.
	 * </p>
	 * @param b - Bomb that is being exploded
	 */
	public void Explode(Bomb b){
		m.Remove_Element(b);
		Propagate_Explosion(b.getX(),b.getY());
	}
	
	/**
	 * Propagates The Explosion
	 * <p>
	 * Creates a new explosion of type 0 (Cross) at position (x,y) then propagates it and adds to Map m
	 * </p>
	 * @param x - Position of origin on the x axis of the map
	 * @param y - Position of origin on the y axis of the map
	 */
	private void Propagate_Explosion(int x,int y){
		Explosion Exp = new Explosion(x,y,lib,m,0,this);
		ArrayList<Element> tmp = Exp.Propagate();
		m.Add_Element_Array(tmp);
	}
}
