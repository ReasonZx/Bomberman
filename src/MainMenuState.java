import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicGameState{

	Image Play;
	Image Controls;
	private GUI_setup sbg;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		Controls = new Image("sprites/Button_Controls.png");
		Play = new Image("sprites/exitGame.png");
		sbg=(GUI_setup) arg1;
		sbg.Set_MainMenu_State(getID());
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		arg0.getInput().clearMousePressedRecord();
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		// TODO Auto-generated method stub
		arg2.drawString("BOMBERMAN", 350, 100);
		Play.draw(225,200);
		Controls.draw(200,300);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		// TODO Auto-generated method stub
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
		
		if((posX>125 && posX<400) && (posY > 200 && posY < 300)) {		// ver tamanhos certos dos botões
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Game_State());
			}
		}
		
		if((posX>125 && posX<400) && (posY > 400 && posY < 500)) {	// ver tamanhos certos dos botões
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Controls_State());
			}
		}
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 5;
	}
	
}
