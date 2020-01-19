package client;

import GameLogic.Image_Library;
import GameLogic.Layout_Logic;
import GameLogic.Map;
import GameLogic.GameLogic;
import GameLogic.Bomber;

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

public class OnlineGameState extends BasicGameState
{
	 static GameLogic L;
	 private ArrayList<Bomber> players;
	 private Image_Library lib;
	 private KeyPresses InputKey;
	 protected int Last_key=0;
	 private int dead;
	 private GUI_setup sbg;
	 private boolean init=false;
	 private String server_response;
	 private Map m;
	 private boolean won;
	 private int winner;
	 private File click_file = new File("music/click.wav");
	 private File hover_file = new File("music/hover.wav");
	 private int Map_OffsetX,Map_OffsetY;
	 private Shape R1,R2,R3,R4,R5;
	 private Image Game_Background;
	 private Image background;
	 private Image Player1,Player2,Player3,Player4;
	 private ArrayList<String[]> PlayerList;
	 private Font MyFont;
	 
	 public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		InputKey = new KeyPresses();
		sbg=(GUI_setup) arg1;
		sbg.Set_OnlineGame_State(getID());
		
		MyFont=arg0.getDefaultFont();
		
		background=new Image("sprites/background.png");
		background=background.getScaledCopy(arg0.getWidth(), arg0.getHeight());
		
		Game_Background= new Image("sprites/Game_Background.png");
		Game_Background=Game_Background.getScaledCopy(sbg.Get_GUI_Scale()*11, sbg.Get_GUI_Scale()*8);
		
		 Map_OffsetX=(int) ((sbg.Get_Display_width()-sbg.Get_GUI_Scale()*11)/2f)-sbg.Get_GUI_Scale();
		 Map_OffsetY=(int) ((sbg.Get_Display_height()-sbg.Get_GUI_Scale()*8)/2f)-sbg.Get_GUI_Scale();
		 
		  R1=new Rectangle((Map_OffsetX+sbg.Get_GUI_Scale())/6f,arg0.getHeight()/10f,(Map_OffsetX+sbg.Get_GUI_Scale())*4/6f,arg0.getHeight()*3/10f);
		  R2=new Rectangle(sbg.Get_Display_width()-(Map_OffsetX+sbg.Get_GUI_Scale())*5/6f,arg0.getHeight()/10f,(Map_OffsetX+sbg.Get_GUI_Scale())*4/6f,arg0.getHeight()*3/10f);
		  
		  R3=new Rectangle((Map_OffsetX+sbg.Get_GUI_Scale())/6f,arg0.getHeight()*6/10f,(Map_OffsetX+sbg.Get_GUI_Scale())*4/6f,arg0.getHeight()*3/10f);
		  R4=new Rectangle(sbg.Get_Display_width()-(Map_OffsetX+sbg.Get_GUI_Scale())*5/6f,arg0.getHeight()*6/10f,(Map_OffsetX+sbg.Get_GUI_Scale())*4/6f,arg0.getHeight()*3/10f);
		  
		  R5=new Rectangle(Map_OffsetX+sbg.Get_GUI_Scale()*2/3,Map_OffsetY+sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*11+sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*8+sbg.Get_GUI_Scale()*2/3);
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		 lib = new Image_Library();
		 arg0.getInput().clearMousePressedRecord();
		 Layout_Logic map_gen = new Layout_Logic(lib);
		 Map m = map_gen.Generate_Standard_Map();
	     L=new GameLogic(lib,m);
	     players = new ArrayList<Bomber>();
	     int [][] Settings=sbg.Get_Settings();
	     players.add(new Bomber(1,1,lib,m,Settings[0][0],
	    		 						Settings[0][1],
	    		 						Settings[0][2],
	    		 						Settings[0][3],
	    		 						Settings[0][4],
	    		 						Settings[0][5],
	    		 						Settings[0][6],
	    		 						players.size()));
	     	     
	     arg0.getInput().addKeyListener(InputKey);
	     arg0.getInput().clearMousePressedRecord();
	     init=true;
	     won=false;
	     PlayerList=new ArrayList<String[]>();
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		String tmp[];
		try {
			server_response=sbg.server.poll();
			
			//System.out.println(server_response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(init) {
			setup_online_game();
		}
		else {
			if(server_response!=null) {
				tmp=server_response.split("_");
				if(tmp[0].equals("game")) {
					if(tmp[1].equals("over")) {
						won=false;
						winner=Integer.parseInt(tmp[2]);
						Timer tt= new Timer();
						tt.schedule(new Game_End_Delay(), 2000);
					}
					else if(tmp[1].equals("won")) {
						won=true;
						winner=Integer.parseInt(tmp[2]);
						Timer tt= new Timer();
						tt.schedule(new Game_End_Delay(), 2000);
					}
					else if(tmp[1].equals("update")) {
						try {
							m = (Map) sbg.server.ois.readObject();
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().removeKeyListener(InputKey);
		try {
			sbg.server.oos.close();
			sbg.server.ois.close();
			sbg.server.socketObject.close();
			sbg.server.socketObject=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
		ArrayList<String> elements;
		String[] tmp;
		
		g.drawImage(background, 0, 0);
		
		g.texture(R1,background,true);
		g.texture(R2,background,true);
		g.texture(R3,background,true);
		g.texture(R4,background,true);
		g.texture(R5,background,true);
		
		g.drawImage(Game_Background, Map_OffsetX+sbg.Get_GUI_Scale(), Map_OffsetY+sbg.Get_GUI_Scale());		
		
		for(int i=1;i<PlayerList.size()+1;i++){
			switch(i) {
				case 1:
					MyFont.drawString(R1.getCenterX()-MyFont.getWidth("PLAYER 1")/2f, R1.getY(), "PLAYER 1");
					g.drawImage(Player1, R1.getCenterX()-Player1.getWidth()/2f,R1.getCenterY()-Player1.getHeight()/2f);
					MyFont.drawString(R1.getCenterX()-MyFont.getWidth(PlayerList.get(i-1)[8])/2f, R1.getMaxY()-MyFont.getWidth(PlayerList.get(i-1)[8]), PlayerList.get(i-1)[8]);
					break;
				case 2:
					MyFont.drawString(R2.getCenterX()-MyFont.getWidth("PLAYER 2")/2f, R2.getY(), "PLAYER 2");
					g.drawImage(Player2, R2.getCenterX()-Player2.getWidth()/2f,R2.getCenterY()-Player2.getHeight()/2f);
					MyFont.drawString(R2.getCenterX()-MyFont.getWidth(PlayerList.get(i-1)[8])/2f, R2.getMaxY()-MyFont.getWidth(PlayerList.get(i-1)[8]), PlayerList.get(i-1)[8]);
					break;
				case 3:
					MyFont.drawString(R3.getCenterX()-MyFont.getWidth("PLAYER 3")/2f, R3.getY(), "PLAYER 3");
					g.drawImage(Player3, R3.getCenterX()-Player3.getWidth()/2f,R3.getCenterY()-Player3.getHeight()/2f);
					MyFont.drawString(R3.getCenterX()-MyFont.getWidth(PlayerList.get(i-1)[8])/2f, R3.getMaxY()-MyFont.getWidth(PlayerList.get(i-1)[8]), PlayerList.get(i-1)[8]);
					break;
				case 4:
					MyFont.drawString(R4.getCenterX()-MyFont.getWidth("PLAYER 4")/2f, R4.getY(), "PLAYER 4");
					g.drawImage(Player4, R4.getCenterX()-Player4.getWidth()/2f,R4.getCenterY()-Player4.getHeight()/2f);
					MyFont.drawString(R4.getCenterX()-MyFont.getWidth(PlayerList.get(i-1)[8])/2f, R4.getMaxY()-MyFont.getWidth(PlayerList.get(i-1)[8]), PlayerList.get(i-1)[8]);
					break;
			}
		}
		
		if(m!=null)
			for(int x = 0 ; x < m.Get_RightBound() ; x++) {
				for(int y = 0 ; y < m.Get_BotBound() ; y++) {
						elements=m.info_elements.get(x).get(y);
						for(int i = 0; i < elements.size(); i++) {
							tmp = elements.get(i).split(",");
								if(!DrawBomber(tmp,g)){
									Image im = new Image(tmp[3]);
									im=im.getScaledCopy(sbg.Get_GUI_Scale(),sbg.Get_GUI_Scale());
									g.drawImage(im,
											sbg.Get_GUI_Scale()*(Integer.parseInt(tmp[1]))+Map_OffsetX, 
											sbg.Get_GUI_Scale()*(Integer.parseInt(tmp[2]))+Map_OffsetY);
								}
						}
					}
			}
	}

	public int getID(){
		return 9;
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
			System.out.println("KEYPRESSED");
			
			//L.Action(key);
			try {
				sbg.server.dos.writeUTF("game_act_"+Integer.toString(key));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Timer tt = new Timer();
			tt.schedule(new KeyTimer(), 200);
			
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
			InputKey.setAcceptingInput(true);
		}
		
	}
	
	private void setup_online_game() {
		try {
			if(sbg.server.socketObject==null)
				sbg.server.Accept_Object_Socket();
			if(server_response!=null)
				if(server_response.equals("game_playerinfo")){
					System.out.println("bomber request received");
					sbg.server.dos.writeUTF("game_playerinfo");
					sbg.server.SendBomber(players.get(0));
				}
				else if(server_response.equals("game_start")){
					
					init=false;
				}
				else if(server_response.equals("game_bombers_start")){
					Read_Opponents_Info();
				}
		} catch (IOException | NumberFormatException | SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void Read_Opponents_Info() throws IOException, NumberFormatException, SlickException{
		
		try {
			PlayerList= (ArrayList<String[]>) sbg.server.ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		for(int i=1;i<PlayerList.size()+1;i++){
			switch(i) {
				case 1:
					Player1 = new Image("sprites/D_"   + Integer.parseInt(PlayerList.get(i-1)[6]) + Integer.parseInt(PlayerList.get(i-1)[7]) + ".png");
					Player1=Player1.getScaledCopy(3);
					break;
				case 2:
					Player2=new Image("sprites/D_"   + Integer.parseInt(PlayerList.get(i-1)[6]) + Integer.parseInt(PlayerList.get(i-1)[7]) + ".png");
					Player2=Player2.getScaledCopy(3);
					break;
				case 3:
					Player3=new Image("sprites/D_"   + Integer.parseInt(PlayerList.get(i-1)[6]) + Integer.parseInt(PlayerList.get(i-1)[7]) + ".png");
					Player3=Player3.getScaledCopy(3);
					break;
				case 4:
					Player4=new Image("sprites/D_"   + Integer.parseInt(PlayerList.get(i-1)[6]) + Integer.parseInt(PlayerList.get(i-1)[7]) + ".png");
					Player4=Player4.getScaledCopy(3);
					break;
			}
		}
	}
	
	
	private boolean DrawBomber(String[] x,Graphics g) throws SlickException {
		String tmp=x[3];
		Image img;
		//System.out.println(Integer.parseInt(x[1])+" "+Integer.parseInt(x[2])+" "+Float.parseFloat(x[4])+" "+Float.parseFloat(x[5])+" "+Integer.parseInt(x[6])+" "+Integer.parseInt(x[7]));
		switch(tmp) {
		case "StopDown":
			img= new Image("sprites/D_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "StopLeft":
			img= new Image("sprites/D_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(90);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "StopUp":
			img= new Image("sprites/D_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(180);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "StopRight":
			img= new Image("sprites/D_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(270);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "Down1":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "Down2":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			g.drawImage(img.getFlippedCopy(true,false),sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX,  
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "Left1":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(90);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "Left2":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img = img.getFlippedCopy(true,false);
			img.setRotation(90);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "Up1":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(180);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "Up2":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img = img.getFlippedCopy(true,false);
			img.setRotation(180);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "Right1":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img.setRotation(270);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		case "Right2":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img=img.getScaledCopy(sbg.Get_GUI_Scale()*2/3,sbg.Get_GUI_Scale()*2/3);
			img = img.getFlippedCopy(true,false);
			img.setRotation(270);
			g.drawImage(img,sbg.Get_GUI_Scale()*Integer.parseInt(x[1]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[4])+(sbg.Get_GUI_Scale()-img.getWidth())/2f+Map_OffsetX, 
					sbg.Get_GUI_Scale()*Integer.parseInt(x[2]) + sbg.Get_GUI_Scale()*Float.parseFloat(x[5])+(sbg.Get_GUI_Scale()-img.getHeight())/2f+Map_OffsetY);
			return true;
		}
		return false;
	}

	public boolean Is_Winner() {
		// TODO Auto-generated method stub
		return won;
	}
	
	public int Get_Winner() {
		return winner;
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
			sbg.enterState(sbg.Get_OnlineGameOver_State());		//Go to Game Over
		}
		
	}
	
}
