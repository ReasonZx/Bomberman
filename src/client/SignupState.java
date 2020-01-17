package client;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class SignupState extends BasicGameState {

	private GUI_setup sbg;
	private TextField Username;
	private TextField Password;
	private TextField Email;
	private TextField Password_repeat;
	private Font myFont;
	private Image Back, Back_hover;
	private Image signup, signup_hover;
	private Image Ok,Ok_hover;
	private Image background;
	private int signup_x, signup_y;
	private int ok_x, ok_y;
	private String user;
	private String mail;
	private String pass;
	private String pass_rpt;
	private boolean pass_match = true;
	private String server_response;
	private boolean say_ok;
	private boolean hovering_l = false, hovering_b = false, hovering_o = false;
	private boolean signup_h = false, back_h = false, ok_h = false;
	private Shape R2;
	private File click_file = new File("music/click.wav");
	private File hover_file = new File("music/hover.wav");

	@Override
	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
		sbg = (GUI_setup) arg1;
		sbg.Set_Signup_State(getID());
		myFont = gc.getDefaultFont();
		new Rectangle(20,20,gc.getWidth()-40,gc.getHeight()-40);
		R2 = new Rectangle(0, gc.getHeight()/3f, gc.getWidth(), gc.getHeight()/3f);
		
		background = new Image("sprites/background.png");
		
		signup = new Image("sprites/signup.png");
		signup = signup.getScaledCopy(0.5f);
		signup_hover = new Image("sprites/signup_hover.png");
		signup_hover = signup_hover.getScaledCopy(0.5f);
		
		signup_x = (int) ((float) sbg.Get_Display_width() * 0.5 - signup.getWidth() / 2);
		signup_y = (int) ((float) sbg.Get_Display_height() * 0.75);
		
		Ok = new Image("sprites/ok.png");
		Ok = Ok.getScaledCopy(0.3f);
		Ok_hover = new Image("sprites/ok_hover.png");
		Ok_hover = Ok_hover.getScaledCopy(0.3f);
		
		ok_x = (int) ((float) sbg.Get_Display_width() * 0.5 - Ok.getWidth() / 2);
		ok_y = (int) ((float) sbg.Get_Display_height() * 0.59);

		Username = new TextField(gc, myFont, (int) ((float) sbg.Get_Display_width() * 0.40),
				(int) ((float) sbg.Get_Display_height() * 0.30), 400, 20);
		Username.setBackgroundColor(Color.white);
		Username.setBorderColor(Color.white);
		Username.setTextColor(Color.black);

		Password = new TextField(gc, myFont, (int) ((float) sbg.Get_Display_width() * 0.40),
				(int) ((float) sbg.Get_Display_height() * 0.50), 400, 20);
		Password.setBackgroundColor(Color.white);
		Password.setBorderColor(Color.white);
		Password.setTextColor(Color.black);

		Email = new TextField(gc, myFont, (int) ((float) sbg.Get_Display_width() * 0.40),
				(int) ((float) sbg.Get_Display_height() * 0.40), 400, 20);
		Email.setBackgroundColor(Color.white);
		Email.setBorderColor(Color.white);
		Email.setTextColor(Color.black);

		Password_repeat = new TextField(gc, myFont, (int) ((float) sbg.Get_Display_width() * 0.40),
				(int) ((float) sbg.Get_Display_height() * 0.60), 400, 20);
		Password_repeat.setBackgroundColor(Color.white);
		Password_repeat.setBorderColor(Color.white);
		Password_repeat.setTextColor(Color.black);

		Back = new Image("sprites/back.png");
		Back = Back.getScaledCopy(0.2f);
		Back_hover = new Image("sprites/back_hover.png");
		Back_hover = Back_hover.getScaledCopy(0.2f);

		user = "";
		pass = "";
		pass_rpt = "";
		mail = "";
		
		say_ok = false;
	}

	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
		Username.setAcceptingInput(true);
		Username.setText("");
		Password.setAcceptingInput(true);
		Password.setText("");
		Password_repeat.setAcceptingInput(true);
		Password_repeat.setText("");
		Email.setAcceptingInput(true);
		Email.setText("");
		pass_match = true;
		server_response = "";
		say_ok = false;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = gc.getInput().getMouseX();
		int posY = gc.getInput().getMouseY();
		
		if(!say_ok) {
			user = Username.getText();
			mail = Email.getText();
	
			if ((posX > 50 && posX < 50 + Back.getWidth()) && (posY > 50 && posY < 50 + Back.getHeight())) {
				back_h = true;
				if(hovering_b == false) {
					play_hover_sound();
					hovering_b = true;
				}
				if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					play_click_sound();
					sbg.enterState(sbg.Get_Menu_State());
				}
			}
			else {
				back_h = false;
				hovering_b = false;
			}
	
			if (Username.hasFocus() && gc.getInput().isKeyPressed(15)) {
				Email.setFocus(true);
			}
	
			if (Email.hasFocus() && gc.getInput().isKeyPressed(15)) {
				Password.setFocus(true);
			}
	
			if (Password.hasFocus() && gc.getInput().isKeyPressed(15)) {
				Password_repeat.setFocus(true);
			}
	
			if (Password_repeat.hasFocus() && gc.getInput().isKeyPressed(15)) {
				Username.setFocus(true);
			}
	
			String temp = new String();
			String temp1 = new String();
	
			if (Password.hasFocus()) {
				if (Password.getText().length() > pass.length()) {
					pass = pass
							+ Password.getText().substring(Password.getText().length() - 1, Password.getText().length());
				} else if (Password.getText().length() < pass.length()) {
					pass = pass.substring(0, pass.length() - 1);
				} else {
					for (int i = 0; i < Password.getText().length(); i++)
						temp = temp + "*";
					Password.setText(temp);
				}
			}
			
	
			if (Password_repeat.hasFocus()) {
				if (Password_repeat.getText().length() > pass_rpt.length()) {
					pass_rpt = pass_rpt + Password_repeat.getText().substring(Password_repeat.getText().length() - 1,
							Password_repeat.getText().length());
				} else if (Password_repeat.getText().length() < pass_rpt.length()) {
					pass_rpt = pass_rpt.substring(0, pass_rpt.length() - 1);
				} else {
					for (int i = 0; i < Password_repeat.getText().length(); i++)
						temp1 = temp1 + "*";
					Password_repeat.setText(temp1);
				}
			}
			
			if ((Password_repeat.hasFocus() && gc.getInput().isKeyPressed(28))
					|| (((posX > signup_x && posX < signup_x + signup.getWidth())
							&& (posY > signup_y && posY < signup_y + signup.getHeight()))
							&& gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))) {
				play_click_sound();
				if (!pass.equals(pass_rpt)) {
					pass_match = false;
				} else {
					try {
						pass_match = true;
						String request = "register_" + this.user + "_" + this.pass + "_" + this.mail;
						server_response = sbg.server.request(request);
						System.out.println(server_response);
						if (server_response.equals("Registered Successfully")) {
							say_ok = true;
						}
	
					} catch (IOException e) {
						e.printStackTrace();
					}
	
				}
			}
			
			if((posX > signup_x && posX < signup_x + signup.getWidth()) && (posY > signup_y && posY < signup_y + signup.getHeight())){
				signup_h = true;
				if(hovering_l == false) {
					play_hover_sound();
					hovering_l = true;
				}
			}
			else {
				hovering_l = false;
				signup_h = false;
			}
		}
		else {
			if ((posX > ok_x && posX < ok_x + Ok.getWidth()) && (posY > ok_y && posY < ok_y + Ok.getHeight())) {
				ok_h = true;
				if(hovering_o == false) {
					play_hover_sound();
					hovering_o = true;
				}
				if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					sbg.enterState(sbg.Get_Menu_State());
				}
			}	
			else {
				ok_h = false;
				hovering_o = false;
			}
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		background.draw(0,0);
		g.drawString("Username:", (int) ((float) sbg.Get_Display_width() * 0.25),
				(int) ((float) sbg.Get_Display_height() * 0.30));
		g.drawString("Email:", (int) ((float) sbg.Get_Display_width() * 0.25),
				(int) ((float) sbg.Get_Display_height() * 0.40));
		g.drawString("Password:", (int) ((float) sbg.Get_Display_width() * 0.25),
				(int) ((float) sbg.Get_Display_height() * 0.50));
		g.drawString("Repeat Password:", (int) ((float) sbg.Get_Display_width() * 0.25),
				(int) ((float) sbg.Get_Display_height() * 0.60));
		Username.render(gc, g);
		Password.render(gc, g);
		Email.render(gc, g);
		Password_repeat.render(gc, g);
		
		if(back_h == false) {
			Back.draw(50, 50);
		}
		else Back_hover.draw(50, 50);
		
		if(signup_h == false) {
			signup.draw(signup_x, signup_y);
		}
		else signup_hover.draw(signup_x, signup_y);

		
		
		if (!pass_match) {
			g.setColor(Color.white);
			g.drawString("Passwords don't match",
					(int) ((float) sbg.Get_Display_width() * 0.50) - myFont.getWidth("Passwords don't match") / 2,
					(int) ((float) sbg.Get_Display_height() * 0.15));
			g.setColor(Color.white);
		}
		else if (server_response != null) {
				if(!server_response.equals("Registered Successfully")) {
					g.setColor(Color.white);
					g.drawString(server_response,(int) ((float) sbg.Get_Display_width() * 0.50) - myFont.getWidth(server_response) / 2,
							(int) ((float) sbg.Get_Display_height() * 0.15));
					g.setColor(Color.white);
				}
		if(say_ok) {
			g.setColor(Color.white);
			g.fill(R2);
			g.setColor(Color.black);
			if(ok_h == false) {
				Ok.draw(ok_x,ok_y);
			}
			else Ok_hover.draw(ok_x,ok_y);
			
			g.drawString(server_response,(int) ((float) sbg.Get_Display_width() * 0.50) - myFont.getWidth(server_response) / 2,
					(int) ((float) sbg.Get_Display_height() * 0.45));
		}
		
		}
	}
	
	

	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Username.setAcceptingInput(false);
		Password.setAcceptingInput(false);
		Password_repeat.setAcceptingInput(false);
		Email.setAcceptingInput(false);
		arg0.getInput().clearMousePressedRecord();
		pass_match = true;
		server_response = "";
		say_ok = false;
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

	

	@Override
	public int getID() {
		return 7;
	}
}
