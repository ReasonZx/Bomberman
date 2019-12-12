package server;

import server.DB;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Client {

	public String username;
	public Socket socket;
	public DataInputStream dis;
	public DataOutputStream dos;
	
	private ArrayList<String> friends = new ArrayList<String>();
	
	
	public Client(Socket socket, DataInputStream dis, DataOutputStream dos) {
		this.socket = socket;
		this.dis = dis;
		this.dos = dos;
		this.username = null;
	}
	
	public void login(String user, String pw, ArrayList<Client> userlist) throws SQLException, IOException {
		String result;
		result = server.DB.login(user, pw);
		
		if (result == "Logged in") {
			userlist.add(this);
			return;
		}
		else {
			this.dos.writeUTF(result);
			return;
		}
		
	}

	
	
}
