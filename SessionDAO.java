import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SessionDAO - data access for the SESSION (tracking) table.
 */
public class SessionDAO extends CoreDAO {

	public void create(Session s) throws SQLException {
		Connection c = getConn();
		String sql = "INSERT INTO SESSION(trackingNumber,status,deliveryDate,collectionDate,driverId) "
				+ "VALUES(?,?,?,?,?)";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, s.getTrackingNumber());
			ps.setString(2, s.getStatus());
			ps.setString(3, s.getDeliveryDate());
			ps.setString(4, s.getCollectionDate());
			if (s.getDriverId() > 0) {
				ps.setInt(5, s.getDriverId());
			} else {
				ps.setNull(5, java.sql.Types.INTEGER);
			}
			ps.executeUpdate();
		}
	}

	public Session findByTracking(String trackingNumber) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement("SELECT * FROM SESSION WHERE trackingNumber=?")) {
			ps.setString(1, trackingNumber);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return map(rs);
				}
			}
		}
		return null;
	}

	public void updateStatus(String trackingNumber, String status) throws SQLException {
		execute("UPDATE SESSION SET status=? WHERE trackingNumber=?", status, trackingNumber);
	}

	public void assignDriver(String trackingNumber, int driverId) throws SQLException {
		execute("UPDATE SESSION SET driverId=? WHERE trackingNumber=?", driverId, trackingNumber);
	}

	public List<String> distinctStatuses() {
		List<String> statuses = new ArrayList<>();
		statuses.add("Registered");
		statuses.add("Picked Up");
		statuses.add("In Transit");
		statuses.add("Out for Delivery");
		statuses.add("Delivered");
		statuses.add("Collected");
		return statuses;
	}

	private Session map(ResultSet rs) throws SQLException {
		Session s = new Session(
				rs.getString("trackingNumber"),
				rs.getString("status"),
				rs.getString("deliveryDate"),
				rs.getString("collectionDate"),
				rs.getInt("driverId"));
		s.setId(rs.getInt("id"));
		return s;
	}
}
