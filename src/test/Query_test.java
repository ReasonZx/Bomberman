package test;

import static org.junit.Assert.assertEquals;

import java.sql.*;
import java.util.ArrayList;

import org.junit.Test;
import server.DB;

public class Query_test {

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

	public ArrayList<String> getFriendsList(String username) throws SQLException {

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
		return friends;

	}

	@Test
	public void friendsListTest() throws SQLException {
		ArrayList<String> friends = new ArrayList<>();
		
		friends = getFriendsList("root");
		System.out.println(friends);
		
	}
	private static String requestFriendship(String username, String friend) throws SQLException {
		Connection conn = connect();
		PreparedStatement data;

		// Checks if friend username exists
		if (!checkUser(conn, friend))
			return "User does not exist";
		//Checks if they are already friends
		if (isFriend(username, friend) == 1)
			return "Already friends";
		//Checks if the request has been sent
		if (isFriend(username, friend) == 0) {
			return "Already requested";
		}
		
		//Checks if the friend has already sent the user a friend request
		if (isFriend(friend, username) == 0) {

			//If so, just accept the request
			acceptFriendship(friend, username);
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

	private static int isFriend(String username, String friend) throws SQLException {
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
			// Utilizador já registado na BD
			return false;
		}

		return true;
	}

	@Test
	public void isFriendTest() throws SQLException {
		Connection conn = connect();

		ResultSet rs;
		PreparedStatement data;

		String query = "SELECT confirmed FROM friends WHERE username = ? AND friend = ?";
		data = conn.prepareStatement(query);
		data.setString(1, "root");
		data.setString(2, "diogo");

		rs = data.executeQuery();
		rs.next();

		assertEquals(true, rs.getBoolean(1));

	}

	@Test
	public void test() throws SQLException {

		String rs = requestFriendship("diogo", "joao");
		assertEquals("Request sent!", rs);

	}

	@Test
	public void login() throws SQLException {

		Connection conn = connect();
		ResultSet rs;
		PreparedStatement data;
		String username = "root";
		String password = "1234";

		String query = "SELECT password FROM users WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, username);

		// Executar query
		rs = data.executeQuery();
		rs.next();
		assertEquals(password, rs.getString(1));
		data.close();
		conn.close();

	}

	@Test
	public void register() throws SQLException {

		Connection conn = connect();
		ResultSet rs = null;
		PreparedStatement data;
		String username = "diogo";
		String password = "ola123";
		String email = "santosdiogo97@gmail.com";

		String query = "INSERT INTO users (username,password,email) VALUES (?,?,?)";
		data = conn.prepareStatement(query);
		data.setString(1, username);
		data.setString(2, password);
		data.setString(3, email);

		// Executar query
		try {
			rs = data.executeQuery();
		} catch (Exception e) {
			data.close();
			conn.close();
			return;
		}
		rs.next();
		data.close();
		conn.close();

	}

	@Test
	public void requestFriendshipTest() throws SQLException {
		String rs = requestFriendship("root", "asdasdasdasd");
		assertEquals("User does not exist", rs);
		
		rs = requestFriendship("root", "diogo");
		assertEquals("Already friends", rs);
		
		rs = requestFriendship("joao", "diogo");
		assertEquals("Already requested", rs);
		
		rs = requestFriendship("diogo", "joao");
		assertEquals("Request sent!", rs);
	}

	@Test
	public void acceptFriendshipTest() throws SQLException {
		
		String rs = acceptFriendship("root","miguel");
		assertEquals("Accepted " + "root" + "as friend", rs);
	}
	
	@Test
	public void incrementPlayedGame() throws SQLException{
		Connection conn = connect();
		PreparedStatement data;

		String query = 
				"UPDATE users\r\n" + 
				"SET \"GamesPlayed\" = \"GamesPlayed\" + 1\r\n" +
				"WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, "root");

		data.executeUpdate();

	}
	
	@Test
	public void checkPLayedGames() throws SQLException {
		
		assertEquals(3, DB.getPlayedGames("diogo"));
	}
	
	

}
