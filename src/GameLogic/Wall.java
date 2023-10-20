package GameLogic;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
/**
 * @author João Gomes up201506251@fe.up.pt
 */
public class Wall extends Element {
	/**
	 * 
	 */
	public static short serialVersionUID = 2;
	static protected String Wall = "sprites/Wall.png";
	static protected String Wall_Breakable = "sprites/Wall_destroyable.png";
	static protected String Rubble = "sprites/Rubble.png";
	
	/**
	 * Constructor of the Wall Class
	 * <p>
	 * This creates a new wall at the given position (x,y) on the Map m
	 * It then sets the type of the new wall
	 * 1 - Not Destroyable
	 * 2- Destroyable
	 * <p>
	 * @param x Position on the x axis of the map
	 * @param y Position on the y axis of the map
	 * @param lib The library where Image changes are to be flagged
	 * @param m The map where the game is being played
	 * @param type The type of the wall to be placed
	 */
	public Wall(int x,int y,Image_Library lib,Map m, int type){
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		this.lib=lib;
		this.m=m;
		if(type==1) {
			Destroyable = false;
			img=Wall;
		}
		if(type==2) {
			Destroyable = true;
			img=Wall_Breakable;
		}
	}
	
	/**
	 * Destroys this wall removing it from the map and placing rubble instead
	 */
	public void Destroy() {
		lib.Flag_For_Change(this,Rubble);
		Solid=false;
		Destroyable=false;
		Timer tt = new Timer();
		tt.schedule(new Fade(this), 3000);
	}
	
	private class Fade extends TimerTask{
		Element x;
		
		Fade(Element x){
			this.x=x;
		}
		@Override
		public void run() {
			m.Remove_Element(x);
		}
		
	}
}
