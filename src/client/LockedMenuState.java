package client;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LockedMenuState extends BasicGameState{

	private Image background;
	private Image bomberman_title;
	private Image Play_local,Play_local_hover;
	private Image Statistics;
	private Image Logout, Logout_hover;
	private Image Friends;
	private Image Settings, Settings_hover;
	private Image Play_online;
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
	protected String server_response;	
	private boolean logout_h = false, playl_h = false;
	private boolean settings_h = false;
	private boolean hovering_l = false, hovering_pl = false;
	private boolean hovering_se = false;
	private File click_file = new File("music/click.wav");
	private File hover_file = new File("music/hover.wav");
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		sbg=(GUI_setup) arg1;
		sbg.Set_LockedMenu_State(getID());
		
		arg0.getDefaultFont();
		
		background = new Image("sprites/background.png");
		bomberman_title = new Image("sprites/bomberman_title.png");
		
		Statistics = new Image("sprites/statistics_locked.png");
		Statistics = Statistics.getScaledCopy(0.4f);
		
		Play_local = new Image("sprites/play_local.png");
		Play_local = Play_local.getScaledCopy(0.4f);
		Play_local_hover = new Image("sprites/play_local_hover.png");
		Play_local_hover = Play_local_hover.getScaledCopy(0.4f);
		
		Logout = new Image("sprites/exitGame.png");
		Logout = Logout.getScaledCopy(0.4f);
		Logout_hover = new Image("sprites/exitGame_hover.png");
		Logout_hover = Logout_hover.getScaledCopy(0.4f);
		
		Friends = new Image("sprites/friends_locked.png");
		Friends = Friends.getScaledCopy(0.4f);
		
		Settings = new Image("sprites/settings.png");
		Settings = Settings.getScaledCopy(0.4f);
		Settings_hover = new Image("sprites/settings_hover.png");
		Settings_hover = Settings_hover.getScaledCopy(0.4f);
		
		Play_online = new Image("sprites/play_online_locked.png");
		Play_online = Play_online.getScaledCopy(0.4f);


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
		bomberman_x = (int) ((float) sbg.Get_Display_width() * 0.50 - bomberman_title.getWidth() / 2);
		bomberman_y = (int) ((float) sbg.Get_Display_height() * 0.75);
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
		
		//PLAY LOCAL BUTTON PRESSED?
		if((posX > playl_x && posX < playl_x + Play_local.getWidth()) && (posY > playl_y && posY < playl_y + Play_local.getHeight())) {
			playl_h = true;
			if(hovering_pl == false) {
				play_hover_sound();
				hovering_pl = true;
			}
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				play_click_sound();
				sbg.enterState(sbg.Get_Game_State());
			}
		}
		else {
			playl_h = false;
			hovering_pl = false;
		}
		
		//LOGOUT BUTTON PRESSED?
		if((posX > logout_x && posX < logout_x + Logout.getWidth()) && (posY > logout_y && posY < logout_y + Logout.getHeight())) {
			logout_h = true;
			if(hovering_l == false) {
				play_hover_sound();
				hovering_l = true;
			}
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				play_click_sound();
				System.exit(0);
			}
		}
		else {
			hovering_l = false;
			logout_h = false;
		}
		
		//SETTINGS BUTTON PRESSED?
		if((posX > settings_x && posX < settings_x + Settings.getWidth()) && (posY > settings_y && posY < settings_y + Settings.getHeight())) {
			settings_h = true;
			if(hovering_se == false) {
				play_hover_sound();
				hovering_se = true;
			}
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				play_click_sound();
				System.out.println(sbg.Get_LockedSettings_State());
				sbg.enterState(sbg.Get_LockedSettings_State());
			}
		}
		else {
			settings_h = false;
			hovering_se = false;
		}
	}
	
	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		background.draw(0,0);
		bomberman_title.draw(bomberman_x,bomberman_y);
		
		if(playl_h == false) {
			Play_local.draw(playl_x,playl_y);
		}
		else Play_local_hover.draw(playl_x,playl_y);
		
		Play_online.draw(playo_x,playo_y);

		Statistics.draw(statistics_x,statistics_y);
		
		if(logout_h == false) {
			Logout.draw(logout_x,logout_y);
		}
		else Logout_hover.draw(logout_x,logout_y);
		
		if(settings_h == false) {
			Settings.draw(settings_x,settings_y);
		}
		else Settings_hover.draw(settings_x,settings_y);
		
		Friends.draw(friends_x,friends_y);
		
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
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
		return 12;
	}
	
}
