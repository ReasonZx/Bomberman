import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bomb extends Element{
	protected Timer tt = new Timer();
	protected Image Bomb_Black = new Image("sprites/normal_bomb.png");
	protected Image Bomb_Red = new Image("sprites/bomb_explode.png");
	protected int blink_count;

	Bomb(int x,int y, GameLogic GL) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		Set_Image(Bomb_Black);
		L=GL;
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
				try {
					Set_Image(Bomb_Red);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				state=!state;
				try {
					Set_Image(Bomb_Black);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(blink_count==7){
				this.cancel();
				L.Explode(b);
			}
		}		
	}
}
