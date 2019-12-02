import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gamestate extends BasicGameState{

	 static GameLogic L;
	 private ArrayList<Bomber> players = new ArrayList<Bomber>();
	 static Wall walls;
	 static Bomb bombs;
	 private ArrayList<Element> elements;
	 private Image_Library lib;
	 KeyPresses Input;
	 protected int Last_key=0;
	 protected char Last_c=0;
	 protected int counter=0;
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		 
		 lib = new Image_Library();
	     L=new GameLogic(lib);
	     players.add(new Bomber(1,1,L,0,1,2,3,87,83,65,68,32));
	     arg0.getInput();
	     players.add(new Bomber(11,8,L,4,2,5,6,org.newdawn.slick.Input.KEY_UP,
											  org.newdawn.slick.Input.KEY_DOWN,
											  org.newdawn.slick.Input.KEY_LEFT,
											  org.newdawn.slick.Input.KEY_RIGHT,
											  13));
	     L.Create_Map(1);
	     L.Place_Characters(players);
	     Input = new KeyPresses();
	     arg0.getInput().addKeyListener(Input);
	}

	public void update(GameContainer container, StateBasedGame sbg, int arg2) throws SlickException {
		if (container.getInput().isKeyDown(Last_key)==true) {
			counter++;
			if(counter>20)
				Input.keyPressed(Last_key, Last_c);
		}
		else {
			counter=0;
			Input.inputStarted();
		}
		Input.inputEnded();
		
		lib.Run_Changes();
		int dead=L.Death_Check();
		
		if(dead!=0) {
							//Go to Game Over
		}
	}
	

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		//g.drawString("Game state", 50, 50);
		//g.drawString(player1.toString(),100,100);
		
		
		for(int x = 0 ; x < L.m.Get_RightBound() ; x++) {
			for(int y = 0 ; y < L.m.Get_BotBound() ; y++) {
					elements=L.m.Get_List_Elements(x, y);
					for(int i = 0; i < elements.size(); i++) {
						Element tmp = elements.get(i);
						
						if(tmp.Has_Image()) {
							g.drawImage(tmp.Get_Image(),
										tmp.Get_Scale()*tmp.getX() + tmp.Get_OffsetX(), 
										tmp.Get_Scale()*tmp.getY() + tmp.Get_OffsetY());
						}
					}
				}
		}
	}

	public int getID() {
		return 2;
	}
	
	public class KeyPresses implements KeyListener{
		boolean used=false;
		@Override
		public void inputEnded() {
			// TODO Auto-generated method stub
			used=false;
		}

		@Override
		public void inputStarted() {
			// TODO Auto-generated method stub
			used = true;
		}

		@Override
		public boolean isAcceptingInput() {
			// TODO Auto-generated method stub
			return used;
		}

		@Override
		public void setInput(org.newdawn.slick.Input arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(int key, char c) {
			// TODO Auto-generated method stub
			
			String tmp = new String();
			tmp="" + c;
			tmp=tmp.toUpperCase();
			
			try {
				if((int)tmp.charAt(0)!=0)
					L.Action((int)tmp.charAt(0));
				else
					L.Action(key);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Last_key=key;
			Last_c=c;
			counter=0;
		}

		@Override
		public void keyReleased(int key, char c) {
			// TODO Auto-generated method stub
			Last_key=0;
			Last_c=0;
		}
		
	}
}
