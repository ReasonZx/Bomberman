import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartGameState extends BasicGameState{
	

	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {

	}

	public void update(GameContainer container, StateBasedGame sbg, int arg2) throws SlickException {
		if(container.getInput().isKeyPressed(Input.KEY_ENTER)) {
			sbg.enterState(2);
		}
	}
	

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("Start State", 50, 50);
	}

	public int getID() {
		return 1;
	}

}