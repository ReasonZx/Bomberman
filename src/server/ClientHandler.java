package server;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import org.newdawn.slick.SlickException;

import GameLogic.Bomber;

import java.net.*;
import java.sql.*;

public class ClientHandler extends Thread {

	private Client client;
	private ArrayList<Client> userlist;
	private Server Server_Handler;
	private int queue_pos;

	public ClientHandler(Client client,Server x) {
		this.client = client;
		Server_Handler = x;
		userlist = Server_Handler.Get_UserList();
	}

	@Override
	public void run() {
		String rx_string;

		try {
			while (true) {
				rx_string = null;

				// receive the answer from client

				rx_string = client.dis.readUTF();
				String words[] = rx_string.split("_");

				if (words[0].equals("Exit")) {
					System.out.println("Client " + this.client.datasocket + " sends exit...");
					System.out.println("Closing this connection.");
					this.client.datasocket.close();
					System.out.println("Connection closed");
					break;
				}

				// write on output stream based on the
				// answer from the client

				switch (words[0]) {

				case "login":
					if (words.length == 4) {
						client.login(words[1], words[2], userlist,words[3]);
					} else
						client.dos.writeUTF("Login and Password mandatory");
					break;
					
				case "looking":
					if(words.length==2) {
						if(words[1].equals("start")) {
							queue_pos=Server_Handler.Add_To_Queue(client);
							System.out.println("Added to queue");
							client.dos.writeUTF("OK");
						}
						if(words[1].equals("cancel"))
							if(!IsGameFound())
								Server_Handler.Remove_From_Queue(queue_pos);
						else
							client.dos.writeUTF("start or cancel needed");
					} else
						client.dos.writeUTF("Wrong number of arguments for looking");
					break;
					
				case "game":
					if(words.length>=2){
						if(words[1].equals("player")){
							Bomber tmp=(Bomber) client.ois.readObject();
							if(tmp!=null) {
								client.Add_Player(tmp);
							}
						}
						else if(words[1].equals("act")){
							try {
								client.game.Buffer_Input(Integer.parseInt(words[2]));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else
						client.dos.writeUTF("Wrong number of arguments for g");

				default:
					client.dos.writeUTF("Invalid input");
					break;
				}
			}
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// closing resources
			this.client.dis.close();
			this.client.dos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean IsGameFound() {
		return client.GetGameFound();
	}

}
