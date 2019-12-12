import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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
	
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		Input = new KeyPresses();
		sbg=(GUI_setup) arg1;
		sbg.Set_Game_State(getID());
	}
	
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		 lib = new Image_Library();
	     L=new GameLogic(lib);
	     players = new ArrayList<Bomber>();
	     int [][] Settings=sbg.Get_Settings();
	     players.add(new Bomber(1,1,L,	Settings[0][0],
	    		 						Settings[0][1],
	    		 						Settings[0][2],
	    		 						Settings[0][3],
	    		 						Settings[0][4],
	    		 						Settings[0][5],
	    		 						Settings[0][6],
	    		 						players.size()));
	     
	     players.add(new Bomber(11,8,L,	Settings[1][0],
										Settings[1][1],
										Settings[1][2],
										Settings[1][3],
										Settings[1][4],
										Settings[1][5],
										Settings[1][6],
										players.size()));
	     L.Create_Map(2);
	     L.Place_Characters(players);
	     
	     arg0.getInput().addKeyListener(Input);
	     arg0.getInput().clearMousePressedRecord();
	     lib.Initialize_Image_Library();
	}

	public void update(GameContainer container, StateBasedGame arg1, int arg2) throws SlickException {
		
		
		lib.Run_Changes();
		dead=L.Death_Check();
		
		if(dead!=0) {
			sbg.enterState(sbg.Get_GameOver_State());		//Go to Game Over
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
							g.drawImage(tmp.Get_Image(),
										tmp.Get_Scale()*(tmp.getX()) + tmp.Get_OffsetX(), 
										tmp.Get_Scale()*(tmp.getY()) + tmp.Get_OffsetY());
						}
					}
				}
		}
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
}
