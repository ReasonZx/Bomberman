import org.newdawn.slick.SlickException;
import java.util.Timer;
import java.util.TimerTask;


public class Bomber extends Element{
	protected boolean bomb_cd;
	protected boolean Walking_cd;
	private int cooldown = 500; //milliseconds
	protected int move_res = 5;
	protected int progression_count=5;
	protected int direction;	//0-down 1-left 2-up 3-right
	protected String StopDown = "sprites/Down.png";
	protected String Down1 = 	"sprites/Down1.png";
	protected String Down2 = 	"sprites/Down2.png";
	protected String StopUp =	"sprites/Up.png";
	protected String Up1 = 		"sprites/Up1.png";
	protected String Up2 = 		"sprites/Up2.png";
	protected String StopLeft = "sprites/Left.png";
	protected String Left1 = 	"sprites/Left1.png";
	protected String Left2 = 	"sprites/Left2.png";
	protected String StopRight ="sprites/Right.png";
	protected String Right1 =	"sprites/Right1.png";
	protected String Right2 = 	"sprites/Right2.png";
	
	
	Bomber(int x,int y,GameLogic GL) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		L=GL;
		
		L.lib.Flag_For_Change(this,StopDown);

		bomb_cd=false;
		Walking_cd=false;
		//direction=0;
		GUI_Scale=64;
		GUI_OffsetX=-16;
		GUI_OffsetY=-16;
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
		tt.schedule(new Walking(this),0,cooldown/move_res);
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
		private Bomber person;
		Walking (Bomber person){
			this.person=person;
			bomb_cd=true;
		}
		
		public void run(){
			progression_count++;
			
			if(feet) {
				if(direction==0)
					L.lib.Flag_For_Change(person,Down1);
				if(direction==1)
					L.lib.Flag_For_Change(person,Left1);
				if(direction==2)
					L.lib.Flag_For_Change(person,Up1);
				if(direction==3)
					L.lib.Flag_For_Change(person,Right1);
			}
			else {
				if(direction==0)
					L.lib.Flag_For_Change(person,Down2);
				if(direction==1)
					L.lib.Flag_For_Change(person,Left2);
				if(direction==2)
					L.lib.Flag_For_Change(person,Up2);
				if(direction==3)
					L.lib.Flag_For_Change(person,Right2);
			}
			
			feet=!feet;
			
			if(progression_count==move_res){
				Walking_cd=false;
				if(direction==0)
					L.lib.Flag_For_Change(person,StopDown);
				if(direction==1)
					L.lib.Flag_For_Change(person,StopLeft);
				if(direction==2)
					L.lib.Flag_For_Change(person,StopUp);
				if(direction==3)
					L.lib.Flag_For_Change(person,StopRight);
				bomb_cd=false;
				this.cancel();
			}
		}
	}
	
	public int Get_OffsetX() {
		if(direction==0)
			return -16;
		if(direction==1)
			return (move_res-progression_count-1)*16;
		if(direction==2)
			return -16;
		if(direction==3)
			return -(move_res-progression_count+1)*16;
		return -16;
	}
	
	public int Get_OffsetY() {
		if(direction==0)
			return -(move_res-progression_count+1)*16;
		if(direction==1)
			return -16;
		if(direction==2)
			return (move_res-progression_count-1)*16;
		if(direction==3)
			return -16;
		return -16;
	}

}
