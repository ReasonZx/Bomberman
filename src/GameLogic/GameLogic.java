package GameLogic;
import java.util.ArrayList;

/**
 * @author João Gomes up201506251@fe.up.pt
 */
public class GameLogic {
	protected ArrayList<Bomber> Character;
	public Map m;
	protected Image_Library lib;
	protected Layout_Logic map_gen;
	
	/**
	 * Constructor for GameLogic Class
	 * <p>
	 * The Game Logic runs the actions and glues the game together with simple methods
	 * <p>
	 * @param l The library where Image changes are to be flagged
	 * @param m The map where the game is being played
	 */
	public GameLogic(Image_Library l,Map m){
		lib=l;
		this.m=m;
		Character = new ArrayList<Bomber>();
	}
	
	/**
	 * Propagates a keypress into the GameLogic Class
	 * <p>
	 * The method takes a key and player value and checks if they are associated, that is, if the key pressed is related to the player that pressed it
	 * If a match is found the method will check if the action is permitted on the map and available for the bomber
	 * Then it will remove any elements from the map if necessary, execute the action and add new elements to the map if needed
	 * <p>
	 * @param key The value of the key pressed
	 * @param player The player number that pressed the key (used by the server, can be zero if playing locally on same keyboard)
	 */
	public void Action(int key,int player){
		for(int i=0;i<Character.size();i++) {
			if(Character.get(i).Get_Player()==player || player==0) {
				if(key==Character.get(i).Get_MoveLeft_Key() && MoveLeftPermitted(i)) { //A
					m.Remove_Element(Character.get(i));
					Character.get(i).MoveLeft();
					m.Add_Element(Character.get(i));
				}	
				
				if(key==Character.get(i).Get_MoveRight_Key() && MoveRightPermitted(i)) { //D
					m.Remove_Element(Character.get(i));
					Character.get(i).MoveRight();
					m.Add_Element(Character.get(i));
				}	
				
				if(key==Character.get(i).Get_MoveDown_Key() && MoveDownPermitted(i)) { //S
					m.Remove_Element(Character.get(i));
					Character.get(i).MoveDown();
					m.Add_Element(Character.get(i));
				}	
				
				if(key==Character.get(i).Get_MoveUp_Key() && MoveUpPermitted(i)) { //W
					m.Remove_Element(Character.get(i));
					Character.get(i).MoveUp();
					m.Add_Element(Character.get(i));
				}
				
				if(key==Character.get(i).Get_Action_Key() && Can_Place_Bomb(i)==true) {	//Space
					Bomb b = new Bomb(Character.get(i).getX(),Character.get(i).getY(),lib,m);
					b.Start_Countdown();
					m.Add_Element(b);
					//System.out.println("created new bomb");
				}
			}
		}
	}

	/**
	 * Places Characters on the map
	 * <p>
	 * The method takes all characters as an array list, saves them into his own 
	 * and places all of them into the map as long as there's no solid object on their spots
	 * <p>
	 * @param b - The list of Players to be added to the game map
	 */
	public void Place_Characters(ArrayList<Bomber> b) {
		for(int i=0;i<b.size();i++)
			if(m.Has_Solid_Element(b.get(i).getX(), b.get(i).getY())==false){
				Character.add(b.get(i));
				m.Add_Element(b.get(i));
			}
		
	}
	
	private boolean Can_Place_Bomb(int i) {
		if(Character.get(i).Can_Use_Bomb()==false || m.Has_Bomb(Character.get(i).getX(),Character.get(i).getY())==true)
			return false;
		
		Character.get(i).Used_Bomb();
		
		return true;
	}
	
	/**
	 * Checks if anyone died
	 * @return The index+1 on the list of the player that died
	 */
	public int Death_Check(){
		for(int i=0;i<Character.size();i++)
			if(Character.get(i).Death_Check()==true){
				Character.remove(i);
				return i+1;
			}
		return 0;
	}
	
	private boolean MoveLeftPermitted(int i){
		if(m.Out_Of_Bounds(Character.get(i).getX()-1,Character.get(i).getY())==true || Character.get(i).Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.get(i).getX()-1,Character.get(i).getY());
	}
	
	private boolean MoveRightPermitted(int i){
		if(m.Out_Of_Bounds(Character.get(i).getX()+1,Character.get(i).getY())==true || Character.get(i).Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.get(i).getX()+1,Character.get(i).getY());
	}
	
	private boolean MoveDownPermitted(int i){
		if(m.Out_Of_Bounds(Character.get(i).getX(),Character.get(i).getY()+1)==true || Character.get(i).Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.get(i).getX(),Character.get(i).getY()+1);
	}
	
	private boolean MoveUpPermitted(int i){
		if(m.Out_Of_Bounds(Character.get(i).getX(),Character.get(i).getY()-1)==true || Character.get(i).Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.get(i).getX(),Character.get(i).getY()-1);
	}
}
