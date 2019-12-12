import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import java.awt.Font;


public class LogIn extends BasicGameState{
	
    private TextField Username;
    private TextField Password;
	private org.newdawn.slick.Font trueTypeFont;
	private String User;								//For hardcoded login
	private String Pass;								//For hardcoded login
	private Image Back;
	private Image Login;
	private int login_x, login_y;
	private int UserTextX, UserTextY;
	private int PassTextX, PassTextY;
	private int backX, backY;
	private boolean error_login = false;
	private GUI_setup sbg;


	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
		  sbg=(GUI_setup) arg1;
		  sbg.Set_Login_State(getID());
		  Font font = new Font("Calibri", Font.PLAIN, 15);
		  trueTypeFont = new TrueTypeFont(font, true);
		 
		  
		  UserTextX = 200;
		  UserTextY = 200;
	      
	      Username = new TextField(gc, this.trueTypeFont, UserTextX, UserTextY, 400 , 20);
	      Username.setBackgroundColor(Color.white);
	      Username.setBorderColor(Color.white);
	      Username.setTextColor(Color.black);
	      
	      PassTextX = 200;
	      PassTextY = 300;
	      
	      Password = new TextField(gc, trueTypeFont, PassTextX, PassTextY, 400 , 20);
	      Password.setBackgroundColor(Color.white);
	      Password.setBorderColor(Color.white);
	      Password.setTextColor(Color.black);
	      
	      Pass = new String();
	      User = new String();
	      
	      Back = new Image("sprites/back.png");
	      Login = new Image("sprites/logIn.png");
	      
	      backX = 50;
	      backY = 50;
	      
	      login_x = 300;
	      login_y = 400;
	}
	
	@Override
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		arg0.getInput().clearMousePressedRecord();
		Username.setAcceptingInput(true);
		Password.setAcceptingInput(true);
	}

	public void update(GameContainer gc, StateBasedGame arg1, int delta) throws SlickException {
			//delta = 60;
			//this.Pass = this.Password.getText();
			User = this.Username.getText();
			
			int posX = Mouse.getX();
			int posY = sbg.Get_Display_height() - Mouse.getY();
			if((posX > backX && posX < backX + Back.getWidth()) && (posY > backY && posY < backY + Back.getHeight())) {		// ver tamanhos certos dos bot�es	//go back
				if(Mouse.isButtonDown(0)) {
					sbg.enterState(sbg.Get_Menu_State());
				}
			}
			
			if((posX > UserTextX && posX < (UserTextX + Username.getWidth())) && (posY > UserTextY && posY < (UserTextY + Username.getHeight()))) {		// ver tamanhos certos dos bot�es
				if(Mouse.isButtonDown(0)) {
					this.Username.setFocus(true);
				}
			}
			
			if((posX > PassTextX && posX < (PassTextX + Password.getWidth())) && (posY > PassTextY && posY < (PassTextY + Password.getHeight()))) {		// ver tamanhos certos dos bot�es
				if(Mouse.isButtonDown(0)) {
					this.Password.setFocus(true);
				}
			}
			
			if((posX > login_x && posX < login_x + Login.getWidth()) && (posY > login_y && posY < login_y + Login.getHeight())) {		// ver tamanhos certos dos bot�es
				if(Mouse.isButtonDown(0)) {
					if(Pass.equals("1234") && User.equals("root")) {
						error_login=false;
						sbg.enterState(sbg.Get_MainMenu_State());
					}
					else {
						error_login=true;
					}
				}
			}
			
			if(this.Password.hasFocus() && gc.getInput().isKeyPressed(15)) {
				this.Username.setFocus(true);
			}
			
			
			if(this.Username.hasFocus() && gc.getInput().isKeyPressed(15)) {
				this.Password.setFocus(true);
			}
			
			if(this.Password.hasFocus() && gc.getInput().isKeyPressed(28)) {
				if(this.Pass.equals("1234") && this.User.equals("root")) {
					error_login=false;
					this.Password.setFocus(false);
					sbg.enterState(sbg.Get_MainMenu_State());
				}
				else {
					error_login=true;
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
	

	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
			//g.drawString(this.User, 500, 10);
			//g.drawString(this.Pass, 500, 30);
			g.drawString("Username", UserTextX, UserTextY);
			Username.render(gc, g);
			g.drawString("Password", PassTextX, PassTextY);
			Password.render(gc, g);
			Back.draw(backX,backY);
			Login.draw(login_x,login_y);
			
			if(error_login) {
				trueTypeFont.drawString(300, 150, "Incorrect Username or Password", Color.red);
			}
			
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		arg0.getInput().clearMousePressedRecord();
		Username.setAcceptingInput(false);
		Password.setAcceptingInput(false);
	}

	public int getID() {
		return 2;
	}
}