package GameLogic;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author gomes
 *
 */
@SuppressWarnings("serial")
public class Explosion extends Element{
	
	/**
	 * 
	 */
	public static short serialVersionUID = 4;
	static protected String Cross = 		"sprites/ExplosionCross.png";
	static protected String Horizontal =	"sprites/Explosion_Horizontal.png";
	static protected String Vertical = 	"sprites/Explosion_Vertical.png";
	static protected String DownEnd = 		"sprites/Explosion_DownEnd.png";
	static protected String UpEnd = 		"sprites/Explosion_TopEnd.png";
	static protected String RightEnd = 	"sprites/Explosion_RightEnd.png";
	static protected String LeftEnd = 		"sprites/Explosion_LeftEnd.png";
	static private int Explosion_time = 500;
	
	/**
	 * Constructor for the Explosion Class
	 * <p>
	 * Creates an object explosion at the given position (x,y)
	 * It then changes the image depending on the type
	 * 0- Cross
	 * 1- Horizontal
	 * 2- Vertical
	 * <p>
	 * @param x Position on the x axis of the map
	 * @param y Position on the y axis of the map
	 * @param lib The library where Image changes are to be flagged
	 * @param m The map where the game is being played
	 * @param type The type of the wall to be placed
	 * @param b The bomb that was transformed into this explosion
	 */
	public Explosion(int x,int y,Image_Library lib,Map m,int type,Bomb b){
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=false;
		this.lib=lib;
		this.m=m;
		if(type==0)
			img=Cross;
		if(type==1)
			img=Horizontal;
		if(type==2)
			img=Vertical;
	}
	
	/**
	 * Propagates the explosion
	 * <p>
	 * This will propagate the explosion starting the position (x,y) of the object that called this method
	 * It will extend in all directions creating new explosion for every square checked until it finds a wall
	 * If it finds a wall it will then attempt to destroy it
	 * <p>
	 * @return The Array List of the explosions created
	 */
	public ArrayList<Element> Propagate(){
		ArrayList<Element> ret = new ArrayList<Element>();
		ret.add(this);
		int i;
		
		for(i=Coord.getX()-1;i>=m.Get_LeftBound();i--) {
			if(m.Has_Blocking_Element(i,Coord.getY())==false)
				ret.add(new Explosion(i,Coord.getY(),lib,m,1,null));
			else{
				if(m.Has_Destroyable_Element(i,Coord.getY())==true){
				m.GetWall(i,Coord.getY()).Destroy();
					if(i!=Coord.getX()-1)
						lib.Flag_For_Change(ret.get(ret.size()-1),LeftEnd);
				}
				break;
			}
		}
		
		if(i==m.Get_LeftBound()-1 && Coord.getX()!=m.Get_LeftBound())
			lib.Flag_For_Change(ret.get(ret.size()-1),LeftEnd);
		
		for(i=Coord.getX()+1;i<m.Get_RightBound();i++) {
			if(m.Has_Blocking_Element(i,Coord.getY())==false)
				ret.add(new Explosion(i,Coord.getY(),lib,m,1,null));
			else{
				if(m.Has_Destroyable_Element(i,Coord.getY())==true){
				m.GetWall(i,Coord.getY()).Destroy();
					if(i!=Coord.getX()+1)
						lib.Flag_For_Change(ret.get(ret.size()-1),RightEnd);
				}
				break;
			}
		}
		
		if(i==m.Get_RightBound() && Coord.getX()!=m.Get_RightBound()-1)
			lib.Flag_For_Change(ret.get(ret.size()-1),RightEnd);
		
		for(i=Coord.getY()-1;i>=m.Get_TopBound();i--) {
			if(m.Has_Blocking_Element(Coord.getX(),i)==false)
				ret.add(new Explosion(Coord.getX(),i,lib,m,2,null));
			else{
				if(m.Has_Destroyable_Element(Coord.getX(),i)==true){
					m.GetWall(Coord.getX(),i).Destroy();
					if(i!=Coord.getY()-1)
						lib.Flag_For_Change(ret.get(ret.size()-1),UpEnd);
				}
				break;
			}
		}
		
		if(i==m.Get_TopBound()-1 && Coord.getY()!=m.Get_TopBound())
			lib.Flag_For_Change(ret.get(ret.size()-1),UpEnd);
		
		for(i=Coord.getY()+1;i<m.Get_BotBound();i++) {
			if(m.Has_Blocking_Element(Coord.getX(),i)==false)
				ret.add(new Explosion(Coord.getX(),i,lib,m,2,null));
			else {
				if(m.Has_Destroyable_Element(Coord.getX(),i)==true){
					m.GetWall(Coord.getX(),i).Destroy();
					if(i!=Coord.getY()+1)
						lib.Flag_For_Change(ret.get(ret.size()-1),DownEnd);
				}
				break;
			}
		}
		
		if(i==m.Get_BotBound() && Coord.getY()!=m.Get_BotBound()-1)
			lib.Flag_For_Change(ret.get(ret.size()-1),DownEnd);
		
		Timer tt = new Timer();
		tt.schedule(new Remove_Explosion(ret), Explosion_time);
		
		return ret;
	}
	
	/**
	 * Sets the explosion Time
	 * @param x The time in milliseconds the explosion will stay on the map
	 */
	public void Set_Explosion_time(int x) {
		Explosion_time=x;
	}
	
	private class Remove_Explosion extends TimerTask{
		private ArrayList<Element> array;
		
		Remove_Explosion(ArrayList<Element> x){
			this.array=x;
		}
		
		public void run() {
			synchronized(m) {
				for(int i=0;i<array.size();i++) {
					m.Remove_Element(array.get(i));
				}
			}
		}
	}
	
}
