import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.SlickException;

public class Explosion extends Element{
	
	protected String Cross = 		"sprites/ExplosionCross.png";
	protected String Horizontal =	"sprites/Explosion_Horizontal.png";
	protected String Vertical = 	"sprites/Explosion_Vertical.png";
	protected String DownEnd = 		"sprites/Explosion_DownEnd.png";
	protected String UpEnd = 		"sprites/Explosion_TopEnd.png";
	protected String RightEnd = 	"sprites/Explosion_RightEnd.png";
	protected String LeftEnd = 		"sprites/Explosion_LeftEnd.png";
	private int Explosion_time = 1000;
	
	Explosion(int x,int y, GameLogic GL,int type,Bomb b) throws SlickException{
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=false;
		L=GL;
		if(type==0)
			L.lib.Flag_For_Change(this,Cross);
		if(type==1)
			L.lib.Flag_For_Change(this,Horizontal);
		if(type==2)
			L.lib.Flag_For_Change(this,Vertical);
	}

	public ArrayList<Element> Propagate() throws SlickException{
		ArrayList<Element> ret = new ArrayList<Element>();
		ret.add(this);
		
		for(int i=Coord.getX()-1;i>L.m.Get_LeftBound();i--) {
			if(L.m.Has_Destroyable_Element(i,Coord.getY())==false)
				ret.add(new Explosion(i,Coord.getY(),L,1,null));
			else
				break;
		}
		
		//L.lib.Flag_For_Change(this,LeftEnd);
		
		for(int i=Coord.getX()+1;i<L.m.Get_RightBound();i++) {
			if(L.m.Has_Destroyable_Element(i,Coord.getY())==false)
				ret.add(new Explosion(i,Coord.getY(),L,1,null));
			else
				break;
		}
		
		//L.lib.Flag_For_Change(this,RightEnd);
		
		for(int i=Coord.getY()-1;i>L.m.Get_TopBound();i--) {
			if(L.m.Has_Destroyable_Element(Coord.getX(),i)==false)
				ret.add(new Explosion(Coord.getX(),i,L,2,null));
			else
				break;
		}
		
		//L.lib.Flag_For_Change(this,UpEnd);
		
		for(int i=Coord.getY()+1;i<L.m.Get_BotBound();i++) {
			if(L.m.Has_Destroyable_Element(Coord.getX(),i)==false)
				ret.add(new Explosion(Coord.getX(),i,L,2,null));
			else
				break;
		}
		
		//L.lib.Flag_For_Change(this,DownEnd);
		
		Timer tt = new Timer();
		tt.schedule(new Remove_Explosion(ret), Explosion_time);
		
		return ret;
	}
	
	private class Remove_Explosion extends TimerTask{
		private ArrayList<Element> array;
		
		Remove_Explosion(ArrayList<Element> x){
			this.array=x;
		}
		
		public void run() {
			for(int i=0;i<array.size();i++) {
				L.m.Remove_Element(array.get(i));
			}
		}
	}
	
}
