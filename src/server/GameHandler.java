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
	private ArrayList<Client> Players = new ArrayList<Client>();
	private ArrayList<Bomber> Characters = new ArrayList<Bomber>();
	public GameLogic L;
	private Image_Library lib;
	private Map m;
	private ArrayList<Integer> Actions = new ArrayList<Integer>();
	
	GameHandler(Client x1, Client x2) {
		x1.AddToGame(this);
		x2.AddToGame(this);
		Players.add(x1);
		Players.add(x2);
	}
	
	@Override
	public void run() {
		boolean init=true;
		
		for(int i=0;i<Players.size();i++) {
			Create_New_Socket(Players.get(i));
		}
		
		Timer tt = new Timer();
		tt.schedule(new Player_Info_Querry(),0,200);
		
		while(true) {
			if(!init){
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
						Players.get(i).Request_Client_Player();
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
			// TODO Auto-generated method stub
			try {
				while(Actions.size()!=0) {
					L.Action(Actions.get(0));
					Actions.remove(0);
				}
				
				lib.Run_Changes();
			} catch (SlickException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int i=0;i<Players.size();i++)
				try {
					Map send=m;
					synchronized(m) {
						Players.get(i).Send_Map(send);
					}
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
			System.out.println("SOCKET CREATED " + x.objectsocket);
			x.oos = new ObjectOutputStream(x.objectsocket.getOutputStream());
			x.ois = new ObjectInputStream(x.objectsocket.getInputStream());
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
			Characters.add(Players.get(i).GetBomber());
		}
		
	    L.Place_Characters(Characters);
	}
	
	public void Buffer_Input(int key){
		Actions.add(key);
	}
}
