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
				for (String field : words) {
					if (field.equals(""))
						continue;
				}

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
							if(!IsGameFound()){
								Server_Handler.Remove_From_Queue(queue_pos);
								client.dos.writeUTF("OK");
							}
						else
							client.dos.writeUTF("start or cancel needed");
					} else
						client.dos.writeUTF("Wrong number of arguments for looking");
					break;
					
				case "game":
					if(words.length>=2 && client.Playing){
						if(words[1].equals("playerinfo")){
							Bomber tmp=(Bomber) client.ois.readObject();
							if(tmp!=null) {
								client.Add_Player(tmp);
							}
						}
						else if(words[1].equals("act")){
							try {
								client.game.Buffer_Input(Integer.parseInt(words[2]),client.Get_Player());
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else
						client.dos.writeUTF("Wrong number of arguments for g");
					
				case "register":
					if (words.length == 4) {
						client.register(words[1], words[2], words[3]);
					} else {
						client.dos.writeUTF("Username, Password and Email mandatory");
					}
					break;
					
				case "friends":
					if(words.length>=2) {
						if(words[1].equals("request")) {
							
							if(client.username==null)
								client.dos.writeUTF("You're Not Logged In");
							else {
								client.dos.writeUTF("friends_request_start");
								
								client.Update_Friends(Server_Handler);
								for(int i=0;i<client.Get_friends().size();i++) {
									client.dos.writeUTF("friends_info_USER="+client.Get_friends().get(i)+
								"_FRIENDSTATE420="+Integer.toString(Server_Handler.database.isFriend(client.username,client.Get_friends().get(i))));
								}
								
								client.dos.writeUTF("friends_request_stop");
							}
							
						}
						else if(words[1].equals("add")) {
							if(words.length!=2) {
								String ret=Server_Handler.database.requestFriendship(client.username,words[2]);
								if(ret.equals("Request sent!"))
									client.dos.writeUTF("friends_add_OK");
								else
									client.dos.writeUTF("friends_add_ERROR");
							}
							else
								client.dos.writeUTF("friends_add_ERROR");
						}
						else if(words[1].equals("remove")) {
							
						}
						else if(words[1].equals("accept")){
							if(words.length!=2) {
								String ret=Server_Handler.database.acceptFriendship(client.username,words[2]);
								if(ret.equals("Accepted " + words[2] + "as friend"))
									client.dos.writeUTF("friends_accept_OK");
								else
									client.dos.writeUTF("friends_accept_ERROR");
							}
							else
								client.dos.writeUTF("friends_add_ERROR");
						}
						else if(words[1].equals("invite")) {
							
						}
					}
					break;

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
