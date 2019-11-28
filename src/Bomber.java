import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import java.util.Timer;
import java.util.TimerTask;


public class Bomber extends Element{
	protected boolean bomb_cd;
	protected boolean Walking_cd;
	private int cooldown = 1000; //milliseconds
	protected int move_res = 20;
	protected int progression_count;
	Image StopFw = new Image("sprites/parado.png");
	Image WalkFw1 = new Image("sprites/andar1.png");
	Image WalkFw2 = new Image("sprites/andar2.png");
	
	
	Bomber(int x,int y) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;

		Set_Image(StopFw);

		bomb_cd=false;
		Walking_cd=false;
	}
	
	public void MoveUp() {
		Coord.MoveUp();
		Start_Walking();
	}
	
	public void MoveRight() {
		Coord.MoveRight();
		Start_Walking();
	}
	
	public void MoveLeft() {
		Coord.MoveLeft();
		Start_Walking();
	}
	
	public void MoveDown() {
		Coord.MoveDown();
		Start_Walking();
	}
	
	private void Start_Walking(){
		Walking_cd=true;
		Timer tt = new Timer();
		tt.schedule(new Walking(),0,cooldown/move_res);
		progression_count=0;
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
	
	public int Get_Progression_Count() {
		return progression_count;
	}
	
	private class Walking extends TimerTask{
		private boolean feet=false;
		
		public void run(){
			progression_count++;
			
			if(feet) {
				feet=!feet;
				try {
					Set_Image(WalkFw1);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				feet=!feet;
				try {
					Set_Image(WalkFw2);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(progression_count==move_res){
				Walking_cd=false;
				this.cancel();
			}
		}
	}

}
