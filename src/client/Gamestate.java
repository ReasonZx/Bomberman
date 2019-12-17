package client;

import GameLogic.Image_Library;
import GameLogic.Layout_Logic;
import GameLogic.Map;
import GameLogic.GameLogic;
import GameLogic.Bomber;
import GameLogic.Element;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
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
	 private Image Back;
	 private int backX, backY;


	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Input = new KeyPresses();
		sbg=(GUI_setup) arg1;
		sbg.Set_Game_State(getID());
		
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
	     
	     players.add(new Bomber(11,8,lib,m,	Settings[1][0],
										Settings[1][1],
										Settings[1][2],
										Settings[1][3],
										Settings[1][4],
										Settings[1][5],
										Settings[1][6],
										players.size()));
	     
	     L.Place_Characters(players);
	     
	     arg0.getInput().addKeyListener(Input);
	     arg0.getInput().clearMousePressedRecord();
	}

	public void update(GameContainer container, StateBasedGame arg1, int arg2) throws SlickException {
		int posX = Mouse.getX();
		int posY = sbg.Get_Display_height() - Mouse.getY();
		
		lib.Run_Changes();
		dead=L.Death_Check();
		
		if(dead!=0) {
			sbg.enterState(sbg.Get_GameOver_State());		//Go to Game Over
		}
		
		if((posX > backX && posX < backX + Back.getWidth()) && (posY > backY && posY < backY + Back.getHeight())) {		// ver tamanhos certos dos bot�es	//go back
			if(Mouse.isButtonDown(0)) {
				sbg.enterState(sbg.Get_Menu_State());
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
		
		for(int x = 0 ; x < L.m.Get_RightBound() ; x++) {
			for(int y = 0 ; y < L.m.Get_BotBound() ; y++) {
					elements=L.m.Get_List_Elements(x, y);
					for(int i = 0; i < elements.size(); i++) {
						Element tmp = elements.get(i);
						
						if(tmp.Has_Image()) {
							if(tmp instanceof Bomber) {
								DrawBomber((Bomber)tmp,g);
							}
							else
							g.drawImage(new Image(tmp.Get_Image()),
										tmp.Get_Scale()*(tmp.getX()) + tmp.Get_OffsetX(), 
										tmp.Get_Scale()*(tmp.getY()) + tmp.Get_OffsetY());
						}
					}
				}
		}
		Back.draw(backX,backY);
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
			try {
				L.Action(key);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "StopLeft":
			img= new Image("sprites/D_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img.setRotation(90);
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "StopUp":
			img= new Image("sprites/D_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img.setRotation(180);
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "StopRight":
			img= new Image("sprites/D_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img.setRotation(270);
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "Down1":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "Down2":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			g.drawImage(img.getFlippedCopy(true,false),x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "Left1":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img.setRotation(90);
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "Left2":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img = img.getFlippedCopy(true,false);
			img.setRotation(90);
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "Up1":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img.setRotation(180);
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "Up2":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img = img.getFlippedCopy(true,false);
			img.setRotation(180);
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "Right1":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img.setRotation(270);
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		case "Right2":
			img= new Image("sprites/D1_"   + sett[x.Get_Player()][0] + sett[x.Get_Player()][1] + ".png");
			img = img.getFlippedCopy(true,false);
			img.setRotation(270);
			g.drawImage(img,x.Get_Scale()*x.getX() + x.Get_Scale()*x.Get_OffsetX()+(x.Get_Scale()-img.getWidth())/2f, 
					x.Get_Scale()*(x.getY() + x.Get_OffsetY())+(x.Get_Scale()-img.getHeight())/2f);
			break;
		}
	}
}
