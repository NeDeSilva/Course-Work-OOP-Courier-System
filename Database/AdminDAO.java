package Database;

/**
 * AdminDAO
 */
public class AdminDAO {

	private java.sql.Connection connection;
	
	public void makefield(){
		
	}

	public boolean authenticate(String username, String password) {
		String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
		try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			try (java.sql.ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		connection = null;
	}

	public void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_system", "root", "");
		} catch (ClassNotFoundException | java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
	
	public java.sql.Connection getConnection() {
		return connection;
	}

	public boolean register(String username, String password) {
		String sql = "INSERT INTO admins (username, password) VALUES (?, ?)";
		try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			int rows = stmt.executeUpdate();
			return rows > 0;
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	
}