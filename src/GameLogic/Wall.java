package GameLogic;
import java.util.Timer;
import java.util.TimerTask;
import org.newdawn.slick.SlickException;

public class Wall extends Element {
	protected String Wall = "sprites/Wall.png";
	protected String Wall_Breakable = "sprites/Wall_destroyable.png";
	protected String Rubble = "sprites/Rubble.png";
	
	Wall(int x,int y,GameLogic L, int type) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		this.L=L;
		if(type==1) {
			Destroyable = false;
			L.lib.Flag_For_Change(this, Wall);
		}
		if(type==2) {
			Destroyable = true;
			L.lib.Flag_For_Change(this,Wall_Breakable);
		}
		GUI_Scale=64;
		GUI_OffsetX=0;
		GUI_OffsetY=0;
	}
	
	public void Destroy() {
		L.lib.Flag_For_Change(this,Rubble);
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
			L.m.Remove_Element(x);
		}
		
	}
}
