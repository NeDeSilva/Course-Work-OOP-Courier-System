import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ReserverDAO - data access for the RESERVER table.
 */
public class ReserverDAO extends CoreDAO {

	public boolean isValid(String username, String password) throws SQLException {
		return countRows(
				"SELECT COUNT(*) FROM RESERVER WHERE username=? AND password=?",
				username, password) > 0;
	}

	public Reserver findByCredentials(String username, String password) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement(
				"SELECT * FROM RESERVER WHERE username=? AND password=?")) {
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

	/**
	 * Returns the id of a reserver, creating the account if it does not exist.
	 * This lets a sender track a parcel even without a pre-made reserver.
	 */
	public int getOrCreate(String username) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement("SELECT id FROM RESERVER WHERE username=?")) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		try (PreparedStatement ps = c.prepareStatement(
				"INSERT INTO RESERVER(username,password,name,email,address,phone,age) "
				+ "VALUES(?,?,?,?,?,?,?)",
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, username);
			ps.setString(2, username + "@track");
			ps.setString(3, username);
			ps.setString(4, "");
			ps.setString(5, "");
			ps.setString(6, "");
			ps.setInt(7, 0);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return -1;
	}

	private Reserver map(ResultSet rs) throws SQLException {
		Reserver r = new Reserver(
				rs.getString("username"),
				rs.getString("password"),
				rs.getString("name"),
				rs.getString("email"),
				rs.getString("address"),
				rs.getString("phone"),
				rs.getInt("age"));
		r.setId(rs.getInt("id"));
		return r;
	}
}
