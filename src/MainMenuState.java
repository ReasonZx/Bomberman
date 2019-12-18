
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicGameState{

	private Image Play;
	private Image Controls;
	private Image Logout;
	protected GUI_setup sbg;
	private int play_x;
	private int play_y;
	private int controls_x;
	private int controls_y;
	private int logout_x;
	private int logout_y;
	protected String server_response;
	private boolean looking=false;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		sbg=(GUI_setup) arg1;
		sbg.Set_MainMenu_State(getID());
		
		Controls = new Image("sprites/Button_Controls.png");
		Controls = Controls.getScaledCopy(0.4f);
		Play = new Image("sprites/play.png");
		Play = Play.getScaledCopy(0.4f);
		
		Logout = new Image("sprites/logout.png");
		Logout = Logout.getScaledCopy(0.4f);

		play_x = (int) (0.43* (float)sbg.Get_Display_width());
		play_y = (int) (0.35* (float)sbg.Get_Display_height());
		controls_x = (int) (0.43* (float)sbg.Get_Display_width());
		controls_y = (int) (0.50* (float)sbg.Get_Display_height());
		logout_x = (int) (0.43* (float)sbg.Get_Display_width());
		logout_y = (int) (0.65* (float)sbg.Get_Display_height());
		
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
		
		if(!looking) {
			if((posX > play_x && posX < play_x + Play.getWidth()) && (posY > play_y && posY < play_y + Play.getHeight())) {		// ver tamanhos certos dos bot�es
				if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					try {
						server_response = sbg.server.request("looking_start");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(server_response.equals("OK"))
						looking=true;
				}
			}
			
			if((posX > controls_x && posX < controls_x + Controls.getWidth()) && (posY > controls_y && posY < controls_y + Controls.getHeight())) {	// ver tamanhos certos dos bot�es
				if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					sbg.enterState(sbg.Get_Controls_State());
				}
			}
			
			if((posX > logout_x && posX < logout_x + Logout.getWidth()) && (posY > logout_y && posY < logout_y + Logout.getHeight())) {	// ver tamanhos certos dos bot�es
				if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					sbg.enterState(sbg.Get_Menu_State());
				}
			}
		}
		else {
			try {
				server_response=sbg.server.poll();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(server_response!=null)
				if(server_response.equals("game_found")) {
				looking=false;
				sbg.enterState(sbg.Get_OnlineGame_State());
			}
			
		}
	}
	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		Play.draw(play_x,play_y);
		Controls.draw(controls_x,controls_y);
		Logout.draw(logout_x,logout_y);
		
		if(looking) {
			
		}
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
	}

	@Override
	public int getID() {
		return 5;
	}
}
