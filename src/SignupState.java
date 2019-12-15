import org.lwjgl.input.Mouse;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;


public class SignupState extends BasicGameState{

	private GUI_setup sbg;
	private TextField Username;
	private TextField Password;
	private TextField Email;
	private TextField Password_repeat;
    private Font myFont;
    private Image Back;
    private Image signup;
    private int signup_x,signup_y;
	
	@Override
	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
		sbg=(GUI_setup) arg1;
		sbg.Set_Signup_State(getID());
		myFont = gc.getDefaultFont();
		signup = new Image("sprites/signup.png");
		signup = signup.getScaledCopy(0.5f);
		signup_x = (int) ((float) sbg.Get_Display_width() * 0.45);
		signup_y = (int) ((float) sbg.Get_Display_height() * 0.75);
		
		
		Username = new TextField(gc, myFont,(int) ((float) sbg.Get_Display_width() * 0.40), (int) ((float) sbg.Get_Display_height() * 0.30), 400, 20);
		Username.setBackgroundColor(Color.white);
	    Username.setBorderColor(Color.white);
	    Username.setTextColor(Color.black);
	    
		Password = new TextField(gc, myFont,(int) ((float) sbg.Get_Display_width() * 0.40), (int) ((float) sbg.Get_Display_height() * 0.40), 400, 20);
		Password.setBackgroundColor(Color.white);
		Password.setBorderColor(Color.white);
		Password.setTextColor(Color.black);
	    
		Email = new TextField(gc, myFont,(int) ((float) sbg.Get_Display_width() * 0.40), (int) ((float) sbg.Get_Display_height() * 0.50), 400, 20);
		Email.setBackgroundColor(Color.white);
		Email.setBorderColor(Color.white);
		Email.setTextColor(Color.black);
	    
		Password_repeat = new TextField(gc, myFont,(int) ((float) sbg.Get_Display_width() * 0.40), (int) ((float) sbg.Get_Display_height() * 0.60), 400, 20);
		Password_repeat.setBackgroundColor(Color.white);
		Password_repeat.setBorderColor(Color.white);
		Password_repeat.setTextColor(Color.black);
		
		Back = new Image("sprites/back.png");
		Back = Back.getScaledCopy(0.2f);
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

	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
				
		if((posX > 50 && posX < 50 + Back.getWidth()) && (posY > 50 && posY < 50 + Back.getHeight())) {		// ver tamanhos certos dos bot�es	//go back
			if(arg0.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				//sbg.enterState(sbg.Get_Menu_State());
				sbg.enterState(sbg.Get_Menu_State(), new FadeOutTransition() , new FadeInTransition());
			}
		}
		
		if((posX > signup_x && posX < signup_x + signup.getWidth()) && (posY > signup_y && posY < signup_y + signup.getHeight())) {		// ver tamanhos certos dos bot�es	//go back
			if(arg0.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Menu_State(), new FadeOutTransition() , new FadeInTransition());
			}
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("Username:",(int) ((float) sbg.Get_Display_width() * 0.25) , (int) ((float) sbg.Get_Display_height() * 0.30));
		g.drawString("Email:",(int) ((float) sbg.Get_Display_width() * 0.25) , (int) ((float) sbg.Get_Display_height() * 0.40));
		g.drawString("Password:", (int) ((float) sbg.Get_Display_width() * 0.25) , (int) ((float) sbg.Get_Display_height() * 0.50));
		g.drawString("Repeat Password:",(int) ((float) sbg.Get_Display_width() * 0.25) , (int) ((float) sbg.Get_Display_height() * 0.60));
		Username.render(gc, g);
		Password.render(gc, g);
		Email.render(gc, g);
		Password_repeat.render(gc, g);
		Back.draw(50,50);
		signup.draw(signup_x , signup_y);
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Username.setAcceptingInput(false);
		Password.setAcceptingInput(false);
		Password_repeat.setAcceptingInput(false);
		Email.setAcceptingInput(false);
		arg0.getInput().clearMousePressedRecord();
	}
	
	@Override
	public int getID() {
		return 7;
	}

}
