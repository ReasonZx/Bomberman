package GameLogic;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.SlickException;

public class Bomb extends Element{
	/**
	 * 
	 */
	private static final long serialVersionUID = -537587995411283071L;
	transient protected Timer tt = new Timer();
	protected String Bomb_Black = 	"sprites/normal_bomb.png";
	protected String Bomb_Red = 	"sprites/bomb_explode.png";
	protected int blink_count=6,counter;
	
	Bomb(int x,int y,Image_Library lib,Map m) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		this.lib=lib;
		this.m=m;
		lib.Flag_For_Change(this,Bomb_Black);
		img = Bomb_Black;

		GUI_Scale=64;
		GUI_OffsetX=0;
		GUI_OffsetY=0;
	}
	
	public void Set_Blink_Counts(int x) {
		blink_count=x;
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
			counter=0;
		}
	}
	
	private class Blink extends TimerTask{
		private Bomb b;
		private boolean state=false;
		
		Blink(Bomb x){
			this.b=x;
		}

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
					try {
						Explode(b);
					} catch (SlickException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}		
	}
	
	public void Explode(Bomb b) throws SlickException{
		m.Remove_Element(b);
		Propagate_Explosion(b.getX(),b.getY());
	}
	
	private void Propagate_Explosion(int x,int y) throws SlickException {
		Explosion Exp = new Explosion(x,y,lib,m,0,this);
		ArrayList<Element> tmp = Exp.Propagate();
		m.Add_Element_Array(tmp);
	}
}
