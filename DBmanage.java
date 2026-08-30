import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBmanage - central SQLite database manager.
 * Creates the connection, the 6 tables (ADMIN, DRIVER, SENDER, RESERVER,
 * SESSION, PARCEL) and seeds default data / credentials on first run.
 */
public class DBmanage {

	private static final String DB_URL = "jdbc:sqlite:cms.db";
	private static Connection conn;

	private DBmanage() {
	}

	/**
	 * Returns the single shared connection, creating it if needed.
	 * Explicitly loads the JDBC driver class so the jar works standalone.
	 */
	public static Connection getConn() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new SQLException("SQLite JDBC driver not found", e);
		}
		if (conn == null || conn.isClosed()) {
			conn = DriverManager.getConnection(DB_URL);
			try (Statement st = conn.createStatement()) {
				st.execute("PRAGMA foreign_keys = ON");
			}
		}
		return conn;
	}

	/**
	 * Closes the shared connection.
	 */
	public static void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	/**
	 * Builds all tables and seeds demo data. Safe to call on every launch.
	 */
	public static void init() throws SQLException {
		try (Connection c = getConn(); Statement st = c.createStatement()) {

			st.executeUpdate("CREATE TABLE IF NOT EXISTS ADMIN ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "username TEXT UNIQUE NOT NULL,"
					+ "password TEXT NOT NULL,"
					+ "name TEXT,"
					+ "email TEXT,"
					+ "address TEXT,"
					+ "phone TEXT,"
					+ "age INTEGER,"
					+ "salary REAL,"
					+ "joiningDate TEXT)");

			st.executeUpdate("CREATE TABLE IF NOT EXISTS DRIVER ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "username TEXT UNIQUE NOT NULL,"
					+ "password TEXT NOT NULL,"
					+ "name TEXT,"
					+ "email TEXT,"
					+ "address TEXT,"
					+ "phone TEXT,"
					+ "age INTEGER,"
					+ "salary REAL,"
					+ "joiningDate TEXT,"
					+ "licenseNo TEXT)");

			st.executeUpdate("CREATE TABLE IF NOT EXISTS SENDER ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "username TEXT UNIQUE NOT NULL,"
					+ "password TEXT NOT NULL,"
					+ "name TEXT,"
					+ "email TEXT,"
					+ "address TEXT,"
					+ "phone TEXT,"
					+ "age INTEGER)");

			st.executeUpdate("CREATE TABLE IF NOT EXISTS RESERVER ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "username TEXT UNIQUE NOT NULL,"
					+ "password TEXT NOT NULL,"
					+ "name TEXT,"
					+ "email TEXT,"
					+ "address TEXT,"
					+ "phone TEXT,"
					+ "age INTEGER)");

			st.executeUpdate("CREATE TABLE IF NOT EXISTS PARCEL ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "trackingNumber TEXT UNIQUE NOT NULL,"
					+ "name TEXT,"
					+ "weight REAL,"
					+ "size REAL,"
					+ "senderAddress TEXT,"
					+ "receiverAddress TEXT,"
					+ "description TEXT,"
					+ "senderId INTEGER,"
					+ "reserverId INTEGER,"
					+ "createdAt TEXT,"
					+ "FOREIGN KEY(senderId) REFERENCES SENDER(id),"
					+ "FOREIGN KEY(reserverId) REFERENCES RESERVER(id))");

			st.executeUpdate("CREATE TABLE IF NOT EXISTS SESSION ("
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "trackingNumber TEXT NOT NULL UNIQUE,"
					+ "status TEXT,"
					+ "deliveryDate TEXT,"
					+ "collectionDate TEXT,"
					+ "driverId INTEGER,"
					+ "FOREIGN KEY(trackingNumber) REFERENCES PARCEL(trackingNumber),"
					+ "FOREIGN KEY(driverId) REFERENCES DRIVER(id))");
		}
		seed();
	}

	/**
	 * Inserts demo users, credentials and parcels if the tables are empty.
	 */
	private static void seed() throws SQLException {
		try (Connection c = getConn()) {
			seedAdmin(c);
			seedDriver(c);
			seedSender(c);
			seedReserver(c);
			seedParcel(c);
		}
	}

	private static boolean hasRows(Connection c, String table) throws SQLException {
		try (Statement st = c.createStatement();
				var rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
			return rs.getInt(1) > 0;
		}
	}

	private static void seedAdmin(Connection c) throws SQLException {
		if (hasRows(c, "ADMIN")) {
			return;
		}
		try (PreparedStatement ps = c.prepareStatement(
				"INSERT INTO ADMIN(username,password,name,email,address,phone,age,salary,joiningDate) "
				+ "VALUES(?,?,?,?,?,?,?,?,?)")) {
			ps.setString(1, "admin");
			ps.setString(2, "admin123");
			ps.setString(3, "Alice Admin");
			ps.setString(4, "admin@cms.com");
			ps.setString(5, "1 HQ Lane, Colombo");
			ps.setString(6, "011-2223334");
			ps.setInt(7, 35);
			ps.setDouble(8, 150000);
			ps.setString(9, "2020-01-10");
			ps.executeUpdate();
		}
	}

	private static void seedDriver(Connection c) throws SQLException {
		if (hasRows(c, "DRIVER")) {
			return;
		}
		try (PreparedStatement ps = c.prepareStatement(
				"INSERT INTO DRIVER(username,password,name,email,address,phone,age,salary,joiningDate,licenseNo) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?)")) {
			ps.setString(1, "driver");
			ps.setString(2, "driver123");
			ps.setString(3, "Bob Driver");
			ps.setString(4, "driver@cms.com");
			ps.setString(5, "22 Depot Rd, Kandy");
			ps.setString(6, "081-5556677");
			ps.setInt(7, 29);
			ps.setDouble(8, 90000);
			ps.setString(9, "2021-06-15");
			ps.setString(10, "DL-2021-0044");
			ps.executeUpdate();
		}
	}

	private static void seedSender(Connection c) throws SQLException {
		if (hasRows(c, "SENDER")) {
			return;
		}
		try (PreparedStatement ps = c.prepareStatement(
				"INSERT INTO SENDER(username,password,name,email,address,phone,age) "
				+ "VALUES(?,?,?,?,?,?,?)")) {
			ps.setString(1, "sender");
			ps.setString(2, "sender123");
			ps.setString(3, "Carol Sender");
			ps.setString(4, "sender@cms.com");
			ps.setString(5, "7 Market St, Galle");
			ps.setString(6, "091-3334455");
			ps.setInt(7, 27);
			ps.executeUpdate();

			ps.setString(1, "sender2");
			ps.setString(2, "sender456");
			ps.setString(3, "David Sender");
			ps.setString(4, "david@cms.com");
			ps.setString(5, "15 Beach Rd, Negombo");
			ps.setString(6, "031-5566778");
			ps.setInt(7, 31);
			ps.executeUpdate();
		}
	}

	private static void seedReserver(Connection c) throws SQLException {
		if (hasRows(c, "RESERVER")) {
			return;
		}
		try (PreparedStatement ps = c.prepareStatement(
				"INSERT INTO RESERVER(username,password,name,email,address,phone,age) "
				+ "VALUES(?,?,?,?,?,?,?)")) {
			ps.setString(1, "reserver");
			ps.setString(2, "reserver123");
			ps.setString(3, "Eve Reserver");
			ps.setString(4, "reserver@cms.com");
			ps.setString(5, "3 Lake Rd, Jaffna");
			ps.setString(6, "021-7788990");
			ps.setInt(7, 24);
			ps.executeUpdate();
		}
	}

	private static void seedParcel(Connection c) throws SQLException {
		if (hasRows(c, "PARCEL")) {
			return;
		}
		// Sample parcels: [tracking, name, weight, size, senderAddr, receiverAddr,
		// description, senderId, reserverId, createdAt, status, deliveryDate, driverId]
		Object[][] data = {
			{ "CMS000001", "Laptop Gift", 2.4, 40,
				"7 Market St, Galle", "3 Lake Rd, Jaffna",
				"Fragile electronics", 1, 1, "2026-08-25", "In Transit", "2026-09-02", 1 },
			{ "CMS000002", "Wedding Cake", 3.1, 55,
				"15 Beach Rd, Negombo", "22 Hill St, Kurunegala",
				"Keep refrigerated", 2, 1, "2026-08-26", "Picked Up", "2026-08-31", 1 },
			{ "CMS000003", "Books Bundle", 5.6, 60,
				"7 Market St, Galle", "44 Lake Rd, Jaffna",
				"Box of hardcover books", 1, 1, "2026-08-27", "Registered", "2026-09-05", 0 },
			{ "CMS000004", "Sneakers", 1.2, 35,
				"15 Beach Rd, Negombo", "5 Temple Rd, Anuradhapura",
				"Sport shoes", 2, 1, "2026-08-22", "Out for Delivery", "2026-08-28", 1 },
			{ "CMS000005", "Office Documents", 0.6, 20,
				"7 Market St, Galle", "9 Queen St, Colombo",
				"Confidential files", 1, 1, "2026-08-20", "Delivered", "2026-08-24", 1 },
		};

		try (PreparedStatement ps = c.prepareStatement(
				"INSERT INTO PARCEL(trackingNumber,name,weight,size,senderAddress,receiverAddress,description,senderId,reserverId,createdAt) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?)");
				PreparedStatement s = c.prepareStatement(
						"INSERT INTO SESSION(trackingNumber,status,deliveryDate,collectionDate,driverId) "
						+ "VALUES(?,?,?,?,?)")) {

			for (Object[] d : data) {
				ps.setString(1, (String) d[0]);
				ps.setString(2, (String) d[1]);
				ps.setDouble(3, ((Number) d[2]).doubleValue());
				ps.setDouble(4, ((Number) d[3]).doubleValue());
				ps.setString(5, (String) d[4]);
				ps.setString(6, (String) d[5]);
				ps.setString(7, (String) d[6]);
				ps.setInt(8, ((Number) d[7]).intValue());
				ps.setInt(9, ((Number) d[8]).intValue());
				ps.setString(10, (String) d[9]);
				ps.executeUpdate();

				s.setString(1, (String) d[0]);
				s.setString(2, (String) d[10]);
				s.setString(3, (String) d[11]);
				s.setString(4, (String) d[9]);
				int driverId = ((Number) d[12]).intValue();
				if (driverId > 0) {
					s.setInt(5, driverId);
				} else {
					s.setNull(5, java.sql.Types.INTEGER);
				}
				s.executeUpdate();
			}
		}
	}
}
