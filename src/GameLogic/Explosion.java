package GameLogic;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.SlickException;

public class Explosion extends Element{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8018201222621918723L;
	protected String Cross = 		"sprites/ExplosionCross.png";
	protected String Horizontal =	"sprites/Explosion_Horizontal.png";
	protected String Vertical = 	"sprites/Explosion_Vertical.png";
	protected String DownEnd = 		"sprites/Explosion_DownEnd.png";
	protected String UpEnd = 		"sprites/Explosion_TopEnd.png";
	protected String RightEnd = 	"sprites/Explosion_RightEnd.png";
	protected String LeftEnd = 		"sprites/Explosion_LeftEnd.png";
	private int Explosion_time = 1000;
	
	Explosion(int x,int y,Image_Library lib,Map m,int type,Bomb b) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=false;
		this.lib=lib;
		this.m=m;
		GUI_Scale=64;
		GUI_OffsetX=-16;
		GUI_OffsetY=-16;
		if(type==0)
			lib.Flag_For_Change(this,Cross);
		if(type==1)
			lib.Flag_For_Change(this,Horizontal);
		if(type==2)
			lib.Flag_For_Change(this,Vertical);
	}

	public ArrayList<Element> Propagate() throws SlickException{
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
