package test;

import static org.junit.Assert.assertEquals;

import java.sql.*;
import java.util.ArrayList;

import org.junit.Test;
import server.DB;

public class Query_test {

	@Test
	public void friendsListTest() throws SQLException {
		ArrayList<String> friends = new ArrayList<>();

		friends = DB.getFriendsList("root");
		System.out.println(friends);

	}

	@Test
	public void isFriendTest() throws SQLException {
		Connection conn = DB.connect();

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

		String rs = DB.requestFriendship("diogo", "joao");
		assertEquals("Request sent!", rs);

	}

	@Test
	public void login() throws SQLException {

		Connection conn = DB.connect();
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

		Connection conn = DB.connect();
		if (conn == null)
			System.out.println("Can't connect to database");

		if (DB.checkUser(conn, "diogo")) {
			System.out.println("User Already Registered");
		}

		PreparedStatement data;

		String query = "INSERT INTO users (username,password,email) VALUES (?,?,?)";
		data = conn.prepareStatement(query);
		data.setString(1, "diogo");
		data.setString(2, "123");
		data.setString(3, "null");

		assertEquals(1, data.executeUpdate());

		data.close();
		conn.close();

	}

	@Test
	public void requestFriendshipTest() throws SQLException {
		String rs = DB.requestFriendship("root", "asdasdasdasd");
		assertEquals("User does not exist", rs);

		rs = DB.requestFriendship("root", "diogo");
		assertEquals("Already friends", rs);

		rs = DB.requestFriendship("joao", "diogo");
		assertEquals("Already requested", rs);

		rs = DB.requestFriendship("diogo", "joao");
		assertEquals("Request sent!", rs);
	}

	@Test
	public void acceptFriendshipTest() throws SQLException {

		String rs = DB.acceptFriendship("root", "miguel");
		assertEquals("Accepted " + "root" + "as friend", rs);
	}

	@Test
	public void incrementPlayedGame() throws SQLException {
		Connection conn = DB.connect();
		PreparedStatement data;

		String query = "UPDATE users\r\n" + "SET \"GamesPlayed\" = \"GamesPlayed\" + 1\r\n" + "WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, "root");

		assertEquals(1, data.executeUpdate());

	}

	@Test
	public void checkPLayedGames() throws SQLException {

		assertEquals(3, DB.getPlayedGames("diogo"));
	}

}
