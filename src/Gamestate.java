import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gamestate extends BasicGameState{

	private Image bomber_im = null;
	private Element bomber;
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		bomber_im = new Image("sprites/parado.png");
	}

	public void update(GameContainer container, StateBasedGame sbg, int arg2) throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_1)) {
			sbg.enterState(3);
		}
	}
	

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("Game state", 50, 50);
		bomber_im.draw(bomber.getX(),bomber.getY());
		
	}

	public int getID() {
		return 2;
	}

}
