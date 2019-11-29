import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import java.util.Timer;
import java.util.TimerTask;


public class Bomber extends Element{
	protected boolean bomb_cd;
	protected boolean Walking_cd;
	private int cooldown = 1000; //milliseconds
	protected int move_res = 10;
	protected int progression_count;
	protected int direction;	//0-down 1-left 2-up 3-right
	Image StopDown = new Image("sprites/StopDown.png");
	Image Down1 = new Image("sprites/Down1.png");
	Image Down2 = new Image("sprites/Down2.png");
	Image StopUp = new Image("sprites/StopUp.png");
	Image Up1 = new Image("sprites/Up1.png");
	Image Up2 = new Image("sprites/Up2.png");
	Image StopLeft = new Image("sprites/StopLeft.png");
	Image Left1 = new Image("sprites/Left1.png");
	Image Left2 = new Image("sprites/Left2.png");
	Image StopRight = new Image("sprites/StopRight.png");
	Image Right1 = new Image("sprites/Right1.png");
	Image Right2 = new Image("sprites/Right2.png");
	
	
	Bomber(int x,int y) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;

		Set_Image(StopDown);

		bomb_cd=false;
		Walking_cd=false;
		direction=0;
	}
	
	public void MoveUp() {
		Coord.MoveUp();
		direction=2;
		Start_Walking();	
	}
	
	public void MoveRight() {
		Coord.MoveRight();
		direction=3;
		Start_Walking();
	}
	
	public void MoveLeft() {
		Coord.MoveLeft();
		direction=1;
		Start_Walking();
	}
	
	public void MoveDown() {
		Coord.MoveDown();
		direction=0;
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
					if(direction==0)
						Set_Image(Down1);
					if(direction==1)
						Set_Image(Left1);
					if(direction==2)
						Set_Image(Up1);
					if(direction==3)
						Set_Image(Right1);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				feet=!feet;
				try {
					if(direction==0)
						Set_Image(Down2);
					if(direction==1)
						Set_Image(Left2);
					if(direction==2)
						Set_Image(Up2);
					if(direction==3)
						Set_Image(Right2);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(progression_count==move_res){
				Walking_cd=false;
				try {
					if(direction==0)
						Set_Image(StopDown);
					if(direction==1)
						Set_Image(StopLeft);
					if(direction==2)
						Set_Image(StopUp);
					if(direction==3)
						Set_Image(StopRight);
				} catch (SlickException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.cancel();
			}
		}
	}

}
