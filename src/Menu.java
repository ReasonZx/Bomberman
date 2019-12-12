import org.newdawn.slick.GameContainer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;

public class Menu extends BasicGameState{
	
	Image exitGame;
	int exit_x, exit_y;
	Image logIn;
	int login_x, login_y;
	Image Guest;
	private Image menu;
	private Image bomberman_title;
	int guest_x, guest_y;
	private GUI_setup sbg;
	

	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		sbg = (GUI_setup) arg1;
		
		logIn = new Image("sprites/logIn.png");
		logIn = logIn.getScaledCopy(0.6f);
		login_x = (int) ((float) sbg.Get_Display_width() * 0.40);
		login_y = (int) ((float) sbg.Get_Display_height() * 0.35);
		Guest = new Image("sprites/play.png");
		Guest = Guest.getScaledCopy(0.6f);
		guest_x = (int) ((float) sbg.Get_Display_width() * 0.40);
		guest_y = (int) ((float) sbg.Get_Display_height() * 0.55);
		exitGame = new Image("sprites/exitGame.png");
		exitGame = exitGame.getScaledCopy(0.6f);
		exit_x = (int) ((float) sbg.Get_Display_width() * 0.40);
		exit_y = (int) ((float) sbg.Get_Display_height() * 0.75);
		menu = new Image("sprites/menu.png");
		bomberman_title = new Image("sprites/bomberman_title.png");
		
		sbg=(GUI_setup) arg1;
		sbg.Set_Menu_State(getID());
	}

	public void update(GameContainer container, StateBasedGame arg1, int delta) throws SlickException {
		int posX = Mouse.getX();
		int posY = sbg.Get_Display_height() - Mouse.getY();
		if((posX>login_x && posX<login_x + logIn.getWidth()) && (posY > login_y && posY < login_y + logIn.getHeight())) {		// ver tamanhos certos dos botões
			if(Mouse.isButtonDown(0)) {
				sbg.enterState(sbg.Get_Login_State());
			}
		}
		
		if((posX > exit_x && posX < exit_x + exitGame.getWidth()) && (posY > exit_y && posY < exit_y + exitGame.getHeight())) {	// ver tamanhos certos dos botões
			if(Mouse.isButtonDown(0)) {
				System.exit(0);
			}
		}
		
		if((posX > guest_x && posX < guest_x + Guest.getWidth()) && (posY > guest_y && posY < guest_y + Guest.getHeight())) {	// ver tamanhos certos dos botões
			if(Mouse.isButtonDown(0)) {
				sbg.enterState(sbg.Get_Game_State());
			}
		}
	}
	

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("BOMBERMAN", 350, 50);
		menu.draw(0,0);
		bomberman_title.draw(100,50);
		Guest.draw(guest_x, guest_y);
		logIn.draw(login_x,login_y);
		exitGame.draw(exit_x,exit_y);
	}

	public int getID() {
		return 1;
	}

}