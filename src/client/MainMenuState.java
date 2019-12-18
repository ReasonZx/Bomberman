package client;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicGameState{

	private Image Play_local;
	private Image Controls;
	private Image Logout;
	private Image Friends;
	private Image Settings;
	private Image Play_online;
	private GUI_setup sbg;
	private int playl_x;
	private int playl_y;
	private int controls_x;
	private int controls_y;
	private int logout_x;
	private int logout_y;
	private int friends_x;
	private int friends_y;
	private int settings_x;
	private int settings_y;
	private int playo_x;
	private int playo_y;
    protected String server_response;	
	private boolean looking=false;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		sbg=(GUI_setup) arg1;
		sbg.Set_MainMenu_State(getID());
		
		Controls = new Image("sprites/Button_Controls.png");
		Controls = Controls.getScaledCopy(0.4f);
		
		Play_local = new Image("sprites/play_local.png");
		Play_local = Play_local.getScaledCopy(0.4f);
		
		Logout = new Image("sprites/logout.png");
		Logout = Logout.getScaledCopy(0.4f);
		
		Friends = new Image("sprites/friends.png");
		Friends = Friends.getScaledCopy(0.4f);
		
		Settings = new Image("sprites/settings.png");
		Settings = Settings.getScaledCopy(0.4f);
		
		Play_online = new Image("sprites/play_online.png");
		Play_online = Play_online.getScaledCopy(0.4f);

		playl_x = (int) (0.40* (float)sbg.Get_Display_width() - Play_local.getWidth()/2);
		playl_y = (int) (0.35* (float)sbg.Get_Display_height());
		controls_x = (int) (0.50* (float)sbg.Get_Display_width() - Controls.getWidth()/2);
		controls_y = (int) (0.50* (float)sbg.Get_Display_height());
		logout_x = (int) (0.50* (float)sbg.Get_Display_width() - Logout.getWidth()/2);
		logout_y = (int) (0.65* (float)sbg.Get_Display_height());
		friends_x = (int) (0.15* (float)sbg.Get_Display_width() - Friends.getWidth()/2);
		friends_y = (int) (0.15* (float)sbg.Get_Display_height());
		settings_x = (int) (0.85* (float)sbg.Get_Display_width() - Settings.getWidth()/2);
		settings_y = (int) (0.15* (float)sbg.Get_Display_height());
		playo_x = (int) (0.60* (float)sbg.Get_Display_width() - Settings.getWidth()/2);
		playo_y = (int) (0.35* (float)sbg.Get_Display_height());
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
		
		if(!looking) {	
			if((posX > playl_x && posX < playl_x + Play_local.getWidth()) && (posY > playl_y && posY < playl_y + Play_local.getHeight())) {		// ver tamanhos certos dos bot�es	
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
		
		if((posX > logout_x && posX < logout_x + Logout.getWidth()) && (posY > logout_y && posY < logout_y + Logout.getHeight())) {	// ver tamanhos certos dos bot�es
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Menu_State());
			}
		}
		
		if((posX > friends_x && posX < friends_x + Friends.getWidth()) && (posY > friends_y && posY < friends_y + Friends.getHeight())) {	// ver tamanhos certos dos bot�es
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Friends_State());
			}
		}
			
		if((posX > playo_x && posX < playo_x + Play_online.getWidth()) && (posY > playo_y && posY < playo_y + Play_online.getHeight())) {	// ver tamanhos certos dos bot�es
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			}
		}
			
		if((posX > settings_x && posX < settings_x + Settings.getWidth()) && (posY > settings_y && posY < settings_y + Settings.getHeight())) {	// ver tamanhos certos dos bot�es
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				
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
		Play_local.draw(playl_x,playl_y);
		Play_online.draw(playo_x,playo_y);
		Controls.draw(controls_x,controls_y);
		Logout.draw(logout_x,logout_y);
		Settings.draw(settings_x,settings_y);
		Friends.draw(friends_x,friends_y);
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
	}

	@Override
	public int getID() {
		return 5;
	}
	
}
