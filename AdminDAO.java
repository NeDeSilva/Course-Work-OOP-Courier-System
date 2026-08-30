import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AdminDAO - data access for the ADMIN table.
 */
public class AdminDAO extends CoreDAO {

	public boolean isValid(String username, String password) throws SQLException {
		return countRows(
				"SELECT COUNT(*) FROM ADMIN WHERE username=? AND password=?",
				username, password) > 0;
	}

	public Admin findByCredentials(String username, String password) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement(
				"SELECT * FROM ADMIN WHERE username=? AND password=?")) {
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

	private Admin map(ResultSet rs) throws SQLException {
		Admin a = new Admin(
				rs.getString("username"),
				rs.getString("password"),
				rs.getString("name"),
				rs.getString("email"),
				rs.getString("address"),
				rs.getString("phone"),
				rs.getInt("age"),
				rs.getDouble("salary"),
				rs.getString("joiningDate"));
		a.setId(rs.getInt("id"));
		return a;
	}
}
