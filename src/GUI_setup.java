import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GUI_setup extends StateBasedGame{

	public GUI_setup(String title) {
		super(title);
	}
	
	public static void main(String args[]) throws SlickException {
		AppGameContainer app = new AppGameContainer(new GUI_setup("Setup"));
		app.setDisplayMode(800, 600, false);
		app.start();
	}

	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new StartGameState());
		this.addState(new Gamestate());
		this.addState(new GameOverState());
		}
}
