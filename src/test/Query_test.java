package test;

import static org.junit.Assert.assertEquals;

import java.sql.*;

import org.junit.Test;

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

	@Test
	public void test() throws SQLException {

		Connection conn = connect();
		ResultSet rs;
		PreparedStatement data;

		String query = "SELECT password FROM users WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, "root");

		// Executar query
		rs = data.executeQuery();
		rs.next();
		data.close();
		conn.close();

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

}