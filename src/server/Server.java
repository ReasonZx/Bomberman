package server;

import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 


public class Server {

	public static void main(String[] args) throws IOException {
		final ArrayList<Client> userlist = new ArrayList<>();
		ServerSocket ss = new ServerSocket(5056); 

	     // running infinite loop for getting 
	     // client request 
	     while (true)  
	     { 
	         Socket s = null; 
	           
	         try 
	         {
	             // socket object to receive incoming client requests 
	             s = ss.accept(); 
	             
	             System.out.println("A new client is connected : " + s); 
	               
	             // obtaining input and out streams 
	             DataInputStream dis = new DataInputStream(s.getInputStream()); 
	             DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
	              
	             Client client = new Client(s, dis, dos);
	             userlist.add(client);
	             
	             System.out.println("Assigning new thread for this client"); 

	             // create a new thread object 
	             Thread t = new ClientHandler(client, userlist); 

	             // Invoking the start() method 
	             t.start(); 
	               
	         } 
	         catch (Exception e){ 
	        	 ss.close();
	             s.close(); 
	             e.printStackTrace(); 
	         } 
	     } 

		
		
	}

}
