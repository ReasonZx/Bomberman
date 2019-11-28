<<<<<<< src/Bomber.java
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
=======
import java.util.Timer;
import java.util.TimerTask;
>>>>>>> src/Bomber.java

public class Bomber extends Element{
	protected boolean bomb_cd;
	
	Bomber(int x,int y) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
<<<<<<< src/Bomber.java
		img = new Image("sprites/parado.png");
=======
		bomb_cd=false;
>>>>>>> src/Bomber.java
	}
	
	public void MoveUp() {
		Coord.MoveUp();
	}
	
	public void MoveRight() {
		Coord.MoveRight();
	}
	
	public void MoveLeft() {
		Coord.MoveLeft();
	}
	
	public void MoveDown() {
		Coord.MoveDown();
	}
	
<<<<<<< src/Bomber.java
	
=======
	public void Used_Bomb(){
		bomb_cd=true;
		Timer tt = new Timer();
		tt.schedule(new Bomb_Cd(), 1000);
	}
	
	public boolean Can_Use_Bomb(){
		return !bomb_cd;
	}
	
	public boolean Death_Check(){
		return L.m.Has_Explosion(Coord.getX(),Coord.getY());
	}
	
	private class Bomb_Cd extends TimerTask{
		
		public void run(){
			bomb_cd=false;
		}
	}
>>>>>>> src/Bomber.java
}
