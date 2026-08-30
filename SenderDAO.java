import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SenderDAO - data access for the SENDER table.
 */
public class SenderDAO extends CoreDAO {

	public boolean isValid(String username, String password) throws SQLException {
		return countRows(
				"SELECT COUNT(*) FROM SENDER WHERE username=? AND password=?",
				username, password) > 0;
	}

	public Sender findByCredentials(String username, String password) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement(
				"SELECT * FROM SENDER WHERE username=? AND password=?")) {
			ps.setString(1, username);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return map(rs);
				}
			}
		}
		return null;
	}

	public int add(Sender s) throws SQLException {
		Connection c = getConn();
		String sql = "INSERT INTO SENDER(username,password,name,email,address,phone,age) "
				+ "VALUES(?,?,?,?,?,?,?)";
		try (PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, s.getUsername());
			ps.setString(2, s.getPassword());
			ps.setString(3, s.getName());
			ps.setString(4, s.getEmail());
			ps.setString(5, s.getAddress());
			ps.setString(6, s.getPhone());
			ps.setInt(7, s.getAge());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return -1;
	}

	public Sender findById(int id) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement("SELECT * FROM SENDER WHERE id=?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return map(rs);
				}
			}
		}
		return null;
	}

	private Sender map(ResultSet rs) throws SQLException {
		Sender s = new Sender(
				rs.getString("username"),
				rs.getString("password"),
				rs.getString("name"),
				rs.getString("email"),
				rs.getString("address"),
				rs.getString("phone"),
				rs.getInt("age"));
		s.setId(rs.getInt("id"));
		return s;
	}
}
