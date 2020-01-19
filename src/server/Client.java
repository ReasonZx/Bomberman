
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import GameLogic.Bomber;
import GameLogic.Map;

public class Client {

	public String username;
	public Socket datasocket;
	public Socket objectsocket;
	public DataInputStream dis;
	public DataOutputStream dos;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	public GameHandler game;
	private boolean GameFound = false;
	public boolean Playing = false;
	private Bomber Character = null;
	public boolean InQueue = false;
	public int outputsocket;
	private int player;
	private Timer tt;
	protected boolean cancelled_game=false;

	private ArrayList<String> friends = new ArrayList<String>();

	public Client(Socket socket, DataInputStream dis, DataOutputStream dos) {
		this.datasocket = socket;
		this.dis = dis;
		this.dos = dos;
		this.username = null;
	}

	public void login(String user, String pw, ArrayList<Client> userlist, String socket) throws SQLException, IOException {
		String result;
		this.outputsocket = Integer.parseInt(socket);
		for(int i=0;i<userlist.size();i++) {
			if(userlist.get(i).username.equals(user)){
				this.dos.writeUTF("User already Logged In");
				return;
			}
		}
		
		result = server.DB.login(user, pw);

		if (result == "Logged in") {
			userlist.add(this);
			this.dos.writeUTF(result);
			username=user;
			return;
		} else {
			this.dos.writeUTF(result);
			return;
		}
	}

	public void Send_Game_Found_Message() throws IOException {
		dos.writeUTF("game_found");
		GameFound = true;
	}
	
	public void Send_Invite_Message(Client Target) throws IOException {
		Target.dos.writeUTF("friends_invited_"+username);
		tt=new Timer();
		tt.schedule(new Timeout(Target),500);
		cancelled_game=true;
	}

	public void register(String user, String pw, String email) throws SQLException, IOException {
		String result;
		result = server.DB.register(user, pw, email);
		System.out.println(result);
		this.dos.writeUTF(result);
	}
	
	public int get_games_won() throws SQLException , IOException{
		int result;
		result = server.DB.getWonGames(username);
		return result;
	}
	
	public int get_games_played() throws SQLException, IOException{
		int result;
		result = server.DB.getPlayedGames(username);
		return result;
	}
	
	public int get_number_of_friends() throws SQLException, IOException{
		ArrayList<String> friendlist;
		friendlist = server.DB.getFriendsList(username);
		return friendlist.size();
	}
	
	
	
	public void AddToQueue(){
		InQueue=true;
	}

	public void RemoveFromQueue(){
		InQueue=false;
	}
	
	public void AddToGame(GameHandler x) {
		game = x;
		Character = null;
		Playing = true;
	}

	public void RemoveFromGame() {
		Character = null;
		Playing = false;
	}

	public boolean GetGameFound() {
		return GameFound;
	}

	public Bomber Request_Client_Playerinfo() throws IOException {
		dos.writeUTF("game_playerinfo");
		return null;
	}

	public void Add_Player(Bomber x) {
		Character = x;
		Character.Set_Player(player);
	}

	public Bomber GetBomber() {
		return Character;
	}

	public void Send_Map(Map x) throws IOException {
		dos.writeUTF("game_update");
		oos.reset();
		oos.writeObject(x);
	}

	public int Get_Player() {
		// TODO Auto-generated method stub
		return player;
	}

	public void Set_Player(int x) {
		// TODO Auto-generated method stub
		player = x;
	}

	public void Game_Ended() throws IOException {
		System.out.println("GameOver");
		game=null;
		RemoveFromGame();
		oos.close();
		ois.close();
		objectsocket.close();
	}

	public ArrayList<String> Get_friends() {
		// TODO Auto-generated method stub
		return friends;
	}

	public void Update_Friends(Server x) {
		friends=new ArrayList<String>();
		try {
			friends=server.DB.getFriendsList(this.username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Cancel_Timeout(){
		cancelled_game=false;
		System.out.println("Cancel Timeout");
	}
	
	
	
	private class Timeout extends TimerTask{
		Client Target;
		
		Timeout(Client x){
			Target=x;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
			if(cancelled_game) {
				dos.writeUTF("friends_invite_NotAvailable");
				Target.dos.writeUTF("friends_invited_cancel_"+username);
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}



	public void Send_Bombers(ArrayList<Client> x) throws IOException {
		dos.writeUTF("game_bombers_start");
		ArrayList<String[]> tmp = new ArrayList<String[]>();
		
		for(int i=0;i<x.size();i++){
			String tmp2;
			tmp2=x.get(i).GetBomber().toString()+","+x.get(i).username;
			tmp.add(tmp2.split(","));
		}
		
		oos.writeObject(tmp);
	}
}