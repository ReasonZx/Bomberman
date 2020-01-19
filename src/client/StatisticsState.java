package client;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.awt.Font;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StatisticsState extends BasicGameState {

	private Image Back, Back_hover;
	private Image background;
	private int backX, backY;
	private int games_won = 1, games_played = 1, n_friends = 1;
	private boolean back_h = false;
	private boolean hovering_b = false;
	private GUI_setup sbg;
	private String server_response;
	private File click_file = new File("music/click.wav");
	private File hover_file = new File("music/hover.wav");

	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
		sbg = (GUI_setup) arg1;
		sbg.Set_Statistics_State(getID());
		
		Back = new Image("sprites/back.png");
		Back = Back.getScaledCopy(0.2f);
		Back_hover = new Image("sprites/back_hover.png");
		Back_hover = Back_hover.getScaledCopy(0.2f);
		
		backX = (int) (sbg.Get_Display_width() * 0.05);
		backY = (int) (sbg.Get_Display_height() * 0.05);
		
		background = new Image("sprites/background.png");	
		background=background.getScaledCopy(gc.getWidth(),gc.getHeight());		
	}

	@Override
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
		server_response = "";
		
		try {
			String request = "statistics";
			server_response = sbg.server.request(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String words[] = server_response.split("_");
		
		games_won = Integer.parseInt(words[0]);
		games_played = Integer.parseInt(words[1]);
		n_friends = Integer.parseInt(words[2]);
				
	}

	public void update(GameContainer gc, StateBasedGame arg1, int delta) throws SlickException {
		int posX = gc.getInput().getMouseX();
		int posY = gc.getInput().getMouseY();

		if ((posX > backX && posX < backX + Back.getWidth()) && (posY > backY && posY < backY + Back.getHeight())) {
			back_h = true;
			if(hovering_b == false) {
				play_hover_sound();
				hovering_b = true;
			}
			if (Mouse.isButtonDown(0)) {
				play_click_sound();
				sbg.enterState(sbg.Get_MainMenu_State());
			}
		}
		else {
			back_h = false;
			hovering_b = false;
		}
		

		
	}

	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		background.draw(0,0);
		
		
	    Font pulsingFont = new Font("Verdana", Font.BOLD, 26);
	    TrueTypeFont pulsing = new TrueTypeFont(pulsingFont, true);
	    g.setFont(pulsing);
	    
	    g.drawString("NUMBER OF GAMES PLAYED:", (float) (sbg.Get_Display_width() * 0.20), (float) (sbg.Get_Display_height() * 0.32));
	    g.drawString( "NUMBER OF GAMES WON:", (float) (sbg.Get_Display_width() * 0.20), (float) (sbg.Get_Display_height() * 0.47));
	    g.drawString("WIN/LOSS RATIO:", (float) (sbg.Get_Display_width() * 0.20), (float) (sbg.Get_Display_height() * 0.62));
	    g.drawString("NUMBER OF FRIENDS:", (float) (sbg.Get_Display_width() * 0.20), (float) (sbg.Get_Display_height() * 0.77));
	    
	    pulsingFont.deriveFont(Font.PLAIN);
	    g.setFont(pulsing);
	    
	    g.drawString(Integer.toString(games_played), (float) (sbg.Get_Display_width() * 0.65), (float) (sbg.Get_Display_height() * 0.32));
	    g.drawString(Integer.toString(games_won), (float) (sbg.Get_Display_width() * 0.65), (float) (sbg.Get_Display_height() * 0.47));
	    DecimalFormat df = new DecimalFormat();
	    df.setMaximumFractionDigits(1);
	    g.drawString(df.format((float) ((float)games_won/(float)games_played)*100) + "%", (float) (sbg.Get_Display_width() * 0.65), (float) (sbg.Get_Display_height() * 0.62));
	    g.drawString(Integer.toString(n_friends), (float) (sbg.Get_Display_width() * 0.65), (float) (sbg.Get_Display_height() * 0.77));

	    
		if(back_h == false) {
			Back.draw(backX, backY);
		}
		else Back_hover.draw(backX, backY);

	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
		server_response = "";
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

	public int getID() {
		return 13;
	}
}