package server;

import java.io.*;
import java.util.*;
import GameLogic.Bomber;

import java.sql.*;

public class ClientHandler extends Thread {

	private Client client;
	private ArrayList<Client> userlist;
	private Server Server_Handler;
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
				
				case "statistics":
					if(words.length == 1) {
						client.dos.writeUTF(Integer.toString(client.get_games_won()) + "_" +
								Integer.toString(client.get_games_played()) + "_" + Integer.toString(client.get_number_of_friends()));
					}
					else {
						client.dos.writeUTF("get_statistics_ERROR");
					}
					break;

				case "login":
					if (words.length == 4) {
						client.login(words[1], words[2], userlist,words[3]);
					} else
						client.dos.writeUTF("Login and Password mandatory");
					break;
					
				case "looking":
					if(words.length==2) {
						if(words[1].equals("start")) {
							Server_Handler.Add_To_Queue(client);
							client.AddToQueue();
							System.out.println("Added to queue");
							client.dos.writeUTF("looking_OK");
						}
						if(words[1].equals("cancel"))
							if(!IsGameFound()){
								Server_Handler.Remove_From_Queue(client.username);
								client.RemoveFromQueue();
								client.dos.writeUTF("looking_OK");
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
					System.out.println(rx_string);
					if(words.length>=2) {
						switch (words[1]) {
						case "request": 
							
							if(client.username==null)
								client.dos.writeUTF("You're Not Logged In");
							else {
								client.dos.writeUTF("friends_request_start");
								
								client.Update_Friends(Server_Handler);
								for(int i=0;i<client.Get_friends().size();i++) {
									client.dos.writeUTF("friends_info_USER="+client.Get_friends().get(i)+
								"_FRIENDSTATE420="+Integer.toString(server.DB.isFriend(client.Get_friends().get(i),client.username)));
								}
								
								client.dos.writeUTF("friends_request_stop");
							}
							break;
							
						case "add":
							if(words.length!=2) {
								String ret=server.DB.requestFriendship(client.username,words[2]);
								if(ret.equals("Request sent!"))
									client.dos.writeUTF("friends_add_OK");
								else
									client.dos.writeUTF(ret);
							}
							else
								client.dos.writeUTF("friends_add_ERROR");
							break;
							
						case "reject":
							if(words.length!=2) {
								String ret=server.DB.rejectFriendRequest(client.username,words[2]);
								if(ret.equals("Request removed"))
									client.dos.writeUTF("friends_reject_OK");
								else
									client.dos.writeUTF("friends_reject_ERROR");
							}
							else
								client.dos.writeUTF("friends_reject_ERROR");
							break;
							
						case "remove":
							if(words.length!=2) {
								String ret=server.DB.removeFriend(client.username,words[2]);
								if(ret.equals(words[2] + " removed from friends list"))
									client.dos.writeUTF("friends_remove_OK");
								else
									client.dos.writeUTF("friends_remove_ERROR");
							}
							else
								client.dos.writeUTF("friends_remove_ERROR");
							break;
							
						case "accept":
							if(words.length!=2) {
								String ret=server.DB.acceptFriendship(client.username,words[2]);
								if(ret.equals("Accepted " + words[2] + "as friend"))
									client.dos.writeUTF("friends_accept_OK");
								else
									client.dos.writeUTF("friends_accept_ERROR");
							}
							else
								client.dos.writeUTF("friends_accept_ERROR");
							break;
							
						case "invite":
							if(words.length!=2) {
								Client Target=Server_Handler.Is_Player_Online(words[2]);
								if(Target!=null){	//Might need to check if players are still friends TODO
									client.Send_Invite_Message(Target);
									client.dos.writeUTF("friends_invite_online");
								}
								else
									client.dos.writeUTF("friends_invite_ERROR");
							}
							else
								client.dos.writeUTF("friends_invite_ERROR");
							break;
							
						case "invited":
							if(words.length>=4) {
								Client Target=Server_Handler.Is_Player_Online(words[3]);
								if(Target!=null){
									if(words[2].equals("get")){
										Target.Cancel_Timeout();
									}
									else if(words[2].equals("decline")){
										client.dos.writeUTF("friends_invited_OK");
										Target.dos.writeUTF("friends_invite_decline");
									}
									else if(words[2].equals("accept")){
										client.dos.writeUTF("friends_invited_OK");
										Target.dos.writeUTF("friends_invite_accept");
										ArrayList<Client> tmp=new ArrayList<Client>();
										tmp.add(client);
										tmp.add(Target);
										GameHandler g = new GameHandler(tmp);
										g.start();
									}
								}
								else
									client.dos.writeUTF("friends_invite_ERROR");
							}
							break;
							
						default:
								client.dos.writeUTF("friends_invite_ERROR");
								break;
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
			
			if(client.game!=null){
				if(client.Playing)
					DB.incrementPlayedGame(client.username,false);
				client.game.Remove_Client(client);
				client.Game_Ended();
			}
			
			if(client.InQueue) {
				Server_Handler.Remove_From_Queue(client.username);
				client.RemoveFromQueue();
			}
				
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean IsGameFound() {
		return client.GetGameFound();
	}

}
