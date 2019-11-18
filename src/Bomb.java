import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends Element{
	Timer tt;
	TimerTask explode = new Helper();
	Map m;
	
	Bomb(int x,int y, Map m1){
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		m=m1;
		tt = new Timer();
		tt.schedule(explode,2000);
	}
	
	private class Helper extends TimerTask{
		
		public void run() {
			Explode();
		}
		
	}
	
	private void Explode() {
		m.Remove_Element(this);
	}
}
