import org.newdawn.slick.GameContainer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;

public class Menu extends BasicGameState{
	
	Image exitGame;
	Image logIn;
	private GUI_setup sbg;
	

	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		logIn = new Image("sprites/logIn.png");
		exitGame = new Image("sprites/exitGame.png");
		sbg=(GUI_setup) arg1;
		sbg.Set_Menu_State(getID());
	}

	public void update(GameContainer container, StateBasedGame arg1, int delta) throws SlickException {
		int posX = Mouse.getX();
		int posY = 600 - Mouse.getY();
		if((posX>125 && posX<400) && (posY > 200 && posY < 300)) {		// ver tamanhos certos dos botões
			if(Mouse.isButtonDown(0)) {
				sbg.enterState(sbg.Get_Login_State());
			}
		}
		
		if((posX>125 && posX<400) && (posY > 400 && posY < 500)) {	// ver tamanhos certos dos botões
			if(Mouse.isButtonDown(0)) {
				System.exit(0);
			}
		}
	}
	

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("BOMBERMAN", 350, 100);
		logIn.draw(225,200);
		exitGame.draw(240,400);
	}

	public int getID() {
		return 1;
	}

}