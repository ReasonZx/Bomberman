package server;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import java.net.*;
import java.sql.*;

public class ClientHandler extends Thread {

	private Client client;
	private ArrayList<Client> userlist;

	public ClientHandler(Client client, ArrayList<Client> userlist) {
		this.client = client;
		this.userlist = userlist;
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
					System.out.println("Client " + this.client.socket + " sends exit...");
					System.out.println("Closing this connection.");
					this.client.socket.close();
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
					if (words.length == 3) {
						client.login(words[1], words[2], userlist);
					} else
						client.dos.writeUTF("Login and Password mandatory");
					break;
				case "register":
					if (words.length == 4) {
						client.register(words[1], words[2], words[3]);
					} else {
						client.dos.writeUTF("Username, Password and Email mandatory");
					}
					break;

				default:
					client.dos.writeUTF("Invalid input");
					break;
				}
			}
		} catch (IOException | SQLException e) {
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

}
