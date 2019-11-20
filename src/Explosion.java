import java.util.ArrayList;

public class Explosion extends Element{
	GameLogic L;
	
	Explosion(int x,int y, GameLogic GL){
		Coordinate tmp = new Coordinate(x,y);
		Coord=tmp;
		Solid=false;
		L=GL;
	}

	public ArrayList<Element> Propagate(){
		ArrayList<Element> ret = new ArrayList<Element>();
		ret.add(this);
		
		for(int i=Coord.getX();i>L.m.Get_LeftBound();i--) {
			if(L.m.Has_Destroyable_Element(i,Coord.getY())==false)
				ret.add(new Explosion(i,Coord.getY(),L));
			else
				break;
		}
		
		for(int i=Coord.getX();i<L.m.Get_RightBound();i++) {
			if(L.m.Has_Destroyable_Element(i,Coord.getY())==false)
				ret.add(new Explosion(i,Coord.getY(),L));
			else
				break;
		}
		
		for(int i=Coord.getY();i>L.m.Get_TopBound();i--) {
			if(L.m.Has_Destroyable_Element(Coord.getX(),i)==false)
				ret.add(new Explosion(Coord.getX(),i,L));
			else
				break;
		}
		
		for(int i=Coord.getY();i<L.m.Get_BotBound();i++) {
			if(L.m.Has_Destroyable_Element(Coord.getX(),i)==false)
				ret.add(new Explosion(Coord.getX(),i,L));
			else
				break;
		}
		
		return ret;
	}
	
}
