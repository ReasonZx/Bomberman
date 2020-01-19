package client;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOverState extends BasicGameState{
	private int Winner;
	private Gamestate Game;
	private KeyPressAny Input;
	protected GUI_setup sbg;
	protected boolean block;
	private int block_time=1000;
	private Font myFont;
	private Image background;
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Input = new KeyPressAny();
		sbg=(GUI_setup) arg1;
		sbg.Set_GameOver_State(getID());
		myFont=arg0.getDefaultFont();
		background=new Image("sprites/background.png");
		background=background.getScaledCopy(arg0.getWidth(), arg0.getHeight());
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Game=(Gamestate) arg1.getState(sbg.Get_Game_State());
		if(Game.getDeath()==1)
			Winner=2;
		else
			Winner=1;
		block=true;
		Timer tt = new Timer();
		tt.schedule(new Blocker(), block_time);
		arg0.getInput().clearMousePressedRecord();
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException { 
		if(block==false) {
			arg0.getInput().addKeyListener(Input);
		}
	}
	

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
		myFont.drawString(sbg.Get_Display_width()/2f-myFont.getWidth("YOU WIN PLAYER " + Winner)/2f, 
						  sbg.Get_Display_height()/2f-myFont.getHeight("YOU WIN PLAYER " + Winner)/2f, "YOU WIN PLAYER " + Winner);
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().removeKeyListener(Input);
	}

	public int getID() {
		return 4;
	}
	
	private class Blocker extends TimerTask{

		@Override
		public void run() {
			block=false;
		}
		
	}

	private class KeyPressAny implements KeyListener{
		protected boolean used=false;
		@Override
		public void inputEnded() {
			// TODO Auto-generated method stub
			used=false;
		}

		@Override
		public void inputStarted() {
			// TODO Auto-generated method stub
			used=true;
		}

		@Override
		public boolean isAcceptingInput() {
			// TODO Auto-generated method stub
			return used;
		}

		@Override
		public void setInput(org.newdawn.slick.Input arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(int arg0, char arg1) {
			
			sbg.enterState(sbg.Get_Menu_State());
		}

		@Override
		public void keyReleased(int arg0, char arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
