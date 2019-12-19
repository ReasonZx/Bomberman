package client;
import java.io.IOException;

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
	private Image Back;
	private Image signup;
	private Image Ok;
	private int signup_x, signup_y;
	private int ok_x, ok_y;
	private String user;
	private String mail;
	private String pass;
	private String pass_rpt;
	private boolean pass_match = true;
	private String server_response;
	private boolean say_ok;
	private Shape R2;

	@Override
	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
		sbg = (GUI_setup) arg1;
		sbg.Set_Signup_State(getID());
		myFont = gc.getDefaultFont();
		new Rectangle(20,20,gc.getWidth()-40,gc.getHeight()-40);
		R2 = new Rectangle(0, gc.getHeight()/3f, gc.getWidth(), gc.getHeight()/3f);
		
		signup = new Image("sprites/signup.png");
		signup = signup.getScaledCopy(0.5f);
		signup_x = (int) ((float) sbg.Get_Display_width() * 0.5 - signup.getWidth() / 2);
		signup_y = (int) ((float) sbg.Get_Display_height() * 0.75);
		
		Ok = new Image("sprites/ok.png");
		Ok = Ok.getScaledCopy(0.3f);
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
				if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					sbg.enterState(sbg.Get_Menu_State());
				}
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
		}
		else {
			if ((posX > ok_x && posX < ok_x + Ok.getWidth()) && (posY > ok_y && posY < ok_y + Ok.getHeight())) { 
				if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					sbg.enterState(sbg.Get_Menu_State());
				}
		}	}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		
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
		Back.draw(50, 50);
		signup.draw(signup_x, signup_y);
		
		
		if (!pass_match) {
			g.setColor(Color.red);
			g.drawString("Passwords don't match",
					(int) ((float) sbg.Get_Display_width() * 0.50) - myFont.getWidth("Passwords don't match") / 2,
					(int) ((float) sbg.Get_Display_height() * 0.15));
			g.setColor(Color.white);
		}
		else if (server_response != null) {
				if(!server_response.equals("Registered Successfully")) {
					g.setColor(Color.red);
					g.drawString(server_response,(int) ((float) sbg.Get_Display_width() * 0.50) - myFont.getWidth(server_response) / 2,
							(int) ((float) sbg.Get_Display_height() * 0.15));
					g.setColor(Color.white);
				}
		if(say_ok) {
			g.setColor(Color.white);
			g.fill(R2);
			g.setColor(Color.black);
			Ok.draw(ok_x,ok_y);
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

	@Override
	public int getID() {
		return 7;
	}
}
