import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends Element{
	Timer tt = new Timer();
<<<<<<< src/Bomb.java
	GameLogic L;
=======
>>>>>>> src/Bomb.java
	
	Bomb(int x,int y, GameLogic GL){
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		L=GL;
	}
	
	public void Start_Countdown(TimerTask t){
		tt.schedule(t,2000);
	}
	
}
