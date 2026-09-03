import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DriverDAO - data access for the DRIVER table.
 */
public class DriverDAO extends CoreDAO {

	public boolean isValid(String username, String password) throws SQLException {
		return countRows(
				"SELECT COUNT(*) FROM DRIVER WHERE username=? AND password=?",
				username, password) > 0;
	}

	public Driver findByCredentials(String username, String password) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement(
				"SELECT * FROM DRIVER WHERE username=? AND password=?")) {
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

	public List<Driver> listAll() throws SQLException {
		List<Driver> list = new ArrayList<>();
		Connection c = getConn();
		try (var ps = c.prepareStatement("SELECT * FROM DRIVER");
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(map(rs));
			}
		}
		return list;
	}

	public String nameOf(int driverId) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement("SELECT name FROM DRIVER WHERE id=?")) {
			ps.setInt(1, driverId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString(1);
				}
			}
		}
		return "Unassigned";
	}

	private Driver map(ResultSet rs) throws SQLException {
		Driver d = new Driver(
				rs.getString("username"),
				rs.getString("password"),
				rs.getString("name"),
				rs.getString("email"),
				rs.getString("address"),
				rs.getString("phone"),
				rs.getInt("age"),
				rs.getDouble("salary"),
				rs.getString("joiningDate"),
				rs.getString("licenseNo"));
		d.setId(rs.getInt("id"));
		return d;
	}
}
