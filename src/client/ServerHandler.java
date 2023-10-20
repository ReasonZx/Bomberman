package client;
import java.io.*;
import java.net.*;

import GameLogic.Bomber;

public class ServerHandler {

	Socket socketdata;
	Socket socketObject;
	DataInputStream dis;
	DataOutputStream dos;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	InetAddress ip;
	public ServerSocket ss;

	public ServerHandler() throws IOException {
		// establish the connection with server port 5056
		this.ip = InetAddress.getByName("localhost");
		this.socketdata = new Socket(ip, 5056);
		// obtaining input and out streams
		this.dis = new DataInputStream(socketdata.getInputStream());
		this.dos = new DataOutputStream(socketdata.getOutputStream());
		ss = new ServerSocket(0);
	}

	public String request(String req) throws IOException {
		String received = null;
		try {
			if(req!=null)
				dos.writeUTF(req);
			while (received == null) {
				received = dis.readUTF();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return received;
	}

	public String poll() throws IOException {
		if(dis.available()!=0)
			return dis.readUTF();
		else return null;
	}
	
	public void SendBomber(Bomber x) throws IOException {
		oos.writeObject(x);
	}
	
	public void Accept_Object_Socket() throws IOException {
		System.out.println("OBJECT SOCKET WAITING");
		socketObject=ss.accept();
		// obtaining input and out streams
		System.out.println("OBJECT SOCKET OPENED");
		this.oos = new ObjectOutputStream(socketObject.getOutputStream());
		this.ois = new ObjectInputStream(socketObject.getInputStream());
		
	}

	public void send(String string) {
		try {
			if(string!=null)
				dos.writeUTF(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
