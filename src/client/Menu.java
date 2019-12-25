package client;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tests.SoundTest;
import javax.swing.JOptionPane;
import org.lwjgl.input.Mouse;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
import org.newdawn.slick.*;

public class Menu extends BasicGameState {

	private Image exitGame;
	private Image exit_hover;
	private int exit_x, exit_y;
	private Image logIn;
	private Image logIn_hover;
	private int login_x, login_y;
	private Image Guest;
	private Image Guest_h;
	private int guest_x, guest_y;
	private int signup_x, signup_y;
	private Image menu;
	private Image bomberman_title;
	private int bomberman_x, bomberman_y;
	private GUI_setup sbg;
	private int highlighted = 0;
	private boolean login_h = false;
	private boolean exit_h = false;
	private boolean guest_h = false;

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
		logIn_hover = new Image("sprites/logIn_hover.png");
		logIn_hover = logIn_hover.getScaledCopy(0.6f);
		login_x = (int) ((float) sbg.Get_Display_width() * 0.50 - logIn.getWidth() / 2);
		login_y = (int) ((float) sbg.Get_Display_height() * 0.35);
		Guest = new Image("sprites/play.png");
		Guest = Guest.getScaledCopy(0.6f);
		Guest_h = new Image("sprites/play_hover.png");
		Guest_h = Guest_h.getScaledCopy(0.6f);
		guest_x = (int) ((float) sbg.Get_Display_width() * 0.50 - Guest.getWidth() / 2);
		guest_y = (int) ((float) sbg.Get_Display_height() * 0.55);
		exitGame = new Image("sprites/exitGame.png");
		exitGame = exitGame.getScaledCopy(0.6f);
		exit_hover = new Image("sprites/exitGame_hover.png");
		exit_hover = exit_hover.getScaledCopy(0.6f);
		exit_x = (int) ((float) sbg.Get_Display_width() * 0.50 - exitGame.getWidth() / 2);
		exit_y = (int) ((float) sbg.Get_Display_height() * 0.75);
		menu = new Image("sprites/menu.png");
		signup_x = (int) ((float) sbg.Get_Display_width() * 0.50 - logIn.getWidth() / 2);
		signup_y = (int) ((float) sbg.Get_Display_height() * 0.49);
		bomberman_title = new Image("sprites/bomberman_title.png");
		bomberman_x = (int) ((float) sbg.Get_Display_width() * 0.50 - bomberman_title.getWidth() / 2);
		bomberman_y = (int) ((float) sbg.Get_Display_height() * 0.05);
		
		URL url = this.getClass().getClassLoader().getResource("music/test.wav");
		
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
	        clip.start();
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void update(GameContainer container, StateBasedGame arg1, int delta) throws SlickException {
		int posX = Mouse.getX();
		int posY = sbg.Get_Display_height() - Mouse.getY();
		if ((posX > login_x && posX < login_x + logIn.getWidth())
				&& (posY > login_y && posY < login_y + logIn.getHeight())) {
			login_h = true;
			if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				if (sbg.server != null) {
					sbg.enterState(sbg.Get_Login_State());
				} else {
					JOptionPane.showMessageDialog(null,
							"Cant' connect to server, restart the game and try again later.\nServer might be down.");
				}
			}
		}
		else login_h = false;

		if ((posX > exit_x && posX < exit_x + exitGame.getWidth())
				&& (posY > exit_y && posY < exit_y + exitGame.getHeight())) {
			exit_h = true;
			if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				System.exit(0);
			}
		}
		else exit_h = false;

		if ((posX > guest_x && posX < guest_x + Guest.getWidth())
				&& (posY > guest_y && posY < guest_y + Guest.getHeight())) {
			guest_h = true;
			if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Game_State());
			}
		}
		else guest_h = false;

		if ((posX > signup_x && posX < signup_x + 280) && (posY > signup_y && posY < signup_y + 18)) {
			highlighted = 1;
			if (container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Signup_State());
			}
		} else {
			highlighted = 0;
		}
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		menu.draw(0, 0);
		bomberman_title.draw(bomberman_x, bomberman_y);
		if(guest_h == false) {
			Guest.draw(guest_x, guest_y);
		}
		else Guest_h.draw(guest_x, guest_y);
		
		if (login_h == false) {
			logIn.draw(login_x,login_y);
		}
		else logIn_hover.draw(login_x, login_y);
		
		if(exit_h == false) {
			exitGame.draw(exit_x, exit_y);
		}
		else exit_hover.draw(exit_x, exit_y);
		
		
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