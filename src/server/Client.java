package server;

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
	private boolean GameFound = false;
	public boolean Playing = false;
	private Bomber Character = null;
	public int outputsocket;
	private int player;

	private ArrayList<String> friends = new ArrayList<String>();

	public Client(Socket socket, DataInputStream dis, DataOutputStream dos) {
		this.datasocket = socket;
		this.dis = dis;
		this.dos = dos;
		this.username = null;
	}

	public void login(String user, String pw, ArrayList<Client> userlist, String socket) throws SQLException, IOException {
		String result;
		result = server.DB.login(user, pw);

		if (result == "Logged in") {
			this.outputsocket = Integer.parseInt(socket);
			userlist.add(this);
			this.dos.writeUTF(result);
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

	public void register(String user, String pw, String email) throws SQLException, IOException {
		String result;
		result = server.DB.register(user, pw, email);
		System.out.println(result);
		this.dos.writeUTF(result);
	}

	public void AddToGame(GameHandler x) {
		game = x;
		Character = null;
		Playing = true;
	}

	public void RemoveFromGame() {
		game = null;
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
		RemoveFromGame();
		oos.close();
		ois.close();
		objectsocket.close();
	}
}
