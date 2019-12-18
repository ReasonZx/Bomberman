package client;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import javax.swing.JOptionPane;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;

public class Menu extends BasicGameState {

	private Image exitGame;
	private int exit_x, exit_y;
	private Image logIn;
	private int login_x, login_y;
	private Image Guest;
	private int guest_x, guest_y;
	private int signup_x, signup_y;
	private Image menu;
	private Image bomberman_title;
	private int bomberman_x, bomberman_y;
	private GUI_setup sbg;
	private int highlighted = 0;

	@Override
	public void enter(GameContainer gc, StateBasedGame arg1) throws SlickException {
		gc.getInput().clearMousePressedRecord();
		// Mouse.setCursorPosition(0, 0);
	}

	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		sbg = (GUI_setup) arg1;
		sbg.Set_Menu_State(getID());

		logIn = new Image("sprites/logIn.png");
		logIn = logIn.getScaledCopy(0.6f);
		login_x = (int) ((float) sbg.Get_Display_width() * 0.50 - logIn.getWidth() / 2);
		login_y = (int) ((float) sbg.Get_Display_height() * 0.35);
		Guest = new Image("sprites/play.png");
		Guest = Guest.getScaledCopy(0.6f);
		guest_x = (int) ((float) sbg.Get_Display_width() * 0.50 - Guest.getWidth() / 2);
		guest_y = (int) ((float) sbg.Get_Display_height() * 0.55);
		exitGame = new Image("sprites/exitGame.png");
		exitGame = exitGame.getScaledCopy(0.6f);
		exit_x = (int) ((float) sbg.Get_Display_width() * 0.50 - exitGame.getWidth() / 2);
		exit_y = (int) ((float) sbg.Get_Display_height() * 0.75);
		menu = new Image("sprites/menu.png");
		signup_x = (int) ((float) sbg.Get_Display_width() * 0.50 - logIn.getWidth() / 2);
		signup_y = (int) ((float) sbg.Get_Display_height() * 0.49);
		bomberman_title = new Image("sprites/bomberman_title.png");
		bomberman_x = (int) ((float) sbg.Get_Display_width() * 0.50 - bomberman_title.getWidth() / 2);
		bomberman_y = (int) ((float) sbg.Get_Display_height() * 0.05);
	}

	public void update(GameContainer container, StateBasedGame arg1, int delta) throws SlickException {
		int posX = Mouse.getX();
		int posY = sbg.Get_Display_height() - Mouse.getY();
		if ((posX > login_x && posX < login_x + logIn.getWidth())
				&& (posY > login_y && posY < login_y + logIn.getHeight())) { // ver tamanhos certos dos bot�es
			if (container.getInput().isMousePressed(container.getInput().MOUSE_LEFT_BUTTON)) {
				if (sbg.server != null) {
					sbg.enterState(sbg.Get_Login_State());
				} else {
					JOptionPane.showMessageDialog(null,
							"Cant' connect to server, restart the game and try again later.\nServer might be down.");
				}
			}
		}

		if ((posX > exit_x && posX < exit_x + exitGame.getWidth())
				&& (posY > exit_y && posY < exit_y + exitGame.getHeight())) { // ver tamanhos certos dos bot�es
			if (container.getInput().isMousePressed(container.getInput().MOUSE_LEFT_BUTTON)) {
				System.exit(0);
			}
		}

		if ((posX > guest_x && posX < guest_x + Guest.getWidth())
				&& (posY > guest_y && posY < guest_y + Guest.getHeight())) { // ver tamanhos certos dos bot�es
			if (container.getInput().isMousePressed(container.getInput().MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Game_State());
			}
		}

		if ((posX > signup_x && posX < signup_x + 280) && (posY > signup_y && posY < signup_y + 18)) { // ver tamanhos
																										// certos dos
																										// bot�es
			highlighted = 1;
			if (container.getInput().isMousePressed(container.getInput().MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Signup_State());
			}
		} else {
			highlighted = 0;
		}
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		menu.draw(0, 0);
		bomberman_title.draw(bomberman_x, bomberman_y);
		Guest.draw(guest_x, guest_y);
		logIn.draw(login_x, login_y);
		exitGame.draw(exit_x, exit_y);
		g.setColor(Color.white);
		g.drawString("Dont have an account? Signup here", signup_x, signup_y);
		if (highlighted == 1) {
			g.drawLine(signup_x, signup_y + 18, signup_x + 300, signup_y + 18);
		}
	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
	}

	public int getID() {
		return 1;
	}

}