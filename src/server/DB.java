package server;

import java.sql.*;
import java.util.ArrayList;

public class DB {

	
	/**
	 * Connects and logins to the database
	 * @return The connection to the database
	 */
	public static Connection connect() {
		Connection conn = null;
		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException ex) {
				System.out.println("Error: Can't connect to database!");
				return null;
			}
			conn = DriverManager.getConnection("jdbc:postgresql://dbm.fe.up.pt:5432/sibd1807", "sibd1807", "diogo");
			conn.setSchema("Bomberman");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}

	
	/**
	 * Checks if the user and password match to the database entry
	 * @param username The username of the user which will log in
	 * @param pw The password of the user
	 * @return The message which will be sent to the client. Can be:
	 * <p>Can't connect to database
	 * <p>User not found
	 * <p>Wrong Password
	 * <p>Logged in
	 * @throws SQLException 
	 */
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

	/** 
	 * Inserts a user into the database
	 * @param username The desired username
	 * @param pw The desired password
	 * @param email User's email
	 * @return The message which will be sent to the client. Can be:
	 * <p>Can't connect to database
	 * <p>User Already Registered
	 * <p>Registered Successfully
	 * @throws SQLException
	 */
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

	/**
	 * Checks if user is registered
	 * @param conn The connection to the database
	 * @param username The username to check
	 * @return True if username is already in the database. False otherwise.
	 * @throws SQLException
	 */
	public static boolean checkUser(Connection conn, String username) throws SQLException {

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

	/**
	 * Checks if 'friend' is friends with 'username'
	 * @param username Username of the user
	 * @param friend Username of the friend
	 * @return <p>-1 if not friend. <p>0 if friend request is pending. <p>1 if friendship is mutual
	 * @throws SQLException
	 */
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

	/**
	 * Creates a pending request in the database from 'username' to 'friend'
	 * @param username Username of the user asking for friendship
	 * @param friend Username of the friend
	 * @return The message which will be sent to the client. Can be:
	 * <p>User does not exist
	 * <p>Already friends
	 * <p>Already requested
	 * <p>Request sent
	 * @throws SQLException
	 */
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

	/**
	 * Accepts the friendship request
	 * @param username Username of the user who will accept the request
	 * @param friend Username of the friend which sent the request
	 * @return The message which will be sent to the client. Returns "Accepted "  + friend + "as friend":
	 * 
	 * @throws SQLException
	 */
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

	/**
	 * Retrieves the list of the user's friends from the database
	 * @param username The username of the user
	 * @return The list of the user's friends
	 * @throws SQLException
	 */
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

	/**
	 * Deletes from the database the friend relationship specified
	 * @param username The username of the user asking for friend deletion
	 * @param friend The username of the friend which will be removed form the user's friend list
	 * @return The message which will be sent to the client. Can be:
	 * <p>friend + " removed from friends list"
	 * <p>Error removing friend
	 * @throws SQLException
	 */
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

		if (rs > 0) {
			return friend + " removed from friends list";
		} else {
			return "Error removing friend";
		}
	}

	/**
	 * Deletes the pending friendship request from the database
	 * @param username The username of the user who wants to delete the entry
	 * @param friend The username of the friend
	 * @return The message which will be sent to the client. Can be:
	 * <p>Request removed
	 * <p>Request not found
	 * @throws SQLException
	 */
	public static String rejectFriendRequest(String username, String friend) throws SQLException {
		Connection conn = connect();
		PreparedStatement data;

		String query = "DELETE FROM friends WHERE username = ? AND friend = ? AND confirmed = ?";
		data = conn.prepareStatement(query);
		data.setBoolean(3, false);
		data.setString(2, username);
		data.setString(1, friend);

		int rs = data.executeUpdate();

		if (rs > 0) {
			return "Request removed";
		} else {
			return "Request not found";
		}
	}

	/**
	 * Increments the number of games played by 1. If the player won, also increments their won games. Shall be used after a game is completed
	 * @param username The username of the user
	 * @param won If the user won the game
	 * @throws SQLException
	 */
	public static void incrementPlayedGame(String username, boolean won) throws SQLException {

		Connection conn = connect();
		PreparedStatement data;

		String query = 
				"UPDATE users\r\n" + 
				"SET \"GamesPlayed\" = \"GamesPlayed\" + 1\r\n" +
				"WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, username);

		data.executeUpdate();

		if (won) {
			query = 
					"UPDATE users\r\n" + 
					"SET \"GamesWon\" = \"GamesWon\" + 1\r\n" +
					"WHERE username = ?";
			data = conn.prepareStatement(query);
			data.setString(1, username);

			data.executeUpdate();
		}

	}
	
	/**
	 * Retrieves the number of won games from the database
	 * @param username The username of the user
	 * @return The number of won games
	 * @throws SQLException
	 */
	public static int getWonGames(String username) throws SQLException {
		Connection conn = connect();
		PreparedStatement data;
		ResultSet rs;
		String query =
				"SELECT \"GamesWon\" FROM users WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, username);
		rs = data.executeQuery();
		
		if (!rs.next()) {
			return 0;
		}
		
		return rs.getInt(1);
	
	}

	/**
	 * Retrieves the number of played games from the database
	 * @param username The username of the user
	 * @return The number of played games
	 * @throws SQLException
	 */
	public static int getPlayedGames(String username) throws SQLException {

		Connection conn = connect();
		PreparedStatement data;
		ResultSet rs;
		String query =
				"SELECT \"GamesPlayed\" FROM users WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, username);
		rs = data.executeQuery();
		
		if (!rs.next()) {
			return 0;
		}
		
		return rs.getInt(1);
	}
}
