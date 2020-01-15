package client;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LogIn extends BasicGameState {

	private TextField Username;
	private TextField Password;
	private Font myFont;
	private String User; 
	private String Pass;
	private Image Back, Back_hover;
	private Image Login, Login_hover;
	private Image background;
	private int login_x, login_y;
	private int UserTextX, UserTextY;
	private int PassTextX, PassTextY;
	private int backX, backY;
	private boolean error_login = false;
	private boolean login_h = false , back_h = false;
	private boolean hovering_l = false , hovering_b = false;
	private GUI_setup sbg;
	private String server_response;
	private File click_file = new File("music/click.wav");
	private File hover_file = new File("music/hover.wav");

	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
		sbg = (GUI_setup) arg1;
		sbg.Set_Login_State(getID());
		myFont = gc.getDefaultFont();

		UserTextX = (int) (sbg.Get_Display_width()/2f - 200);
		UserTextY = (int) (sbg.Get_Display_height()/2f-sbg.Get_Display_height()/10f);

		Username = new TextField(gc, myFont, UserTextX, UserTextY, 400, 20);
		Username.setBackgroundColor(Color.white);
		Username.setBorderColor(Color.white);
		Username.setTextColor(Color.black);

		PassTextX = (int) (sbg.Get_Display_width()/2f - 200);
		PassTextY = (int) (sbg.Get_Display_height()/2f+sbg.Get_Display_height()/10f);

		Password = new TextField(gc, myFont, PassTextX, PassTextY, 400, 20);
		Password.setBackgroundColor(Color.white);
		Password.setBorderColor(Color.white);
		Password.setTextColor(Color.black);

		Pass = new String();
		User = new String();

		Back = new Image("sprites/back.png");
		Back = Back.getScaledCopy(0.2f);
		Back_hover = new Image("sprites/back_hover.png");
		Back_hover = Back_hover.getScaledCopy(0.2f);
		
		background = new Image("sprites/background.png");
		
		Login = new Image("sprites/logIn.png");
		Login = Login.getScaledCopy(0.5f);
		Login_hover = new Image("sprites/logIn_hover.png");
		Login_hover = Login_hover.getScaledCopy(0.5f);

		backX = 50;
		backY = 50;

		login_x = (int) (0.43 * (float) sbg.Get_Display_width());
		login_y = (int) (0.7 * (float) sbg.Get_Display_height());
	}

	@Override
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
		Username.setAcceptingInput(true);
		Password.setAcceptingInput(true);
		Username.setText("");
		Password.setText("");
		Pass = "";
		User = "";
		server_response = "";
		error_login = false;
	}

	public void update(GameContainer gc, StateBasedGame arg1, int delta) throws SlickException {
		User = this.Username.getText();

		int posX = gc.getInput().getMouseX();
		int posY = gc.getInput().getMouseY();

		if ((posX > backX && posX < backX + Back.getWidth()) && (posY > backY && posY < backY + Back.getHeight())) {
			back_h = true;
			if(hovering_b == false) {
				play_hover_sound();
				hovering_b = true;
			}
			if (Mouse.isButtonDown(0)) {
				play_click_sound();
				sbg.enterState(sbg.Get_Menu_State());
			}
		}
		else {
			back_h = false;
			hovering_b = false;
		}

		if ((posX > login_x && posX < login_x + Login.getWidth())
				&& (posY > login_y && posY < login_y + Login.getHeight())) {
			login_h = true;
			if(hovering_l == false) {
				play_hover_sound();
				hovering_l = true;
			}
			if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				play_click_sound();
				try {
					String request = "login_" + this.User + "_" + this.Pass + "_" + sbg.server.ss.getLocalPort();
					server_response = sbg.server.request(request);
					if (server_response.equals("Logged in")) {
						error_login = false;
						sbg.enterState(sbg.Get_MainMenu_State());
					} else {
						error_login = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			hovering_l = false;
			login_h = false;
		}

		if (this.Password.hasFocus() && gc.getInput().isKeyPressed(15)) {
			this.Username.setFocus(true);
		}

		if (this.Username.hasFocus() && gc.getInput().isKeyPressed(15)) {
			this.Password.setFocus(true);
		}

		if (this.Password.hasFocus() && gc.getInput().isKeyPressed(28)) {
			try {
				String request = "login_" + this.User + "_" + this.Pass + "_" + sbg.server.ss.getLocalPort();
				server_response = sbg.server.request(request);
				if (server_response.equals("Logged in")) {
					error_login = false;
					sbg.enterState(sbg.Get_MainMenu_State());
				} else {
					error_login = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String temp = new String();

		if (this.Password.hasFocus()) {
			if (this.Password.getText().length() > this.Pass.length()) {
				this.Pass = this.Pass + this.Password.getText().substring(this.Password.getText().length() - 1,
						this.Password.getText().length());
			} else if (this.Password.getText().length() < this.Pass.length()) {
				this.Pass = this.Pass.substring(0, this.Pass.length() - 1);
			} else {
				for (int i = 0; i < this.Password.getText().length(); i++)
					temp = temp + "*";
				this.Password.setText(temp);
			}
		}
	}

	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		background.draw(0,0);
		g.drawString("Username:", UserTextX - 100, UserTextY);
		Username.render(gc, g);
		g.drawString("Password:", PassTextX - 100, PassTextY);
		Password.render(gc, g);
		if(back_h == false) {
			Back.draw(backX, backY);
		}
		else Back_hover.draw(backX, backY);

		if(login_h == false) {
			Login.draw(login_x, login_y);
		}
		else Login_hover.draw(login_x, login_y);

		if (error_login) {
			g.setColor(Color.white);
			g.drawString(server_response,
					(int) ((float) sbg.Get_Display_width() * 0.50) - myFont.getWidth(server_response) / 2,
					(int) ((float) sbg.Get_Display_height() * 0.15));
			g.setColor(Color.white);
		}
	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
		Username.setAcceptingInput(false);
		Password.setAcceptingInput(false);
		server_response = "";
		error_login = false;
	}
	
	public void play_hover_sound() {
		
		AudioInputStream hover_sound;
	
		try {
			hover_sound = AudioSystem.getAudioInputStream(hover_file);
			Clip hover_s = AudioSystem.getClip();
			hover_s.open(hover_sound);
			hover_s.loop(0);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	public void play_click_sound() {
		
		AudioInputStream click_sound;
		
		try {
			click_sound = AudioSystem.getAudioInputStream(click_file);
			Clip click_s = AudioSystem.getClip();
			click_s.open(click_sound);
			click_s.loop(0);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getID() {
		return 2;
	}
}