package server;

import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 


public class Server implements Runnable{
	private ArrayList<Client> userlist = new ArrayList<>();
	private ServerSocket ss; 
	private ArrayList<Client> queue = new ArrayList<Client>();
	
	Server() throws IOException{
	}
	
	public static void main(String[] args) throws IOException{
	     // running infinite loop for getting 
	     // client request 
		 Server srv = new Server();
		 srv.run();
	}
	
	public void run() {
		try {
			ss = new ServerSocket(5056);
		}catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 
		
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
	             
	             System.out.println("Assigning new thread for this client"); 

	             // create a new thread object 
	             Thread t = new ClientHandler(client, this); 

	             // Invoking the start() method 
	             t.start(); 
	               
	         } 
	         catch (Exception e){ 
	        	 try {
					ss.close();
					s.close(); 
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	             e.printStackTrace(); 
	         } 
	     } 
	}
	
	public ArrayList<Client>Get_UserList(){
		return userlist;
	}
	
	public void Add_To_Queue(Client x) {
		queue.add(x);
	}

}
