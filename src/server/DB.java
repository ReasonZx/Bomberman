package server;

import java.sql.*;
import java.util.ArrayList;

public class DB {

	private static Connection connect() {
		Connection conn = null;
		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException ex) {
				System.out.println("Error: unable to load driver class!");
				System.exit(1);
			}
			conn = DriverManager.getConnection("jdbc:postgresql://dbm.fe.up.pt:5432/sibd1807", "sibd1807", "diogo");
			conn.setSchema("Bomberman");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}

	public static String login(String username, String pw) throws SQLException {
		Connection conn = connect();
		if (conn == null)
			return "Can't connect to database";
		ResultSet rs;
		PreparedStatement data;

		String query = "SELECT password FROM users WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, username);

		// Executar query
		rs = data.executeQuery();

		// Se query vazia = utilizador no existe na BD
		if (!rs.next()) {
			// Utilizador n�o registado na BD
			return "User not found";
		}

		// Se a password obtida diferente da encontrada na bd = PW errada
		if (!rs.getString(1).equals(pw)) {
			// Password errada
			return "Wrong Password";
		}

		data.close();
		conn.close();

		return "Logged in";

	}

	public static String register(String username, String pw, String email) throws SQLException {

		Connection conn = connect();
		if (conn == null)
			return "Can't connect to database";

		if (checkUser(conn, username)) {
			return "User Already Registered";
		}

		PreparedStatement data;

		String query = "INSERT INTO users (username,password,email) VALUES (?,?,?)";
		data = conn.prepareStatement(query);
		data.setString(1, username);
		data.setString(2, pw);
		data.setString(3, email);

		try {
			data.executeUpdate();
		} catch (Exception e) {

		}

		data.close();
		conn.close();

		return "Registered Successfully";

	}

	private static boolean checkUser(Connection conn, String username) throws SQLException {

		PreparedStatement data;
		ResultSet rs;
		String query = "SELECT username FROM users WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, username);

		// Executar query
		rs = data.executeQuery();

		// Se query vazia = utilizador no existe na BD
		if (!rs.next()) {
			// Utilizador j� registado na BD
			return false;
		}

		return true;
	}

	public static int isFriend(String username, String friend) throws SQLException {
		Connection conn = connect();

		ResultSet rs;
		PreparedStatement data;

		String query = "SELECT confirmed FROM friends WHERE username = ? AND friend = ?";
		data = conn.prepareStatement(query);
		data.setString(1, username);
		data.setString(2, friend);

		rs = data.executeQuery();
		if (!rs.next()) {
			return -1;
		}

		if (rs.getBoolean(1) == true) {
			return 1;
		}

		return 0;
	}

	public static String requestFriendship(String username, String friend) throws SQLException {
		Connection conn = connect();
		PreparedStatement data;

		// Checks if friend username exists
		if (!checkUser(conn, friend))
			return "User does not exist";
		// Checks if they are already friends
		if (isFriend(username, friend) == 1)
			return "Already friends";
		// Checks if the request has been sent
		if (isFriend(username, friend) == 0) {
			return "Already requested";
		}

		// Checks if the friend has already sent the user a friend request
		if (isFriend(friend, username) == 0) {
			System.out.println("Diogo");
			// If so, just accept the request
			acceptFriendship(username, friend);
			return "Request sent!";
		}

		String query = "INSERT INTO friends (username,friend,confirmed) VALUES (?,?,?)";
		data = conn.prepareStatement(query);
		data.setString(1, username);
		data.setString(2, friend);
		data.setBoolean(3, false);

		data.executeUpdate();

		return "Request sent!";
	}

	public static String acceptFriendship(String username, String friend) throws SQLException {
		Connection conn = connect();
		PreparedStatement data;

		String query = "UPDATE friends SET confirmed = ? WHERE username = ? AND friend = ?";
		data = conn.prepareStatement(query);
		data.setBoolean(1, true);
		data.setString(2, friend);
		data.setString(3, username);

		data.executeUpdate();
		query = "INSERT INTO friends (username,friend,confirmed) VALUES (?,?,?)";
		data = conn.prepareStatement(query);
		data.setString(1, username);
		data.setString(2, friend);
		data.setBoolean(3, true);

		data.executeUpdate();
		return "Accepted " + friend + "as friend";
	}

	public static ArrayList<String> getFriendsList(String username) throws SQLException {

		ArrayList<String> friends = new ArrayList<>();
		
		Connection conn = connect();
		PreparedStatement data;
		ResultSet rs;
		
		
		String query = "SELECT friend FROM friends WHERE username = ? AND confirmed = true";
		data = conn.prepareStatement(query);
		data.setString(1, username);
		
		rs = data.executeQuery();
		while (rs.next()) {
			friends.add(rs.getString(1));
		}
		
		query = "SELECT username FROM friends WHERE friend = ? AND confirmed = false";
		data = conn.prepareStatement(query);
		data.setString(1, username);
		
		rs = data.executeQuery();
		while (rs.next()) {
			friends.add(rs.getString(1));
		}
		
		return friends;

	}
	
	public static String removeFriend(String username, String friend) throws SQLException {
		Connection conn = connect();
		PreparedStatement data;

		String query = "DELETE FROM friends WHERE username = ? AND friend = ? AND confirmed = ?";
		data = conn.prepareStatement(query);
		data.setBoolean(3, true);
		data.setString(2, friend);
		data.setString(1, username);
		
		data.executeUpdate();

		
		query = "DELETE FROM friends WHERE username = ? AND friend = ? AND confirmed = ?";
		data = conn.prepareStatement(query);
		data.setBoolean(3, true);
		data.setString(2, username);
		data.setString(1, friend);
		
		int rs = data.executeUpdate();
		
		if(rs > 0) {
			return friend + " removed from friends list";
			}
		else {
			return "Error removing friend";
		}
	}
	
	public static String rejectFriendRequest(String username, String friend) throws SQLException {
		Connection conn = connect();
		PreparedStatement data;

		String query = "DELETE FROM friends WHERE username = ? AND friend = ? AND confirmed = ?";
		data = conn.prepareStatement(query);
		data.setBoolean(3, false);
		data.setString(2, username);
		data.setString(1, friend);

		int rs = data.executeUpdate();
		
		if(rs > 0) {
			return "Request removed";
		}
		else {
			return "Request not found";
		}
	}

}
