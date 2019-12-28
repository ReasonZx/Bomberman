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
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainMenuState extends BasicGameState{

	private Image background;
	private Image bomberman_title;
	private Image Play_local,Play_local_hover;
	private Image Statistics;
	private Image Logout;
	private Image Friends;
	private Image Settings;
	private Image Play_online;
	private Image Cancel;
	private Image Accept;
	private Image Decline;
	private GUI_setup sbg;
	private int bomberman_x, bomberman_y;
	private int playl_x;
	private int playl_y;
	private int statistics_x;
	private int statistics_y;
	private int logout_x;
	private int logout_y;
	private int friends_x;
	private int friends_y;
	private int settings_x;
	private int settings_y;
	private int playo_x;
	private int playo_y;
	private int cancel_x;
	private int cancel_y;
	private int accept_x;
	private int accept_y;
	private int decline_x;
	private int decline_y;
    protected String server_response;	
	private boolean looking=false;
	private boolean Request=false;
	private Rectangle R1;
	private Font myFont;
	private String FriendName;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		sbg=(GUI_setup) arg1;
		sbg.Set_MainMenu_State(getID());
		R1 = new Rectangle(0,arg0.getHeight()/3f,arg0.getWidth(),arg0.getHeight()/3f);
		
		myFont = arg0.getDefaultFont();
		
		background = new Image("sprites/background.png");
		bomberman_title = new Image("sprites/bomberman_title.png");
		
		Statistics = new Image("sprites/statistics.png");
		Statistics = Statistics.getScaledCopy(0.4f);
		
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
		
		Cancel = new Image("sprites/back.png");
		Cancel = Cancel.getScaledCopy(0.3f);
		
		Accept = new Image("sprites/accept.png");
		Accept = Accept.getScaledCopy(0.3f);
		
		Decline = new Image("sprites/decline.png");
		Decline = Decline.getScaledCopy(0.3f);

		playl_x = (int) (0.12* (float)sbg.Get_Display_width() - Play_local.getWidth()/2);
		playl_y = (int) (0.40* (float)sbg.Get_Display_height());
		statistics_x = (int) (0.88* (float)sbg.Get_Display_width() - Statistics.getWidth()/2);
		statistics_y = (int) (0.60* (float)sbg.Get_Display_height());
		logout_x = (int) (0.12* (float)sbg.Get_Display_width() - Logout.getWidth()/2);
		logout_y = (int) (0.60* (float)sbg.Get_Display_height());
		friends_x = (int) (0.88* (float)sbg.Get_Display_width() - Friends.getWidth()/2);
		friends_y = (int) (0.40* (float)sbg.Get_Display_height());
		settings_x = (int) (0.88* (float)sbg.Get_Display_width() - Settings.getWidth()/2);
		settings_y = (int) (0.50* (float)sbg.Get_Display_height());
		playo_x = (int) (0.12* (float)sbg.Get_Display_width() - Settings.getWidth()/2);
		playo_y = (int) (0.50* (float)sbg.Get_Display_height());
		cancel_x=(int) (R1.getX()+R1.getWidth()/2f-Cancel.getWidth()/2f);
		cancel_y=(int) (((R1.getY()+R1.getHeight())-(R1.getHeight()-(R1.getHeight()/3f + myFont.getHeight("LOOKING FOR A GAME")/2f))/2f)-Cancel.getHeight()/2f);
		accept_x=(int) (R1.getX()+R1.getWidth()/3f-Cancel.getWidth()/2f);
		accept_y=(int) (((R1.getY()+R1.getHeight())-(R1.getHeight()-(R1.getHeight()/3f + myFont.getHeight("LOOKING FOR A GAME")/2f))/2f)-Cancel.getHeight()/2f);
		decline_x=(int) (R1.getX()+R1.getWidth()/3f*2f-Cancel.getWidth()/2f);
		decline_y=(int) (((R1.getY()+R1.getHeight())-(R1.getHeight()-(R1.getHeight()/3f + myFont.getHeight("LOOKING FOR A GAME")/2f))/2f)-Cancel.getHeight()/2f);
		bomberman_x = (int) ((float) sbg.Get_Display_width() * 0.50 - bomberman_title.getWidth() / 2);
		bomberman_y = (int) ((float) sbg.Get_Display_height() * 0.75);
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
		Request=false;
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
		try {
			server_response=sbg.server.poll();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(Request) {
			
			if(server_response!=null)	
				if(server_response.equals("friends_invited_cancel_"+FriendName)) {	
				Request=false;
			}
            
			//DECLINE BUTTON PRESSED
            if((posX > decline_x && posX < decline_x + Cancel.getWidth()) && (posY > decline_y && posY < decline_y + Cancel.getHeight())) {	
    			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
    				try {	
						server_response = sbg.server.request("friends_invited_decline_"+FriendName);	
					} catch (IOException e) {	
						// TODO Auto-generated catch block	
						e.printStackTrace();	
					}	
    				if(server_response!=null)
						if(server_response.equals("friends_invited_OK"))	
							Request=false;	
    			}
    		}
            
          //ACCEPT BUTTON PRESSED
            if((posX > accept_x && posX < accept_y + Cancel.getWidth()) && (posY > accept_x && posY < accept_y + Cancel.getHeight())) {	
    			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
    				try {	
						server_response = sbg.server.request("friends_invited_accept_"+FriendName);	
					} catch (IOException e) {	
						// TODO Auto-generated catch block	
						e.printStackTrace();	
					}	
    				if(server_response!=null)
						if(server_response.equals("friends_invited_OK"))	
							sbg.enterState(sbg.Get_OnlineGame_State());
						else
							Request=false;
    			}
    		}
			
		}
		else
			if(!looking) {
				
				if(server_response!=null) {
					String tmp[]=server_response.split("_");
					if(tmp.length>=3){
						if(tmp[0].equals("friends") && tmp[1].equals("invited")){
							Request=true;
							FriendName=tmp[2];
							sbg.server.send("friends_invited_get_"+FriendName);
						}
					}
				}
				
				//PLAY LOCAL BUTTON PRESSED?
				if((posX > playl_x && posX < playl_x + Play_local.getWidth()) && (posY > playl_y && posY < playl_y + Play_local.getHeight())) {		// ver tamanhos certos dos bot�es	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {	
						try {	
							server_response = sbg.server.request("looking_start");
						} catch (IOException e) {	
							// TODO Auto-generated catch block	
							e.printStackTrace();	
						}
						if(server_response!=null)
							if(server_response.equals("looking_OK"))	
								looking=true;	
					}	
				}
	        
				//SETTINGS BUTTON PRESSED?
				if((posX > statistics_x && posX < statistics_x + Statistics.getWidth()) && (posY > statistics_y && posY < statistics_y + Statistics.getHeight())) {	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						sbg.enterState(sbg.Get_Controls_State());
					}
				}
				
				//LOGOUT BUTTON PRESSED?
				if((posX > logout_x && posX < logout_x + Logout.getWidth()) && (posY > logout_y && posY < logout_y + Logout.getHeight())) {	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						sbg.enterState(sbg.Get_Menu_State());
					}
				}
				
				//FRIENDS BUTTON PRESSED?
				if((posX > friends_x && posX < friends_x + Friends.getWidth()) && (posY > friends_y && posY < friends_y + Friends.getHeight())) {	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						sbg.enterState(sbg.Get_Friends_State());
					}
				}
				
				//PLAY ONLINE BUTTON PRESSED?
				if((posX > playo_x && posX < playo_x + Play_online.getWidth()) && (posY > playo_y && posY < playo_y + Play_online.getHeight())) {	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					}
				}
				
				//SETTINGS BUTTON PRESSED?
				if((posX > settings_x && posX < settings_x + Settings.getWidth()) && (posY > settings_y && posY < settings_y + Settings.getHeight())) {	
					if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						
					}
				}
	        }
	        else {	
	            if(server_response!=null)	
					if(server_response.equals("game_found")) {	
					looking=false;	
					sbg.enterState(sbg.Get_OnlineGame_State());	
				}
	            
	            if((posX > cancel_x && posX < cancel_x + Cancel.getWidth()) && (posY > cancel_y && posY < cancel_y + Cancel.getHeight())) {	
	    			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
	    				try {	
							server_response = sbg.server.request("looking_cancel");	
						} catch (IOException e) {	
							// TODO Auto-generated catch block	
							e.printStackTrace();	
						}	
	    				if(server_response!=null)
							if(server_response.equals("looking_OK"))	
								looking=false;	
	    			}
	    		}
	        }
	}
	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		background.draw(0,0);
		bomberman_title.draw(bomberman_x,bomberman_y);
		Play_local.draw(playl_x,playl_y);
		Play_online.draw(playo_x,playo_y);
		Statistics.draw(statistics_x,statistics_y);
		Logout.draw(logout_x,logout_y);
		Settings.draw(settings_x,settings_y);
		Friends.draw(friends_x,friends_y);
		
		if(Request){
			arg2.setColor(Color.white);
			arg2.fill(R1);
			myFont.drawString(	R1.getX()+R1.getWidth()/2f - myFont.getWidth("YOU WERE INVITED FOR A GAME BY "+FriendName)/2f,
					R1.getY()+R1.getHeight()/3f - myFont.getHeight("YOU WERE INVITED FOR A GAME BY "+FriendName)/2f,
					"YOU WERE INVITED FOR A GAME BY "+FriendName,Color.black);
			Accept.draw(accept_x,accept_y);
			Decline.draw(decline_x,decline_y);
		}
		else
			if(looking){
				arg2.setColor(Color.white);
				arg2.fill(R1);
				myFont.drawString(	R1.getX()+R1.getWidth()/2f - myFont.getWidth("LOOKING FOR A GAME")/2f,
									R1.getY()+R1.getHeight()/3f - myFont.getHeight("LOOKING FOR A GAME")/2f,
									"LOOKING FOR A GAME",Color.black);
				Cancel.draw(cancel_x,cancel_y);
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
