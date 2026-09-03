import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ParcelDAO - data access for the PARCEL table.
 */
public class ParcelDAO extends CoreDAO {

	/**
	 * Adds a new parcel and its initial tracking session.
	 * Returns the generated tracking number.
	 */
	public String add(Parcel p, String initialStatus) throws SQLException {
		Connection c = getConn();
		String tracking = nextTracking("CMS");
		String sql = "INSERT INTO PARCEL(trackingNumber,name,weight,size,senderAddress,"
				+ "receiverAddress,description,senderId,reserverId,createdAt) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?)";
		try (PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, tracking);
			ps.setString(2, p.getName());
			ps.setDouble(3, p.getWeight());
			ps.setDouble(4, p.getSize());
			ps.setString(5, p.getSenderAddress());
			ps.setString(6, p.getReceiverAddress());
			ps.setString(7, p.getDescription());
			if (p.getSenderId() > 0) {
				ps.setInt(8, p.getSenderId());
			} else {
				ps.setNull(8, java.sql.Types.INTEGER);
			}
			if (p.getReserverId() > 0) {
				ps.setInt(9, p.getReserverId());
			} else {
				ps.setNull(9, java.sql.Types.INTEGER);
			}
			ps.setString(10, java.time.LocalDate.now().toString());
			ps.executeUpdate();
		}
		new SessionDAO().create(new Session(tracking, initialStatus, "", "", 0));
		return tracking;
	}

	/**
	 * Looks up a parcel by its tracking number, or null if not found.
	 */
	public Parcel findByTracking(String trackingNumber) throws SQLException {
		Connection c = getConn();
		try (var ps = c.prepareStatement("SELECT * FROM PARCEL WHERE trackingNumber=?")) {
			ps.setString(1, trackingNumber);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return map(rs);
				}
			}
		}
		return null;
	}

	/**
	 * Lists all active (not finalised) parcels in the system.
	 */
	public List<Parcel> listAllActive() throws SQLException {
		List<Parcel> list = new ArrayList<>();
		Connection c = getConn();
		String sql = "SELECT p.* FROM PARCEL p "
				+ "JOIN SESSION s ON s.trackingNumber = p.trackingNumber "
				+ "WHERE s.status NOT IN ('Delivered','Collected') ORDER BY p.id";
		try (var ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(map(rs));
			}
		}
		return list;
	}

	/**
	 * Lists parcels sent by a particular sender.
	 */
	public List<Parcel> listBySender(int senderId) throws SQLException {
		List<Parcel> list = new ArrayList<>();
		Connection c = getConn();
		try (var ps = c.prepareStatement("SELECT * FROM PARCEL WHERE senderId=? ORDER BY id")) {
			ps.setInt(1, senderId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(map(rs));
				}
			}
		}
		return list;
	}

	/**
	 * Links a parcel to a reserver (tracker) account.
	 */
	public void setReserver(String trackingNumber, int reserverId) throws SQLException {
		execute("UPDATE PARCEL SET reserverId=? WHERE trackingNumber=?",
				reserverId, trackingNumber);
	}

	public int countAll() throws SQLException {
		return countRows("SELECT COUNT(*) FROM PARCEL");
	}

	private Parcel map(ResultSet rs) throws SQLException {
		Parcel p = new Parcel(
				rs.getString("trackingNumber"),
				rs.getString("name"),
				rs.getDouble("weight"),
				rs.getDouble("size"),
				rs.getString("senderAddress"),
				rs.getString("receiverAddress"),
				rs.getString("description"),
				rs.getInt("senderId"),
				rs.getInt("reserverId"));
		p.setId(rs.getInt("id"));
		p.setCreatedAt(rs.getString("createdAt"));
		return p;
	}
}
