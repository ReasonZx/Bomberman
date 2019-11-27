import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gamestate extends BasicGameState{

	 static GameLogic L;
	 static Bomber Elemento;
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		 Elemento=new Bomber(1,1);
	     L=new GameLogic(Elemento);
	}

	public void update(GameContainer container, StateBasedGame sbg, int arg2) throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_W)) {
			L.Action(87);
		}
		if(container.getInput().isKeyPressed(Input.KEY_S)) {
			L.Action(83);
		}
		if(container.getInput().isKeyPressed(Input.KEY_A)) {
			L.Action(65);
		}
		if(container.getInput().isKeyPressed(Input.KEY_D)) {
			L.Action(68);
		}
		if(container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			L.Action(32);
		}
	}
	

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("Game state", 50, 50);
		g.drawString(Elemento.toString(),100,100);
		//bomber_im.draw(bomber.getX(),bomber.getY());
		
	}

	public int getID() {
		return 2;
	}

}
