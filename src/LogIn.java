import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.IOException;
import javax.swing.*;


public class LogIn extends BasicGameState{
	
    private TextField Username;
    private TextField Password;
	private org.newdawn.slick.Font trueTypeFont;
	private String User;								//For hardcoded login
	private String Pass;								//For hardcoded login
	private Image Back;
	private int textWidth, textLenght;
	private int UserTextX, UserTextY;
	private int PassTextX, PassTextY;
	private int backX, backY, backWidth, backLength;
	private Image Login;
	private boolean error_login = false;


	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		  Font font = new Font("Calibri", Font.PLAIN, 15);
		  trueTypeFont = new TrueTypeFont(font, true);
		  
		  this.textWidth = 400;
		  this.textLenght = 20;
		  
		  this.UserTextX = 200;
		  this.UserTextY = 200;
	      
	      this.Username = new TextField(gc, this.trueTypeFont, this.UserTextX, this.UserTextY, this.textWidth , this.textLenght);
	      this.Username.setBackgroundColor(Color.white);
	      this.Username.setBorderColor(Color.white);
	      this.Username.setTextColor(Color.black);
	      
	      this.PassTextX = 200;
	      this.PassTextY = 300;
	      
	      this.Password = new TextField(gc, this.trueTypeFont, this.PassTextX, this.PassTextY, this.textWidth , this.textLenght);
	      this.Password.setBackgroundColor(Color.white);
	      this.Password.setBorderColor(Color.white);
	      this.Password.setTextColor(Color.black);
	      
	      this.Pass = new String();
	      this.User = new String();
	      
	      this.Back = new Image("sprites/back.png");
	      this.Login = new Image("sprites/logIn.png");
	      
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
			//delta = 60;
			//this.Pass = this.Password.getText();
			this.User = this.Username.getText();
			this.backX = 50;
			this.backY = 50;
			this.backLength = 30;
			this.backWidth = 50;
			
			
			int posX = Mouse.getX();
			int posY = 600 - Mouse.getY();
			if((posX > this.backX && posX < (this.backX + this.backWidth)) && (posY > this.backY && posY < (this.backY + this.backLength))) {		// ver tamanhos certos dos botões	//go back
				if(Mouse.isButtonDown(0)) {
					sbg.enterState(1);
				}
			}
			
			if((posX > this.UserTextX && posX < (this.UserTextX + this.textWidth)) && (posY > this.UserTextY && posY < (this.UserTextY + this.textLenght))) {		// ver tamanhos certos dos botões
				if(Mouse.isButtonDown(0)) {
					this.Username.setFocus(true);
				}
			}
			
			if((posX > this.PassTextX && posX < (this.PassTextX + this.textWidth)) && (posY > this.PassTextY && posY < (this.PassTextY + this.textLenght))) {		// ver tamanhos certos dos botões
				if(Mouse.isButtonDown(0)) {
					this.Password.setFocus(true);
				}
			}
			
			if((posX > 300 && posX < 400) && (posY > 400 && posY < 450)) {		// ver tamanhos certos dos botões
				if(Mouse.isButtonDown(0)) {
					if(this.Pass.equals("1234") && this.User.equals("root")) {
						error_login=false;
						sbg.enterState(3);
					}
					else {
						Font font = new Font("Verdana", Font.BOLD, 20);
						TrueTypeFont trueTypeFont = new TrueTypeFont(font, true);
						error_login=true;
					}
				}
			}
			String temp = new String();
			
			if(this.Password.hasFocus()) {
				if(this.Password.getText().length() > this.Pass.length()) {
					System.out.println(this.Password.getText().length() + "  " + this.Pass.length());
					this.Pass = this.Pass + this.Password.getText().substring(this.Password.getText().length()-1,this.Password.getText().length());
				}
				else if(this.Password.getText().length() < this.Pass.length()) {
					this.Pass = this.Pass.substring(0, this.Pass.length()-1);
				}
				else {
					for(int i=0 ; i < this.Password.getText().length() ; i++)
						temp = temp + "*";
					this.Password.setText(temp);
				}
			}
	}
	

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
			float back_scale = (float) 0.1;
			float login_scale = (float) 0.5;
			
			g.drawString(this.User, 500, 10);
			g.drawString(this.Pass, 500, 30);
			g.drawString("Username", 120, 200);
			this.Username.render(gc, g);
			g.drawString("Password", 120, 300);
			this.Password.render(gc, g);
			this.Back.draw(50,50,back_scale);
			this.Login.draw(300,400,login_scale);
			
			if(error_login) {
				trueTypeFont.drawString(300, 150, "Incorrect Username or Password", Color.red);
			}
			
	}

	public int getID() {
		return 2;
	}
}