package server;

import server.DB;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

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
	private boolean GameFound=false;
	private Bomber Character= null;
	public int outputsocket;
	
	private ArrayList<String> friends = new ArrayList<String>();
	
	
	public Client(Socket socket, DataInputStream dis, DataOutputStream dos) {
		this.datasocket = socket;
		this.dis = dis;
		this.dos = dos;
		this.username = null;
	}
	
	public void login(String user, String pw, ArrayList<Client> userlist,String socket) throws SQLException, IOException {
		String result = null;
		//result = server.DB.login(user, pw);
		if((user.equals("root1") && pw.equals("1234")) || (user.equals("root2") && pw.equals("1234")))
			result="Logged in";	
		
		if (result == "Logged in") {
			userlist.add(this);
			outputsocket=Integer.parseInt(socket);
			this.dos.writeUTF(result);
			return;
		}
		else {
			this.dos.writeUTF(result);
			return;
		}
		
	}
	
	public void Send_Game_Found_Message() throws IOException {
		dos.writeUTF("game_found");
		GameFound=true;
	}

	public void AddToGame(GameHandler x) {
		game=x;
		Character=null;
	}
	
	public void RemoveFromGame() {
		game=null;
		Character=null;
	}
	
	public boolean GetGameFound() {
		return GameFound;
	}
	
	public Bomber Request_Client_Player() throws IOException {
		dos.writeUTF("game_player");
		return null;
	}
	
	public void Add_Player(Bomber x) {
		System.out.println("ADDED BOMBER");
		Character=x;
		System.out.println(x.Get_MoveUp_Key());
	}
	
	public Bomber GetBomber() {
		return Character;
	}
	
	public void Send_Map(Map x) throws IOException {
		dos.writeUTF("game_update");
		oos.reset();
		oos.writeObject(x);
	}
}
