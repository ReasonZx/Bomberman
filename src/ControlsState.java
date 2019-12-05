import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ControlsState extends BasicGameState{

	private GUI_setup sbg;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		sbg=(GUI_setup) arg1;
		sbg.Set_Controls_State(getID());
	}
	
	@Override
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 6;
	}

}
