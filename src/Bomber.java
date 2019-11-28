import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import java.util.Timer;
import java.util.TimerTask;


public class Bomber extends Element{
	protected boolean bomb_cd;
	protected boolean Walking_cd;
	int cooldown = 200; //milliseconds
	
	Bomber(int x,int y) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;

		img = new Image("sprites/parado.png");

		bomb_cd=false;
		Walking_cd=false;
	}
	
	public void MoveUp() {
		Coord.MoveUp();
		Walking_cd=true;
		Timer tt = new Timer();
		tt.schedule(new Walk_Cd(), cooldown);
	}
	
	public void MoveRight() {
		Coord.MoveRight();
		Walking_cd=true;
		Timer tt = new Timer();
		tt.schedule(new Walk_Cd(), cooldown);
	}
	
	public void MoveLeft() {
		Coord.MoveLeft();
		Walking_cd=true;
		Timer tt = new Timer();
		tt.schedule(new Walk_Cd(), cooldown);
	}
	
	public void MoveDown() {
		Coord.MoveDown();
		Walking_cd=true;
		Timer tt = new Timer();
		tt.schedule(new Walk_Cd(), cooldown);
	}

	public void Used_Bomb(){
		bomb_cd=true;
		Timer tt = new Timer();
		tt.schedule(new Bomb_Cd(), 1000);
	}
	
	public boolean Can_Use_Bomb(){
		return !bomb_cd;
	}
	
	public boolean Can_Walk() {
		return !Walking_cd;
	}
	
	public boolean Death_Check(){
		return L.m.Has_Explosion(Coord.getX(),Coord.getY());
	}
	
	private class Bomb_Cd extends TimerTask{
		
		public void run(){
			bomb_cd=false;
		}
	}
	
	private class Walk_Cd extends TimerTask{
		
		public void run(){
			Walking_cd=false;
		}
	}

}
