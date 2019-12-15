import java.io.*;
import java.net.*;

public class ServerHandler {

	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	InetAddress ip;

	public ServerHandler() throws IOException {
        // establish the connection with server port 5056 
		this.ip = InetAddress.getByName("localhost"); 
        this.socket = new Socket(ip, 5056);
        // obtaining input and out streams 
        this.dis = new DataInputStream(socket.getInputStream()); 
        this.dos = new DataOutputStream(socket.getOutputStream()); 
	}

	public String request(String req) throws IOException {
		try {

			dos.writeUTF(req);
			System.out.println("sent" + req);
			// the following loop performs the exchange of
			// information between client and client handler
//			while (true) {
//				String received = dis.readUTF();
//				System.out.println(received);
//				// System.out.println(dis.readUTF());
//				// printing date or time as requested by client
//				// String received = dis.readUTF();
//				// System.out.println(received);
//				if (received.equals("ok")) {
//
//					return "ok";
//				} else if (received.equals("logout")) {
//					dis.close();
//					dos.close();
//					return "ok";
//				}
//
//				else if (received.equals("not")) {
//					return "not";
//				} else {
//					return received;
//				}
//			}

			// closing resources

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "not";
	}

}
