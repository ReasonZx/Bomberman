import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Bomb extends Element{
	Timer tt = new Timer();

	Bomb(int x,int y, GameLogic GL) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		L=GL;
		img = new Image("sprites/normal_bomb.png");

	}
	
	public void Start_Countdown(TimerTask t){
		tt.schedule(t,2000);
	}
	
}
