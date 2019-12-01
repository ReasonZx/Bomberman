import java.util.ArrayList;

public class Map {
	private ArrayList<ArrayList<ArrayList<Element>>> elements;
	int LeftBound, RightBound, TopBound, BotBound;
	
	Map(int LB,int RB,int TB,int BB){
		LeftBound=LB;
		RightBound=RB;
		TopBound=TB;
		BotBound=BB;
		
		Initialize_Space();
	}
	
	public int Add_Element(Element cha) {
		
		if(Out_Of_Bounds(cha.getX(),cha.getY())==true) //|| ((cha.Is_Solid()==true) && Has_Solid_Element(cha.getX(),cha.getY())))
			return 0;
		
		Get_List_Elements(cha.getX(),cha.getY()).add(cha);
		
		return 1;
	}
	
	public int Remove_Element(Element cha) {
		if(cha==null)
			return 0;
		if(Has_Element(cha.getX(),cha.getY())==false)
			return 0;
		
		for(int i=0;i<Get_List_Elements(cha.getX(),cha.getY()).size();i++)
			if(Get_List_Elements(cha.getX(),cha.getY()).get(i).getClass() == cha.getClass() ) {
				Get_List_Elements(cha.getX(),cha.getY()).remove(i);
				return 1;
			}
		
		return 1;
	}
	
	public void Add_Element_Array(ArrayList<Element> x) {
		for(int i=0;i<x.size();i++)
			this.Add_Element(x.get(i));
	}
	
	public boolean Has_Element(int x,int y) {
		if(Get_List_Elements(x,y).size()==0)
			return false;
		else
			return true;
	}
	
	public boolean Has_Solid_Element(int x,int y) {
	
		for(int i=0;i<Get_List_Elements(x,y).size();i++) {
			if(Get_List_Elements(x,y).get(i).Is_Solid()==true)
				return true;
		}
		return false;
	}
	
	public boolean Has_Destroyable_Element(int x,int y) {
		for(int i=0;i<Get_List_Elements(x,y).size();i++) {
			if(Get_List_Elements(x,y).get(i).Is_Destroyable()==true)
				return true;
		}
		return false;
	}
	
	public boolean Out_Of_Bounds(int x,int y) {
		if(x<=LeftBound || x>=RightBound || y<=TopBound || y>=BotBound)
			return true;
		else
			return false;
	}
	
	public boolean Has_Bomb(int x,int y) {
		if(Has_Element(x,y)==true) {
			for(int i=0;i<Get_List_Elements(x,y).size();i++) {
				if(Get_List_Elements(x,y).get(i) instanceof Bomb) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean Has_Explosion(int x,int y) {
		if(Has_Element(x,y)==true) {
			for(int i=0;i<Get_List_Elements(x,y).size();i++) {
				if(Get_List_Elements(x,y).get(i) instanceof Explosion) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void Initialize_Space() {
		elements = new ArrayList<ArrayList<ArrayList<Element>>>(RightBound-1);
		
		for (int i = 0; i < RightBound; i++) {
			elements.add(new ArrayList<ArrayList<Element>>(BotBound));
		    for (int j = 0; j < BotBound; j++) {
		    	elements.get(i).add(new ArrayList<Element>());
		    }
		}
	}
	
	public Element GetWall(int x,int y){
		
		for(int i=0;i<Get_List_Elements(x,y).size();i++) 
			if(Get_List_Elements(x,y).get(i) instanceof Wall) 
				return Get_List_Elements(x,y).get(i);
		
		return null;
	}
	
	public ArrayList<Element> Get_List_Elements(int x,int y){
		return elements.get(x).get(y);
	}
	
	public int Get_RightBound() {
		return RightBound;
	}
	
	public int Get_LeftBound() {
		return LeftBound+1;
	}
	
	public int Get_BotBound() {
		return BotBound;
	}
	
	public int Get_TopBound() {
		return TopBound+1;
	}
}
