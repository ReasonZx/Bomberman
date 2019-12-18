package GameLogic;

import java.util.Timer;
import java.util.TimerTask;
import org.newdawn.slick.SlickException;

public class Wall extends Element {
	/**
	 * 
	 */
	public static short serialVersionUID = 2;
	static protected String Wall = "sprites/Wall.png";
	static protected String Wall_Breakable = "sprites/Wall_destroyable.png";
	static protected String Rubble = "sprites/Rubble.png";
	
	Wall(int x,int y,Image_Library lib,Map m, int type) throws SlickException{
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
