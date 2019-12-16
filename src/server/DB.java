package server;

import java.sql.*;

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
			// Utilizador não registado na BD
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
			data.executeQuery();
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
			// Utilizador já registado na BD
			return false;
		}

		return true;
	}

}
