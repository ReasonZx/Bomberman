import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameLogic {
	Bomber Character;
	Map m;
	
	GameLogic(Bomber x){
		Character=x;
		ArrayList<Element> y = new ArrayList<Element>();
		y.add(Character);
		m = new Map(y);
		Character.Set_GameLogic(this);
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
			Bomb b = new Bomb(Character.getX(),Character.getY(),this);
			b.Start_Countdown(new Explode(b));
			m.Add_Element(b);
			//System.out.println("created new bomb");
		}
		
		Death_Check();
	
	}
	
	private class Explode extends TimerTask{
		private Bomb b;
		
		Explode (Bomb x){
			this.b=x;
		}
		
		public void run() {
			m.Remove_Element(this.b);
			Propagate_Explosion(b.getX(),b.getY());
		}
		
	}
	
	private class Remove_Explosion extends TimerTask{
		private ArrayList<Element> array;
		
		Remove_Explosion(ArrayList<Element> x){
			this.array=x;
		}
		
		public void run() {
			for(int i=0;i<array.size();i++) {
				m.Remove_Element(array.get(i));
			}
		}
	}
	
	private void Propagate_Explosion(int x,int y) {
		Explosion Exp = new Explosion(x,y,this);
		ArrayList<Element> tmp = Exp.Propagate();
		m.Add_Element_Array(tmp);
		Timer tt = new Timer();
		tt.schedule(new Remove_Explosion(tmp), 100);
		Death_Check();
	}
	
	private boolean Can_Place_Bomb() {
			if(Character.Can_Use_Bomb()==false || m.Has_Bomb(Character.getX(),Character.getY())==true)
				return false;
		
		Character.Used_Bomb();
		
		return true;
	}
	
	private void Death_Check(){
		if(Character.Death_Check()==true){
			System.out.println("YOU DEAD MAN");
		}
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
