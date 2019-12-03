import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOverState extends BasicGameState{
	private int Winner;
	private Gamestate Game;
	private KeyPressAny Input;
	protected StateBasedGame sbg;
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Input = new KeyPressAny();
		sbg=arg1;
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Game=(Gamestate) arg1.getState(3);
		if(Game.getDeath()==1)
			Winner=2;
		else
			Winner=1;
		arg0.getInput().addKeyListener(Input);
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		Input.inputStarted();	
		Input.inputEnded();
	}
	

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("YOU WIN PLAYER " + Winner, 300, 300);
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().removeKeyListener(Input);
	}

	public int getID() {
		return 4;
	}

	public class KeyPressAny implements KeyListener{
		boolean used=false;
		@Override
		public void inputEnded() {
			// TODO Auto-generated method stub
			used=false;
		}

		@Override
		public void inputStarted() {
			// TODO Auto-generated method stub
			used=true;
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
		public void keyPressed(int arg0, char arg1) {
			sbg.enterState(1);
		}

		@Override
		public void keyReleased(int arg0, char arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
