package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
						for(i=0;i<Players.size();i++) {
							Players.get(i).dos.writeUTF("game_start");
						}
						Timer tt2 = new Timer();
						tt2.schedule(new Update_Task(),100);
					} catch (SlickException | IOException e) {
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
			// TODO Auto-generated method stub
			try {
				while(Actions.size()!=0) {
					L.Action(Actions.get(0),Actions.get(1));
					Actions.remove(0);
					Actions.remove(0);
				}
				
			lib.Run_Changes();
			int ret=L.Death_Check();
				if(ret!=0) {
					Players.get(ret-1).dos.writeUTF("game_lost");
					Spectators.add(Players.get(ret-1));
					Players.remove(ret-1);
					Spectators.get(Spectators.size()-1).RemoveFromGame();
				}
			if(Players.size()==1){
				Players.get(0).dos.writeUTF("game_won_"+Players.get(0).Get_Player());
				Players.get(0).Game_Ended();
				for(int i=0;i<Spectators.size();i++) {
					Spectators.get(i).dos.writeUTF("game_over_"+Players.get(0).Get_Player());
					Spectators.get(0).Game_Ended();
				}
				Players.removeAll(Players);
				Spectators.removeAll(Spectators);
				this.cancel();
			}
				
			} catch (SlickException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Serialize_Data();
			
			for(int i=0;i<Players.size();i++)
				try {

						Players.get(i).Send_Map(m);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			Timer tt2 = new Timer();
			tt2.schedule(new Update_Task(),100);
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
}
