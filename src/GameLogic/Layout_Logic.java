package GameLogic;

/**
 * @author João Gomes up201506251@fe.up.pt
 */
public class Layout_Logic {
	Image_Library lib;
	
	/**
	 * Constructor for Layout Logic
	 * <p>
	 * Used to initiate a logic creator for map making
	 * <p>
	 * @param lib The library where Image changes are to be flagged
	 */
	public Layout_Logic(Image_Library lib){
		this.lib=lib;
	}
	
	/**
	 * Creates a Test Map with all walls as destroyable
	 * @return The Map generated
	 */
	public Map Generate_Test_Map(){
		Map m = new Map(0,12,0,9);
		
		for(int i=m.Get_LeftBound();i<m.Get_RightBound();i++)
			for(int j=m.Get_TopBound();j<m.Get_BotBound();j++) {
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
				m.Add_Element(new Wall(i,j,lib,m,2));
			}
		lib.m=m;
		return m;
	}
	
	/**
	 * Creates the playing map using a module generation pattern
	 * @return The generated map
	 */
	public Map Generate_Standard_Map(){
		Map m = new Map(0,12,0,9);
		for(int i=m.Get_LeftBound();i<m.Get_RightBound();i++)
			for(int j=m.Get_TopBound();j<m.Get_BotBound();j++) {
				if(i==m.Get_LeftBound() && j==m.Get_TopBound())
					continue;
				if(i==m.Get_LeftBound() && j==m.Get_TopBound()+1)
					continue;
				if(i==m.Get_LeftBound()+1 && j==m.Get_TopBound())
					continue;
				
				if(i==m.Get_RightBound()-1 && j==m.Get_BotBound()-1)
					continue;
				if(i==m.Get_RightBound()-2 && j==m.Get_BotBound()-1)
					continue;
				if(i==m.Get_RightBound()-1 && j==m.Get_BotBound()-2)
					continue;
				
				if(i==m.Get_LeftBound() && j==m.Get_BotBound()-1)
					continue;
				if(i==m.Get_LeftBound() && j==m.Get_BotBound()-2)
					continue;
				if(i==m.Get_LeftBound()+1 && j==m.Get_BotBound()-1)
					continue;
				
				if(i==m.Get_RightBound()-1 && j==m.Get_TopBound())
					continue;
				if(i==m.Get_RightBound()-2 && j==m.Get_TopBound())
					continue;
				if(i==m.Get_RightBound()-1 && j==m.Get_TopBound()+1)
					continue;
				
				if((i*j)%3==0) if((i+j)%2==0) m.Add_Element(new Wall(i,j,lib,m,1));
					else m.Add_Element(new Wall(i,j,lib,m,2));
				else m.Add_Element(new Wall(i,j,lib,m,2));
			}
		lib.m=m;
		return m;
	}
}
