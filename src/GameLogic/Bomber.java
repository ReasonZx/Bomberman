package GameLogic;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;


@SuppressWarnings("serial")
/**
 * @author João Gomes up201506251@fe.up.pt
 */
public class Bomber extends Element implements Serializable{
	/**
	 * 
	 */
	public static short serialVersionUID = 1;
	transient protected boolean bomb_cd;
	transient protected boolean Walking_cd;
	
	/**
	 * The cooldown of walking action, it blocks any possible movement for this ammount of milliseconds
	 */
	static private int cooldown = 500; //milliseconds
	
	/**
	 * The cooldown of bomb placing action, blocking that action for this ammount of milliseconds
	 */
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
	static protected String Blood="sprites/blood.png";
    protected int upkey,downkey,rightkey,leftkey,actionkey;
    transient private int player;
    private boolean dead=false;
    /**
	 * Constructor for Bomber Class
	 * <p>
	 * The constructor creates a bomber (player character) for the give map in the (x,y) position.
	 * It also saves internal values for the colors and keys passed as parameters.
	 * Primary color affects shirt and shoes while secondary color affects hat and pants.
	 * </p>
	 * @param x Position on the x axis of the map
	 * @param y Position on the y axis of the map
	 * @param lib The library where Image changes are to be flagged
	 * @param m The map where the game is being played
	 * @param c1 The primary color of the Bomber style
	 * @param c2 The secondary color of the Bomber style
	 * @param k1 The Up movement key value
	 * @param k2 The Down movement key value
	 * @param k3 The Left movement key value
	 * @param k4 The Right movement key value
	 * @param k5 The action key value, used to place bombs
	 * @param p The number of the player being added
	 */
	public Bomber(int x,int y,Image_Library lib,Map m,int c1,int c2,int k1,int k2, int k3, int k4,int k5,int p){
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
	
	/**
	 * Constructor for Bomber Class
	 * <p>
	 * This constructor is used by the server to replicate the de-serialised Bomber object that it receives, using a new Image Library and map objects.
	 * </p>
	 * @param x Bomber to be replicated
	 * @param lib The library where Image changes are to be flagged
	 * @param m The map where the game is being played
	 */
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
	
	/**
	 * Move Bomber to the square above
	 * <p>
	 * Changes Y position by -1
	 * Then starts a walking animation
	 * </p>
	 */
	public void MoveUp() {
		Coord.MoveUp();
		direction=2;
		Start_Walking();	
	}
	
	/**
	 * Move Bomber to the square to his right
	 * <p>
	 * Changes X position by +1
	 * Then starts a walking animation
	 * </p>
	 */
	public void MoveRight() {
		Coord.MoveRight();
		direction=3;
		Start_Walking();
	}
	
	/**
	 * Move Bomber to the square to his left
	 * <p>
	 * Changes X position by -1
	 * Then starts a walking animation
	 * </p>
	 */
	public void MoveLeft() {
		Coord.MoveLeft();
		direction=1;
		Start_Walking();
	}
	
	/**
	 * Move Bomber to the square below
	 * <p>
	 * Changes Y position by +1
	 * Then starts a walking animation
	 * </p>
	 */
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

	/**
	 * Puts bomb placing skill on cooldown
	 * <p>
	 * Method to be called after creating a bomb to put the ability to place bombs for the said player on a cooldown
	 * </p>
	 */
	public void Used_Bomb(){
		bomb_cd=true;
		Timer tt = new Timer();
		tt.schedule(new Bomb_Cd(), bomb_cooldown);
	}
	
	/**
	 * Indicates if Bomber can place a bomb
	 * @return The negated value of the place bomb cooldown
	 */
	public boolean Can_Use_Bomb(){
		return !bomb_cd;
	}
	
	/**
	 * Indicates if Bomber is able to walk
	 * @return The negated value of the walking cooldown
	 */
	public boolean Can_Walk() {
		return !Walking_cd;
	}
	
	/**
	 * Checks if bomber died
	 * <p>
	 * Indicates if Bomber is inside a square with an explosion
	 * Transforms the Bomber into a pool of blood
	 * <p>
	 * @return If the position (x,y) has an explosion
	 */
	public boolean Death_Check(){
		if (m.Has_Explosion(Coord.getX(),Coord.getY())){
			lib.Flag_For_Change(this, Blood);
			Solid=false;
			Walking_cd=true;
			bomb_cd=true;
			dead=true;
			return true;
		}
		
		return false;
	}
	
	private class Bomb_Cd extends TimerTask{
		
		public void run(){
			synchronized(m) {
				bomb_cd=false;
			}
		}
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
				if(dead) {
					this.cancel();
					return;
				}
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
	
	/**
	 * Get the progression count of the movement animation in the X axis
	 * <p>
	 * This method returns the current value of the offset on the X axis
	 * This variable indicates the progression of the movement animation that was initiated.
	 * At the start -progression_count/(move_res+1)
	 * At the end 0
	 * When drawing the Bomber you can add this as an offset to make an animation and 
	 * then have in consideration the scale and distances of the images you're drawing
	 * <p>
	 * @return Returns the value of the offsetX
	 */
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
	
	/**
	 * Get the progression count of the movement animation in the Y axis
	 * <p>
	 * This method returns the current value of the offset on the Y axis
	 * This variable indicates the progression of the movement animation that was initiated.
	 * At the start -progression_count/(move_res+1)
	 * At the end 0
	 * When drawing the Bomber you can add this as an offset to make an animation and 
	 * then have in consideration the scale and distances of the images you're drawing
	 * <p>
	 * @return Returns the value of the offsetY
	 */
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
	
	/**
	 * Get the Key used by the Bomber to move up
	 * @return The integer value of the Move Up key
	 */
	public int Get_MoveUp_Key() {
		return upkey;
	}
	
	/**
	 * Get the Key used by the Bomber to move down
	 * @return The integer value of the Move Down key
	 */
	public int Get_MoveDown_Key() {
		return downkey;
	}
	
	/**
	 * Get the Key used by the Bomber to move left
	 * @return The integer value of the Move Left key
	 */
	public int Get_MoveLeft_Key() {
		return leftkey;
	}
	
	/**
	 * Get the Key used by the Bomber to move right
	 * @return The integer value of the Move Right key
	 */
	public int Get_MoveRight_Key() {
		return rightkey;
	}
	
	/**
	 * Get the Key used by the Bomber to place bombs
	 * @return The integer value of the Action key
	 */
	public int Get_Action_Key() {
		return actionkey;
	}
	
	/**
	 * Set the cooldown of the walking ability
	 * @param x The ammount of milliseconds until the Bomber can walk again (Time it takes to end walking animation)
	 */
	static public void Set_Walking_Speed(int x) {
		cooldown=x;
	}
	
	/**
	 * Set the resolution of the movement animation
	 * @param x The number of steps the Bomber will take going from one square to another
	 */
	public void Set_Moving_Resolution(int x) {
		move_res=x;
		progression_count=move_res;
	}
	
	/**
	 * Set the cooldown of the place bomb ability
	 * @param x The ammount of milliseconds until the Bomber can place a bomb again
	 */
	static public void Set_Bomb_Cooldown(int x) {
		bomb_cooldown=x;
	}
	
	/**
	 * Get the player number
	 * @return The number of the player
	 */
	public int Get_Player() {
		return player;
	}
	
	/**
	 * Serialises data into a string format to be sent over the object socket of the server
	 */
	public String toString() {
		return Long.toString(serialVersionUID)+","+Integer.toString(getX())+","+Integer.toString(getY())+","+img+","
					+Float.toString(Get_OffsetX())+","
					+Float.toString(Get_OffsetY())+","+Integer.toString(color1)+","+Integer.toString(color2);
	}

	/**
	 * Sets the number of the player
	 * @param x The number of the player
	 */
	public void Set_Player(int x) {
		// TODO Auto-generated method stub
		player=x;
	}
}
