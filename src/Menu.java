import org.newdawn.slick.GameContainer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState{
	
	Image exitGame;
	Image logIn;
	

	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		logIn = new Image("sprites/logIn.png");
		exitGame = new Image("sprites/exitGame.png");

	}

	public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
		delta = 60;
		int posX = Mouse.getX();
		int posY = 600 - Mouse.getY();
		if((posX>125 && posX<400) && (posY > 200 && posY < 300)) {		// ver tamanhos certos dos botões
			if(Mouse.isButtonDown(0)) {
				sbg.enterState(2);
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