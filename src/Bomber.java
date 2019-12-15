import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import java.util.Timer;
import java.util.TimerTask;


public class Bomber extends Element{
	protected boolean bomb_cd;
	protected boolean Walking_cd;
	private int cooldown = 500; //milliseconds
	private int bomb_cooldown=1000;
	protected int move_res = 5;
	protected int progression_count=move_res;
	protected int direction;	//0-down 1-left 2-up 3-right
	protected String StopDown="StopDown";
	protected String Down1="Down1";
	protected String Down2="Down2";
	protected String StopUp="StopUp";
	protected String Up1="Up1";
	protected String Up2="Up2";
	protected String StopLeft="StopLeft";
	protected String Left1="Left1";
	protected String Left2="Left2";
	protected String StopRight="StopRight";
	protected String Right1="Right1";
	protected String Right2="Right2";
	protected int upkey,downkey,rightkey,leftkey,actionkey;
	private int player;
	
	
	Bomber(int x,int y,GameLogic GL,int c1,int c2,int k1,int k2, int k3, int k4,int k5,int player) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		L=GL;
		initialize_images(c1,c2);
		L.lib.Flag_For_Change(this,StopDown);
		
		upkey=k1;
		downkey=k2;
		leftkey=k3;
		rightkey=k4;
		actionkey=k5;
		this.player=player;
		img = new Image("sprites/D_"   + c1 + c2 + ".png");
		bomb_cd=false;
		Walking_cd=false;
		//direction=0;
		GUI_Scale=64;
		GUI_OffsetX=img.getWidth()/2;
		GUI_OffsetY=img.getHeight()/2;
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
		tt.schedule(new Bomb_Cd(), bomb_cooldown);
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
			return GUI_OffsetX;
		if(direction==1)
			return (move_res-progression_count)*GUI_Scale/(move_res+1)+GUI_OffsetX;
		if(direction==2)
			return GUI_OffsetX;
		if(direction==3)
			return -(move_res-progression_count)*GUI_Scale/(move_res+1)+GUI_OffsetX;
		return GUI_OffsetX;
	}
	
	public int Get_OffsetY() {
		if(direction==0)
			return -(move_res-progression_count)*GUI_Scale/(move_res+1)+GUI_OffsetY;
		if(direction==1)
			return GUI_OffsetY;
		if(direction==2)
			return (move_res-progression_count)*GUI_Scale/(move_res+1)+GUI_OffsetY;
		if(direction==3)
			return GUI_OffsetY;
		return GUI_OffsetY;
	}
	
	public int Get_MoveUp_Key() {
		return upkey;
	}
	
	public int Get_MoveDown_Key() {
		return downkey;
	}
	
	public int Get_MoveLeft_Key() {
		return leftkey;
	}
	
	public int Get_MoveRight_Key() {
		return rightkey;
	}
	
	public int Get_Action_Key() {
		return actionkey;
	}
	
	public void Set_Walking_Speed(int x) {
		cooldown=x;
	}
	
	public void Set_Moving_Resolution(int x) {
		move_res=x;
		progression_count=move_res;
	}
	
	public void Set_Bomb_Cooldown(int x) {
		bomb_cooldown=x;
	}
	
	private void initialize_images(int c1,int c2) {
		L.lib.Player_Sprites_Initialize("sprites/D_"   + c1 + c2  + ".png","sprites/D1_" + c1 + c2 + ".png");
	}
	
	public int Get_Player() {
		return player;
	}
}
