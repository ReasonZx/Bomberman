import java.util.ArrayList;
import org.newdawn.slick.SlickException;

public class GameLogic {
	Bomber Character;
	Map m;
	Image_Library lib;
	
	GameLogic(Image_Library l){
		ArrayList<Element> y = new ArrayList<Element>();
		m = new Map(y);
		lib=l;
	}

	public void Action(int key) throws SlickException {
		
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
			Bomb b = new Bomb(Character.getX(),Character.getY(),this);
			b.Start_Countdown();
			m.Add_Element(b);
			//System.out.println("created new bomb");
		}
	
	}

	public void Place_Character(Bomber b) {
		Character=b;
		m.Add_Element(Character);
	}
	
	public void Explode(Bomb b) throws SlickException{
		m.Remove_Element(b);
		Propagate_Explosion(b.getX(),b.getY(),b);
	}
	
	private void Propagate_Explosion(int x,int y,Bomb b) throws SlickException {
		Explosion Exp = new Explosion(x,y,this,0,b);
		ArrayList<Element> tmp = Exp.Propagate();
		m.Add_Element_Array(tmp);
	}
	
	private boolean Can_Place_Bomb() {
			if(Character.Can_Use_Bomb()==false || m.Has_Bomb(Character.getX(),Character.getY())==true)
				return false;
		
		Character.Used_Bomb();
		
		return true;
	}
	
	public void Death_Check(){
		if(Character.Death_Check()==true){
			System.out.println("YOU DEAD MAN");
		}
	}
	
	private boolean MoveLeftPermitted(){
		if(m.Out_Of_Bounds(Character.getX()-1,Character.getY())==true || Character.Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.getX()-1,Character.getY());
	}
	
	private boolean MoveRightPermitted(){
		if(m.Out_Of_Bounds(Character.getX()+1,Character.getY())==true || Character.Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.getX()+1,Character.getY());
	}
	
	private boolean MoveDownPermitted(){
		if(m.Out_Of_Bounds(Character.getX(),Character.getY()+1)==true || Character.Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.getX(),Character.getY()+1);
	}
	
	private boolean MoveUpPermitted(){
		if(m.Out_Of_Bounds(Character.getX(),Character.getY()-1)==true || Character.Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.getX(),Character.getY()-1);
	}
}
