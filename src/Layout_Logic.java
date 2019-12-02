import org.newdawn.slick.SlickException;

public class Layout_Logic {
	private GameLogic L;
	
	Layout_Logic(GameLogic GL){
		L=GL;
	}
	
	public void Generate_Standard_Map() throws SlickException{
		L.m = new Map(0,12,0,9);
		
		for(int i=L.m.Get_LeftBound();i<L.m.Get_RightBound();i++)
			for(int j=L.m.Get_TopBound();j<L.m.Get_BotBound();j++) {
				if(i==1 && j==1)
					continue;
				if(i==1 && j==2)
					continue;
				if(i==2 && j==1)
					continue;
				if(i==11 && j==8)
					continue;
				if(i==10 && j==8)
					continue;
				if(i==11 && j==7)
					continue;
				L.m.Add_Element(new Wall(i,j,L));
			}
	}
}
