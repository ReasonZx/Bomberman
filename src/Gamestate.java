import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gamestate extends BasicGameState{

	 static GameLogic L;
	 static Bomber player;
	 static Wall walls;
	 static Bomb bombs;
	 private ArrayList<Element> elements;
	 private Image_Library lib;
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		 
		 lib = new Image_Library();
	     L=new GameLogic(lib);
	     player=new Bomber(1,1,L);
	     L.Place_Character(player);
	}

	public void update(GameContainer container, StateBasedGame sbg, int arg2) throws SlickException {
		lib.Run_Changes();
		
		if(container.getInput().isKeyDown(Input.KEY_W)) {
			L.Action(87);
		}
		if(container.getInput().isKeyDown(Input.KEY_S)) {
			L.Action(83);
		}
		if(container.getInput().isKeyDown(Input.KEY_A)) {
			L.Action(65);
		}
		if(container.getInput().isKeyDown(Input.KEY_D)) {
			L.Action(68);
		}
		if(container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			L.Action(32);
		}
		
		L.Death_Check();
	}
	

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("Game state", 50, 50);
		g.drawString(player.toString(),100,100);
		
		
		for(int x = 0 ; x < L.m.Get_RightBound() ; x++) {
			for(int y = 0 ; y < L.m.Get_BotBound() ; y++) {
					elements=L.m.Get_List_Elements(x, y);
					for(int i = 0; i < elements.size(); i++)
						if(elements.get(i).Has_Image())
							g.drawImage(elements.get(i).Get_Image(), 32*elements.get(i).getX(), 32*elements.get(i).getY());
					}
		}
	}

	public int getID() {
		return 2;
	}

}
