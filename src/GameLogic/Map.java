package GameLogic;
import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6168971874434311471L;
	transient private ArrayList<ArrayList<ArrayList<Element>>> elements;
	public ArrayList<ArrayList<ArrayList<String>>> info_elements;
	private int LeftBound, RightBound, TopBound, BotBound;
	
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
				cha.lib.Removal_Change();
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
	
	public boolean Has_Blocking_Element(int x,int y) {
		ArrayList<Element> tmplist;
		tmplist=Get_List_Elements(x, y);
		for(int i=0;i<tmplist.size();i++){
			if(tmplist.get(i) instanceof Wall && tmplist.get(i).Is_Solid())
				return true;
		}
		return false;
	}
	
	private void Initialize_Space() {
		elements = new ArrayList<ArrayList<ArrayList<Element>>>(RightBound-1);
		info_elements = new ArrayList<ArrayList<ArrayList<String>>>(RightBound-1);
		
		for (int i = 0; i < RightBound; i++) {
			elements.add(new ArrayList<ArrayList<Element>>(BotBound));
			info_elements.add(new ArrayList<ArrayList<String>>(BotBound));
		    for (int j = 0; j < BotBound; j++) {
		    	elements.get(i).add(new ArrayList<Element>());
		    	info_elements.get(i).add(new ArrayList<String>());
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
	
	public void Update() {
		for(int x=0;x<elements.size();x++) {
			for(int y=0;y<elements.get(x).size();y++) {
				info_elements.get(x).get(y).removeAll(info_elements.get(x).get(y));
				for(int i=0;i<elements.get(x).get(y).size();i++)
					info_elements.get(x).get(y).add(elements.get(x).get(y).get(i).toString());
			}
		}
	}
}
