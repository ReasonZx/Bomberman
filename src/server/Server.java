package server;

import java.io.*; 
import java.util.*; 
import java.net.*; 


public class Server implements Runnable{
	private ArrayList<Client> userlist = new ArrayList<>();
	private ServerSocket ss; 
	private ArrayList<Client> queue = new ArrayList<Client>();
	public DB database = new DB();
	
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
		
		Timer tt = new Timer();
		tt.schedule(new Queue_Handler(),0, 500);
			
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
	
	public int Add_To_Queue(Client x) {
		queue.add(x);
		return queue.size()-1;
	}
	
	public void Remove_From_Queue(int pos) {
		queue.remove(pos);
	}
	
	private class Queue_Handler extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(queue.size()>=2) {
				for(int i=0;i<10;i++){
					if(queue.size()<2)
						break;
					ArrayList<Client> tmp =new ArrayList<Client>();	
					try {
						
						for(i=0;i<queue.size();i++) {
							queue.get(i).Send_Game_Found_Message();
							tmp.add(queue.get(i));
							if(i==3)
								break;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					GameHandler g = new GameHandler(tmp);
					g.start();
					queue.remove(0);
					queue.remove(0);
				}
			}
		}
		
	}
	
	public Client Is_Player_Online(String s) {
		for(int i=0;i<userlist.size();i++) {
			if(userlist.get(i).username!=null)
				if(userlist.get(i).username.equals(s))
					return userlist.get(i);
		}
		return null;
	}
}
