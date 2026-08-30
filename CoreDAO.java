import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * CoreDAO - base Data Access Object that provides the shared connection and
 * small query helpers used by every specific DAO.
 */
public class CoreDAO {

	protected Connection getConn() throws SQLException {
		return DBmanage.getConn();
	}

	/**
	 * Runs a simple count query and returns the number of rows.
	 */
	protected int countRows(String sql, Object... params) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			try (ResultSet rs = ps.executeQuery()) {
				return rs.getInt(1);
			}
		}
	}

	/**
	 * Executes an INSERT, UPDATE or DELETE and returns rows affected.
	 */
	protected int execute(String sql, Object... params) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement(sql)) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
			return ps.executeUpdate();
		}
	}

	/**
	 * Generates a fresh, unique tracking number in the form CMS########.
	 */
	protected String nextTracking(String prefix) throws SQLException {
		Connection c = getConn();
		try (Statement st = c.createStatement();
				ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM PARCEL")) {
			int n = rs.getInt(1) + 1;
			return String.format("%s%06d", prefix, n);
		}
	}
}
