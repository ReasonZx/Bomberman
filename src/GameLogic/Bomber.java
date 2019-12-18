package GameLogic;
import org.newdawn.slick.SlickException;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;


public class Bomber extends Element implements Serializable{
	/**
	 * 
	 */
	public static short serialVersionUID = 1;
	transient protected boolean bomb_cd;
	transient protected boolean Walking_cd;
	static private int cooldown = 500; //milliseconds
	static private int bomb_cooldown=1000;
	static protected int move_res = 5;
	protected int progression_count=move_res;
	transient protected int direction;	//0-down 1-left 2-up 3-right
	private int color1,color2;
	static protected String StopDown="StopDown";
	static protected String Down1="Down1";
	static protected String Down2="Down2";
	static protected String StopUp="StopUp";
	static protected String Up1="Up1";
	static protected String Up2="Up2";
	static protected String StopLeft="StopLeft";
	static protected String Left1="Left1";
	static protected String Left2="Left2";
	static protected String StopRight="StopRight";
	static protected String Right1="Right1";
	static protected String Right2="Right2";
    protected int upkey,downkey,rightkey,leftkey,actionkey;
    transient private int player;
	
	
	public Bomber(int x,int y,Image_Library lib,Map m,int c1,int c2,int k1,int k2, int k3, int k4,int k5,int p) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=true;
		this.lib=lib;
		this.m=m;
		
		img=StopDown;
		color1=c1;
		color2=c2;
		upkey=k1;
		downkey=k2;
		leftkey=k3;
		rightkey=k4;
		actionkey=k5;
		player=p;
		img = StopDown;
		bomb_cd=false;
		Walking_cd=false;
		//direction=0;
		GUI_Scale=64;
	}

	public Bomber(Bomber x, Image_Library lib, Map m) {
		Coordinate tmp = new Coordinate(x.getX(),x.getY());
		Coord=tmp;
		Solid=true;
		this.lib=lib;
		this.m=m;
		
		img=StopDown;
		color1=x.color1;
		color2=x.color2;
		upkey=x.upkey;
		downkey=x.downkey;
		leftkey=x.leftkey;
		rightkey=x.rightkey;
		actionkey=x.actionkey;
		player=x.player;
		bomb_cd=false;
		Walking_cd=false;
		//direction=0;
		GUI_Scale=64;
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
		return m.Has_Explosion(Coord.getX(),Coord.getY());
	}
	
	private class Bomb_Cd extends TimerTask{
		
		public void run(){
			synchronized(m) {
				bomb_cd=false;
			}
		}
	}
	
	public int Get_Progression_Count() {
		return progression_count;
	}
	
	private class Walking extends TimerTask{
		transient private boolean feet=false;
		private Bomber person;
		Walking (Bomber person){
			this.person=person;
			bomb_cd=true;
		}
		
		public void run(){
				progression_count++;
				
				if(feet) {
					if(direction==0)
						lib.Flag_For_Change(person,Down1);
					if(direction==1)
						lib.Flag_For_Change(person,Left1);
					if(direction==2)
						lib.Flag_For_Change(person,Up1);
					if(direction==3)
						lib.Flag_For_Change(person,Right1);
				}
				else {
					if(direction==0)
						lib.Flag_For_Change(person,Down2);
					if(direction==1)
						lib.Flag_For_Change(person,Left2);
					if(direction==2)
						lib.Flag_For_Change(person,Up2);
					if(direction==3)
						lib.Flag_For_Change(person,Right2);
				}
				
				feet=!feet;
				
				if(progression_count==move_res){
					Walking_cd=false;
					if(direction==0)
						lib.Flag_For_Change(person,StopDown);
					if(direction==1)
						lib.Flag_For_Change(person,StopLeft);
					if(direction==2)
						lib.Flag_For_Change(person,StopUp);
					if(direction==3)
						lib.Flag_For_Change(person,StopRight);
					bomb_cd=false;
					this.cancel();
				}
			
		}
	}
	
	public float Get_OffsetX() {
		if(direction==0)
			return 0;
		if(direction==1)
			return (float)(move_res-progression_count)/(move_res+1);
		if(direction==2)
			return 0;
		if(direction==3)
			return -(float)(move_res-progression_count)/(move_res+1);
		return 0;
	}
	
	public float Get_OffsetY() {
		if(direction==0)
			return -(float)(move_res-progression_count)/(move_res+1);
		if(direction==1)
			return 0;
		if(direction==2)
			return (float)(move_res-progression_count)/(move_res+1);
		if(direction==3)
			return 0;
		return 0;
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
	
	static public void Set_Walking_Speed(int x) {
		cooldown=x;
	}
	
	public void Set_Moving_Resolution(int x) {
		move_res=x;
		progression_count=move_res;
	}
	
	static public void Set_Bomb_Cooldown(int x) {
		bomb_cooldown=x;
	}
	
	public int Get_Player() {
		return player;
	}
	
	public String toString() {
		return Long.toString(serialVersionUID)+","+Integer.toString(getX())+","+Integer.toString(getY())+","+img+","
					+Float.toString(Get_OffsetX())+","
					+Float.toString(Get_OffsetY())+","+Integer.toString(color1)+","+Integer.toString(color2);
	}

	public void Set_Player(int x) {
		// TODO Auto-generated method stub
		player=x;
	}
}
