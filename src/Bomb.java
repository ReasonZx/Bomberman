import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.SlickException;

public class Bomb extends Element{
	protected Timer tt = new Timer();
	protected String Bomb_Black = 	"sprites/normal_bomb.png";
	protected String Bomb_Red = 	"sprites/bomb_explode.png";
	protected int blink_count;

	Bomb(int x,int y, GameLogic GL) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		GL.lib.Flag_For_Change(this,Bomb_Black);
		L=GL;
		GUI_Scale=64;
		GUI_OffsetX=16;
		GUI_OffsetY=16;
	}
	
	public void Start_Countdown(){
		Timer tt = new Timer();
		tt.schedule(new Start(this), 2000);
	}
	
	private class Start extends TimerTask{
		private Bomb b;
		
		Start(Bomb x){
			this.b=x;
		}
		
		public void run() {
			Timer tt = new Timer();
			tt.schedule(new Blink(b),0,200);
			blink_count=0;
		}
	}
	private class Blink extends TimerTask{
		private Bomb b;
		private boolean state=false;
		
		Blink(Bomb x){
			this.b=x;
		}

		public void run() {
			blink_count++;
			if(state) {
				state=!state;
				L.lib.Flag_For_Change(b,Bomb_Red);
			}
			else {
				state=!state;
				L.lib.Flag_For_Change(b,Bomb_Black);
			}
			
			if(blink_count==7){
				this.cancel();
				try {
					L.Explode(b);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
}
