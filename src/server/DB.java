package server;

import java.sql.*;

public class DB {

	private static Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://dbm.fe.up.pt:5432/sibd1807?currentSchema=Bomberman",
					"sibd1807", "diogo");
			System.out.println("Access Granted");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}

	public static String login(String username, String pw) throws SQLException {
		Connection conn = connect();
		ResultSet rs;
		PreparedStatement data;

		String query = "SELECT password FROM users WHERE username = ?";
		data = conn.prepareStatement(query);
		data.setString(1, username);

		// Executar query
		rs = data.executeQuery();

		// Se query vazia = utilizador no existe na BD
		if (!rs.next()) {
			// Utilizador no registado na BD
			System.out.println("User nao registado na BD");
			return "User not found";
		}

		// Se a password obtida diferente da encontrada na bd = PW errada
		if (!rs.getString(1).equals(pw)) {
			// Password errada
			System.out.println("Pw errada");
			return "Wrong Password";
		}

		data.close();
		conn.close();

		System.out.println("Login Efectuado");
		return "Logged in";

	}

}
