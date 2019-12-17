package GameLogic;

import java.util.Timer;
import java.util.TimerTask;
import org.newdawn.slick.SlickException;

public class Wall extends Element {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6352446170262017601L;
	protected String Wall = "sprites/Wall.png";
	protected String Wall_Breakable = "sprites/Wall_destroyable.png";
	protected String Rubble = "sprites/Rubble.png";
	
	Wall(int x,int y,Image_Library lib,Map m, int type) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		this.lib=lib;
		this.m=m;
		if(type==1) {
			Destroyable = false;
			lib.Flag_For_Change(this, Wall);
		}
		if(type==2) {
			Destroyable = true;
			lib.Flag_For_Change(this,Wall_Breakable);
		}
		GUI_Scale=64;
		GUI_OffsetX=0;
		GUI_OffsetY=0;
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
