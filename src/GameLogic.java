import java.util.ArrayList;

public class GameLogic {
	Bomber Character;
	Map m;
	
	GameLogic(Bomber x){
		Character=x;
		ArrayList<Element> y = new ArrayList<Element>();
		y.add(Character);
		m = new Map(y);
	}

	public void Action(int key) {
		
		if(key==65 && MoveLeftPermitted()) { //A
			m.Remove_Element(Character);
			Character.MoveLeft();
			m.Add_Element(Character);
		}	
		
		if(key==68 && MoveRightPermitted()) { //D
			m.Remove_Element(Character);
			Character.MoveRight();
			m.Add_Element(Character);
		}	
		
		if(key==83 && MoveDownPermitted()) { //S
			m.Remove_Element(Character);
			Character.MoveDown();
			m.Add_Element(Character);
		}	
		
		if(key==87 && MoveUpPermitted()) { //W
			m.Remove_Element(Character);
			Character.MoveUp();
			m.Add_Element(Character);
		}
		
		if(key==32 && Can_Place_Bomb()==true) {	//Space
			Bomb b = new Bomb(Character.getX(),Character.getY(),m);
			m.Add_Element(b);
		}
	}
	
	private boolean Can_Place_Bomb() {
		if(m.Has_Bomb(Character.getX(),Character.getY())==true)
			return false;
		
		return true;
	}

	private boolean MoveLeftPermitted(){
		if(m.Out_Of_Bounds(Character.getX()-1,Character.getY())==true)
			return false;
		
		return !m.Has_Solid_Element(Character.getX()-1,Character.getY());
	}
	
	private boolean MoveRightPermitted(){
		if(m.Out_Of_Bounds(Character.getX()+1,Character.getY())==true)
			return false;
		
		return !m.Has_Solid_Element(Character.getX()+1,Character.getY());
	}
	
	private boolean MoveDownPermitted(){
		if(m.Out_Of_Bounds(Character.getX(),Character.getY()+1)==true)
			return false;
		
		return !m.Has_Solid_Element(Character.getX(),Character.getY()+1);
	}
	
	private boolean MoveUpPermitted(){
		if(m.Out_Of_Bounds(Character.getX(),Character.getY()-1)==true)
			return false;
		
		return !m.Has_Solid_Element(Character.getX(),Character.getY()-1);
	}
}
