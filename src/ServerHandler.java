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
		String received = null;
		try {
			dos.writeUTF(req);
			while (received == null) {
				received = dis.readUTF();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return received;
	}

}
