package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.SlickException;

import GameLogic.Bomber;
import GameLogic.GameLogic;
import GameLogic.Image_Library;
import GameLogic.Layout_Logic;
import GameLogic.Map;

public class GameHandler extends Thread{
	private ArrayList<Client> Players;
	private ArrayList<Client> Spectators;
	private ArrayList<Bomber> Characters;
	public GameLogic L;
	transient private Image_Library lib;
	private Map m;
	private ArrayList<Integer> Actions;
	private boolean First_Action=false;
	private boolean Game_Over;
	
	GameHandler(ArrayList<Client> x) {
		Players = new ArrayList<Client>();
		Spectators = new ArrayList<Client>();	
		Characters = new ArrayList<Bomber>();
		L=null;
		lib=null;
		m=null;
		Actions = new ArrayList<Integer>();
		Players=x;
		for(int i=0;i<Players.size();i++) {
			Players.get(i).Set_Player(i+1);
			Players.get(i).AddToGame(this);
		}
		Game_Over=false;
	}
	
	@Override
	public void run() {
		boolean init=true;
		
		for(int i=0;i<Players.size();i++) {
			Create_New_Socket(Players.get(i));
		}
		
		if(Players.size()==0)
			return;
		
		Timer tt = new Timer();
		tt.schedule(new Player_Info_Querry(),0,200);
		
		while(true) {
			if(!init){
				try {
					this.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				int i;
				for(i=0;i<Players.size();i++) 
					if(Players.get(i).GetBomber()==null)
						break;
				if(i==Players.size()) {
					init=false;
					tt.cancel();
					
					try {
						init_game();
						for(i=0;i<Players.size();i++)
							try {
								Players.get(i).Send_Bombers(Players);
								Players.get(i).dos.writeUTF("game_start");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						Timer tt2 = new Timer();
						tt2.schedule(new Update_Task(),20);
					} catch (SlickException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private class Player_Info_Querry extends TimerTask{

		@Override
		public void run() {
			
			for(int i=0;i<Players.size();i++) {
				if(Players.get(i).GetBomber()!=null)
					continue;
					try {
						Players.get(i).Request_Client_Playerinfo();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
		}
	}
	
	private class Update_Task extends TimerTask{

		@Override
		public void run() {
			if(Players.size()==0)
				this.cancel();
			
			if(!First_Action) {
				Serialize_Data();
				for(int i=0;i<Players.size();i++)
					try {

							Players.get(i).Send_Map(m);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
			while(Actions.size()!=0) {
				L.Action(Actions.get(0),Actions.get(1));
				Actions.remove(0);
				Actions.remove(0);
			}
			
			try {
				if(lib.Run_Changes()!=0){
					First_Action=true;
					
					if(!Game_Over){
						int ret=L.Death_Check();
							if(ret!=0) {
								Players.get(ret-1).dos.writeUTF("game_lost");
								Spectators.add(Players.get(ret-1));
								Players.remove(ret-1);
								Spectators.get(Spectators.size()-1).RemoveFromGame();
								DB.incrementPlayedGame(Spectators.get(Spectators.size()-1).username,false);
							}
							
						if(Players.size()==1){
							Game_Over=true;
							Players.get(0).dos.writeUTF("game_won_"+Players.get(0).Get_Player());
							Timer tt = new Timer();
							tt.schedule(new Game_End_Delay(this), 2000);
							DB.incrementPlayedGame(Players.get(0).username,true);
							for(int i=0;i<Spectators.size();i++) {
								Spectators.get(i).dos.writeUTF("game_over_"+Players.get(0).Get_Player());
							}
						}
					}		
					
					
					Serialize_Data();
					
					for(int i=0;i<Players.size();i++)
						try {

								Players.get(i).Send_Map(m);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					for(int i=0;i<Spectators.size();i++)
						try {

							Spectators.get(i).Send_Map(m);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Timer tt2 = new Timer();
			tt2.schedule(new Update_Task(),20);
		}
		
	}
	
	private void Create_New_Socket(Client x){
		try {
			x.objectsocket = new Socket(x.datasocket.getInetAddress(),x.outputsocket);
			x.oos = new ObjectOutputStream(x.objectsocket.getOutputStream());
			x.ois = new ObjectInputStream(x.objectsocket.getInputStream());
			System.out.println("SOCKET CREATED " + x.objectsocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// obtaining input and out streams
	}
	
	private void init_game() throws SlickException {
		lib = new Image_Library();
		Layout_Logic map_gen = new Layout_Logic(lib);
		m = map_gen.Generate_Standard_Map();
	    L=new GameLogic(lib,m);
		Characters = new ArrayList<Bomber>();
		
		for(int i=0;i<Players.size();i++){
			int ret=Players.get(i).Get_Player();
			Bomber tmp= new Bomber(Players.get(i).GetBomber(),lib,m);
			Players.get(i).Add_Player(tmp);
			if(ret==1) {
				Players.get(i).GetBomber().setX(m.Get_LeftBound());
				Players.get(i).GetBomber().setY(m.Get_TopBound());
			}
			else if(ret==2) {
				Players.get(i).GetBomber().setX(m.Get_RightBound()-1);
				Players.get(i).GetBomber().setY(m.Get_BotBound()-1);
			}
			else if(ret==3) {
				Players.get(i).GetBomber().setX(m.Get_LeftBound());
				Players.get(i).GetBomber().setY(m.Get_BotBound()-1);
			}
			else {
				Players.get(i).GetBomber().setX(m.Get_RightBound()-1);
				Players.get(i).GetBomber().setY(m.Get_TopBound());
			}
			
			Characters.add(Players.get(i).GetBomber());
		}
		
	    L.Place_Characters(Characters);
	}
	
	public void Buffer_Input(int key,int player){
		Actions.add(key);
		Actions.add(player);
	}
	
	public void Serialize_Data() {
		m.Update();
	}
	
	public void Remove_Client(Client x) {
		int player=x.Get_Player();
		
		for(int i=0;i<Players.size();i++)
			if(Players.get(i).Get_Player()==player){
				Players.remove(i);
				return;
			}
		
		for(int i=0;i<Spectators.size();i++)
			if(Spectators.get(i).Get_Player()==player) {
				Spectators.remove(i);
				return;
			}
		
		System.out.println("Client not in this game");
	}
	
	private class Game_End_Delay extends TimerTask{
		private Update_Task Update;
		
		Game_End_Delay(Update_Task x){
			Update=x;
		}

		@Override
		public void run() {
			try {
				Players.get(0).Game_Ended();
				
				for(int i=0;i<Spectators.size();i++) {
					Spectators.get(0).Game_Ended();
				}
				
				Players.removeAll(Players);
				Spectators.removeAll(Spectators);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Update.cancel();
		}
		
	}
}
