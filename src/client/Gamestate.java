package client;

import GameLogic.Image_Library;
import GameLogic.Layout_Logic;
import GameLogic.Map;
import GameLogic.GameLogic;
import GameLogic.Bomber;
import GameLogic.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Gamestate extends BasicGameState{

	 static GameLogic L;
	 private ArrayList<Bomber> players;
	 private Image_Library lib;
	 KeyPresses Input;
	 protected int Last_key=0;
	 private int dead;
	 private GUI_setup sbg;
	 private Image Back, Back_hover;
	 private boolean Game_Over;
	 private boolean back_h = false;
	 private boolean hovering_b = false;
	 private int backX, backY;
	 private File click_file = new File("music/click.wav");
	 private File hover_file = new File("music/hover.wav");
	 private int Map_OffsetX,Map_OffsetY;
	 private Image background;
	 private Font MyFont;
	 private Shape R1,R2,R3;
	 private Image Game_Background;
	 private Image Player1,Player2;
	 
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Input = new KeyPresses();
		sbg=(GUI_setup) arg1;
		sbg.Set_Game_State(getID());
		
		MyFont=arg0.getDefaultFont();
		
		background=new Image("sprites/background.png");
		background=background.getScaledCopy(arg0.getWidth(), arg0.getHeight());
		
		Game_Background= new Image("sprites/Game_Background.png");
		Game_Background=Game_Background.getScaledCopy(sbg.Get_GUI_Scale()*11, sbg.Get_GUI_Scale()*8);
		
		Back = new Image("sprites/back.png");
		Back = Back.getScaledCopy(0.2f);
		Back_hover = new Image("sprites/back_hover.png");
		Back_hover = Back_hover.getScaledCopy(0.2f);
	    backX = 50;
	    backY = 20;
	    
	    Map_OffsetX=(int) ((sbg.Get_Display_width()-sbg.Get_GUI_Scale()*11)/2f)-sbg.Get_GUI_Scale();
	    Map_OffsetY=(int) ((sbg.Get_Display_height()-sbg.Get_GUI_Scale()*8)/2f)-sbg.Get_GUI_Scale();
	    
	    R1=new Rectangle((Map_OffsetX+sbg.Get_GUI_Scale())/6f,arg0.getHeight()/3f,(Map_OffsetX+sbg.Get_GUI_Scale())*4/6f,arg0.getHeight()/3f);
	    R2=new Rectangle(sbg.Get_Display_width()-(Map_OffsetX+sbg.Get_GUI_Scale())*5/6f,arg0.getHeight()/3f,(Map_OffsetX+sbg.Get_GUI_Scale())*4/6f,arg0.getHeight()/3f);
	    R3=new Rectangle(Map_OffsetX+sbg.Get_GUI_Scale()*2/3,Map_OffsetY+sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*11+sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*8+sbg.Get_GUI_Scale()*2/3);
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		 lib = new Image_Library();
		 arg0.getInput().clearMousePressedRecord();
		 Layout_Logic map_gen = new Layout_Logic(lib);
		 Map m = map_gen.Generate_Standard_Map();
	     L=new GameLogic(lib,m);
	     players = new ArrayList<Bomber>();
	     int [][] Settings=sbg.Get_Settings();
	     players.add(new Bomber(1,1,lib,m,	Settings[0][0],
	    		 						Settings[0][1],
	    		 						Settings[0][2],
	    		 						Settings[0][3],
	    		 						Settings[0][4],
	    		 						Settings[0][5],
	    		 						Settings[0][6],
	    		 						players.size()));
	     
	     players.add(new Bomber(11,8,lib,m,	Settings[1][0],
										Settings[1][1],
										Settings[1][2],
										Settings[1][3],
										Settings[1][4],
										Settings[1][5],
										Settings[1][6],
										players.size()));
	     
		Player1 = new Image("sprites/D_"   + Settings[0][0] + Settings[0][1] + ".png");
		Player1=Player1.getScaledCopy(3);
		Player2 = new Image("sprites/D_"   + Settings[1][0] + Settings[1][1] + ".png");
		Player2=Player2.getScaledCopy(3);
	     
	     L.Place_Characters(players);
	     System.out.println(sbg.Get_locked_State());
	     arg0.getInput().addKeyListener(Input);
	     arg0.getInput().clearMousePressedRecord();
	     
	     Game_Over=false;
	}

	public void update(GameContainer container, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = Mouse.getX();
		int posY = sbg.Get_Display_height() - Mouse.getY();
		
		lib.Run_Changes();
		
		
		if(!Game_Over) {
			dead=L.Death_Check();
			
			if ((posX > backX && posX < backX + Back.getWidth()) && (posY > backY && posY < backY + Back.getHeight())) {
				back_h = true;
				if(hovering_b == false) {
					play_hover_sound();
					hovering_b = true;
				}
				if (Mouse.isButtonDown(0)) {
					play_click_sound();
					if(sbg.Get_locked_State())
						sbg.enterState(sbg.Get_LockedMenu_State());
					else sbg.enterState(sbg.Get_MainMenu_State());
				}
			}
			else {
				back_h = false;
				hovering_b = false;
			}
			
			if(dead!=0) {
				Game_Over=true;
				Timer tt= new Timer();
				tt.schedule(new Game_End_Delay(), 2000);
			}
			if((posX > backX && posX < backX + Back.getWidth()) && (posY > backY && posY < backY + Back.getHeight())) {		
				if(Mouse.isButtonDown(0)) {
					if(sbg.Get_locked_State())
						sbg.enterState(sbg.Get_LockedMenu_State());
					else sbg.enterState(sbg.Get_MainMenu_State());
				}
			}
		}
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().removeKeyListener(Input);
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		//g.drawString("Game state", 50, 50);
		//g.drawString(player1.toString(),100,100);
		ArrayList<Element> elements;
		g.drawImage(background, 0, 0);
		g.texture(R3,background,true);
		g.drawImage(Game_Background, Map_OffsetX+sbg.Get_GUI_Scale(), Map_OffsetY+sbg.Get_GUI_Scale());
		g.texture(R1,background,true);
		g.texture(R2, background,true);
		
		g.drawImage(Player1, R1.getCenterX()-Player1.getWidth()/2f,R1.getCenterY()-Player1.getHeight()/2f);
		g.drawImage(Player2, R2.getCenterX()-Player2.getWidth()/2f,R2.getCenterY()-Player2.getHeight()/2f);
		
		MyFont.drawString(R1.getCenterX()-MyFont.getWidth("PLAYER 1")/2f, R1.getY(), "PLAYER 1");
		MyFont.drawString(R2.getCenterX()-MyFont.getWidth("PLAYER 2")/2f, R2.getY(), "PLAYER 2");
		
		for(int x = 0 ; x < L.m.Get_RightBound() ; x++) {
			for(int y = 0 ; y < L.m.Get_BotBound() ; y++) {
					elements=L.m.Get_List_Elements(x, y);
					for(int i = 0; i < elements.size(); i++) {
						Element tmp = elements.get(i);
						
						if(tmp.Has_Image()) {
							if(tmp instanceof Bomber) {
								DrawBomber((Bomber)tmp,g);
							}
							else{
								Image im = new Image(tmp.Get_Image());
								im=im.getScaledCopy(sbg.Get_GUI_Scale(),sbg.Get_GUI_Scale());
								g.drawImage(im,
											sbg.Get_GUI_Scale()*(tmp.getX())+Map_OffsetX, 
											sbg.Get_GUI_Scale()*(tmp.getY())+Map_OffsetY);
							}
						}
					}
				}
		}
		if(back_h == false) {
			Back.draw(backX, backY);
		}
		else Back_hover.draw(backX, backY);
	}

	public int getID() {
		return 3;
	}
	
	public int getDeath() {
		return dead;
	}
	
	private class KeyPresses implements KeyListener{
		boolean usable=true;
		@Override
		public void inputEnded() {
			// TODO Auto-generated method stub
			usable=false;
		}

		@Override
		public void inputStarted() {
			// TODO Auto-generated method stub
			usable = true;
		}

		@Override
		public boolean isAcceptingInput() {
			// TODO Auto-generated method stub
			return usable;
		}
		
		public void setAcceptingInput(boolean x) {
			// TODO Auto-generated method stub
			usable=x;
		}

		@Override
		public void setInput(org.newdawn.slick.Input arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(int key, char c) {
			// TODO Auto-generated method stub
			//System.out.println("KEYPRESSED");
			
			if(L.Death_Check()==0)
				L.Action(key,0);
			
			Timer tt = new Timer();
			tt.schedule(new KeyTimer(), 100);
			
			this.setAcceptingInput(false);
		}

		@Override
		public void keyReleased(int key, char c) {
			// TODO Auto-generated method stub
		}
		
	}
	
	private class KeyTimer extends TimerTask{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Input.setAcceptingInput(true);
		}
		
	}
	
	private void DrawBomber(Bomber x,Graphics g) throws SlickException {
		String tmp=x.Get_Image();
		int sett[][]=sbg.Get_Settings();
		Image img;
		
		switch(tmp) {
		case "StopDown":
			img= new Image("sprites/D_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "StopLeft":
			img= new Image("sprites/D_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(90);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "StopUp":
			img= new Image("sprites/D_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(180);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "StopRight":
			img= new Image("sprites/D_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(270);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "Down1":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "Down2":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "Left1":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(90);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "Left2":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img = img.getFlippedCopy(true,false);
			img.setRotation(90);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "Up1":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(180);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "Up2":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img = img.getFlippedCopy(true,false);
			img.setRotation(180);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "Right1":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(270);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "Right2":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img = img.getFlippedCopy(true,false);
			img.setRotation(270);
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX() + sbg.Get_GUI_Scale()*x.Get_OffsetX()+(sbg.Get_GUI_Scale()-img.getWidth())/2f, 
					Map_OffsetY+sbg.Get_GUI_Scale()*(x.getY() + x.Get_OffsetY())+(sbg.Get_GUI_Scale()-img.getHeight())/2f);
			break;
		case "sprites/blood.png":
			img= new Image(tmp);
			img=img.getScaledCopy(sbg.Get_GUI_Scale(),sbg.Get_GUI_Scale());
			g.drawImage(img,Map_OffsetX+sbg.Get_GUI_Scale()*x.getX(),Map_OffsetY+sbg.Get_GUI_Scale()*x.getY());
			break;
		}
	}
	
	public void play_hover_sound() {
		
		AudioInputStream hover_sound;
	
		try {
			hover_sound = AudioSystem.getAudioInputStream(hover_file);
			Clip hover_s = AudioSystem.getClip(null);
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
			Clip click_s = AudioSystem.getClip(null);
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
	
	private class Game_End_Delay extends TimerTask{

		@Override
		public void run() {
			sbg.enterState(sbg.Get_GameOver_State());		//Go to Game Over
		}
		
	}
	
}
