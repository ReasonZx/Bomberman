package client;

import GameLogic.Image_Library;
import GameLogic.Layout_Logic;
import GameLogic.Map;
import GameLogic.GameLogic;
import GameLogic.Bomber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
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
	 private Image Back;
	 private int backX, backY;
	 private boolean init=false;
	 private String server_response;
	 private Map m;
	 private boolean won;
	 private int winner;
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		InputKey = new KeyPresses();
		sbg=(GUI_setup) arg1;
		sbg.Set_OnlineGame_State(getID());
		
		Back = new Image("sprites/back.png");
	    Back = Back.getScaledCopy(0.2f);
	    backX = 50;
	    backY = 20;
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
	     	     
	     arg0.getInput().addKeyListener(InputKey);
	     arg0.getInput().clearMousePressedRecord();
	     init=true;
	     won=false;
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = arg0.getInput().getMouseX();
		int posY = arg0.getInput().getMouseY();
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
						sbg.enterState(sbg.Get_OnlineGameOver_State());
					}
					else if(tmp[1].equals("won")) {
						won=true;
						winner=Integer.parseInt(tmp[2]);
						sbg.enterState(sbg.Get_OnlineGameOver_State());
					}
					else if(tmp[1].equals("update")) {
						try {
							m=(Map) sbg.server.ois.readObject();
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		/*dead=L.Death_Check();
		
		if(dead!=0) {
			sbg.enterState(sbg.Get_GameOver_State());		//Go to Game Over
		}*/
		
		if((posX > backX && posX < backX + Back.getWidth()) && (posY > backY && posY < backY + Back.getHeight())) {		// ver tamanhos certos dos bot�es	//go back
			if(arg0.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				sbg.enterState(sbg.Get_Menu_State());
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
		//g.drawString("Game state", 50, 50);
		//g.drawString(player1.toString(),100,100);
		ArrayList<String> elements;
		String[] tmp;
		
		if(m!=null)
			for(int x = 0 ; x < m.Get_RightBound() ; x++) {
				for(int y = 0 ; y < m.Get_BotBound() ; y++) {
						elements=m.info_elements.get(x).get(y);
						for(int i = 0; i < elements.size(); i++) {
							tmp = elements.get(i).split(",");
								if(Short.parseShort(tmp[0])== Bomber.serialVersionUID) {
									DrawBomber(tmp,g);
								}
								else {
									Image im = new Image(tmp[3]);
									im=im.getScaledCopy(64,64);
									g.drawImage(im,
												64*(Integer.parseInt(tmp[1])), 
												64*(Integer.parseInt(tmp[2])));
								}
						}
					}
			}
		Back.draw(backX,backY);
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
				else if(server_response.equals("game_start")) {
					init=false;
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void DrawBomber(String[] x,Graphics g) throws SlickException {
		String tmp=x[3];
		Image img;
		//System.out.println(Integer.parseInt(x[1])+" "+Integer.parseInt(x[2])+" "+Float.parseFloat(x[4])+" "+Float.parseFloat(x[5])+" "+Integer.parseInt(x[6])+" "+Integer.parseInt(x[7]));
		switch(tmp) {
		case "StopDown":
			img= new Image("sprites/D_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "StopLeft":
			img= new Image("sprites/D_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img.setRotation(90);
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "StopUp":
			img= new Image("sprites/D_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img.setRotation(180);
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "StopRight":
			img= new Image("sprites/D_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img.setRotation(270);
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "Down1":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "Down2":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			g.drawImage(img.getFlippedCopy(true,false),64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "Left1":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img.setRotation(90);
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "Left2":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img = img.getFlippedCopy(true,false);
			img.setRotation(90);
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "Up1":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img.setRotation(180);
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "Up2":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img = img.getFlippedCopy(true,false);
			img.setRotation(180);
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "Right1":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img.setRotation(270);
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		case "Right2":
			img= new Image("sprites/D1_"   + Integer.parseInt(x[6]) + Integer.parseInt(x[7]) + ".png");
			img = img.getFlippedCopy(true,false);
			img.setRotation(270);
			g.drawImage(img,64*Integer.parseInt(x[1]) + 64*Float.parseFloat(x[4])+(64-img.getWidth())/2f, 
					64*Integer.parseInt(x[2]) + 64*Float.parseFloat(x[5])+(64-img.getHeight())/2f);
			break;
		}
	}

	public boolean Is_Winner() {
		// TODO Auto-generated method stub
		return won;
	}
	
	public int Get_Winner() {
		return winner;
	}
}
