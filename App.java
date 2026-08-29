import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Courier Management System (single file).
 *
 * Architecture (one file, many focused components):
 *   Theme       - shared colors / fonts / paddings (the pastel yellow-orange palette)
 *   UI          - reusable helpers that build and style every Swing element
 *   Database    - SQLite connection, schema, seeding, auth + password hashing
 *   HasId, models (Item, Driver, Customer, Admin, Session, StorageBox) - POJOs
 *   CrudDao + six DAOs - generic CRUD contract with a concrete SQL implementation per table
 *   CrudPanel   - reusable master-detail screen (table + add/edit/delete) driven by any CrudDao
 *   SideButton, DashboardPanel, LoginFrame, MainFrame - the screens
 *
 * Run:
 *   javac  -cp lib/sqlite-jdbc.jar -d out App.java
 *   java   -cp "lib/sqlite-jdbc.jar;out" App
 *   (optional smoke test of the data layer: java -cp "lib/sqlite-jdbc.jar;out" App selftest)
 */
class App {
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("selftest")) {
			selfTest();
			return;
		}
		SwingUtilities.invokeLater(() -> {
			Database.init();
			new LoginFrame().setVisible(true);
		});
	}

	// -------- shared look and feel ----------------------------------------

	static final class Theme {
		static final Color BG = new Color(255, 250, 237);
		static final Color CARD = new Color(255, 244, 219);
		static final Color SIDE = new Color(255, 214, 170);
		static final Color ACCENT = new Color(247, 143, 72);
		static final Color ACCENT_DARK = new Color(230, 118, 52);
		static final Color WHITE = Color.WHITE;
		static final Color TEXT = new Color(94, 68, 60);
		static final Color MUTED = new Color(168, 140, 118);
		static final Color LINE = new Color(240, 214, 178);
		static final Color ERROR = new Color(200, 84, 66);
		static final Color GOOD = new Color(96, 150, 90);

		static final Font TITLE = new Font("Segoe UI", Font.BOLD, 26);
		static final Font H1 = new Font("Segoe UI", Font.BOLD, 22);
		static final Font LABEL = new Font("Segoe UI", Font.BOLD, 13);
		static final Font BODY = new Font("Segoe UI", Font.PLAIN, 14);
		static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);
		static final Font BUTTON = new Font("Segoe UI", Font.BOLD, 14);
	}

	static final class UI {
		private UI() {}

		static JLabel h1(String text) {
			JLabel l = new JLabel(text);
			l.setFont(Theme.H1);
			l.setForeground(Theme.TEXT);
			return l;
		}

		static JLabel subtle(String text) {
			JLabel l = new JLabel(text);
			l.setFont(Theme.SMALL);
			l.setForeground(Theme.MUTED);
			return l;
		}

		static JLabel fieldLabel(String text) {
			JLabel l = new JLabel(text);
			l.setFont(Theme.LABEL);
			l.setForeground(Theme.TEXT);
			return l;
		}

		static JLabel errorLabel() {
			JLabel l = new JLabel(" ");
			l.setFont(Theme.SMALL);
			l.setForeground(Theme.ERROR);
			l.setAlignmentX(Component.CENTER_ALIGNMENT);
			return l;
		}

		static JLink link(String text, Runnable action) {
			return new JLink(text, action);
		}

		static JButton button(String text, boolean primary) {
			JButton b = new JButton(text);
			b.setFont(Theme.BUTTON);
			b.setFocusPainted(false);
			b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			if (primary) {
				b.setBackground(Theme.ACCENT);
				b.setForeground(Theme.WHITE);
				b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
				b.addMouseListener(hover(Theme.ACCENT, Theme.ACCENT_DARK));
			} else {
				b.setBackground(Theme.WHITE);
				b.setForeground(Theme.TEXT);
				b.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Theme.LINE),
					BorderFactory.createEmptyBorder(9, 17, 9, 17)));
				b.addMouseListener(hover(Theme.WHITE, new Color(255, 248, 235)));
			}
			return b;
		}

		private static MouseAdapter hover(Color off, Color on) {
			return new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					((JButton) e.getSource()).setBackground(on);
				}

				public void mouseExited(MouseEvent e) {
					((JButton) e.getSource()).setBackground(off);
				}
			};
		}

		static JTextField input() {
			JTextField f = new JTextField(14);
			f.setFont(Theme.BODY);
			f.setForeground(Theme.TEXT);
			f.setBackground(Theme.WHITE);
			f.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.LINE),
				BorderFactory.createEmptyBorder(8, 10, 8, 10)));
			return f;
		}

		static HintField hintField(String hint) {
			return new HintField(hint);
		}

		static HintPassword hintPassword(String hint) {
			return new HintPassword(hint);
		}

		static void styleTable(JTable t) {
			t.setFont(Theme.BODY);
			t.setForeground(Theme.TEXT);
			t.setBackground(Theme.WHITE);
			t.setRowHeight(32);
			t.setShowGrid(false);
			t.setIntercellSpacing(new Dimension(0, 0));
			t.setFillsViewportHeight(true);
			t.setSelectionBackground(Theme.ACCENT);
			t.setSelectionForeground(Theme.WHITE);
			t.setGridColor(Theme.LINE);
			t.getTableHeader().setFont(Theme.LABEL);
			t.getTableHeader().setForeground(Theme.TEXT);
			t.getTableHeader().setBackground(Theme.CARD);
			t.getTableHeader().setReorderingAllowed(false);
			t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.ACCENT));
		}

		// Modal add/edit dialog; save(..) returns an error message or null on success.
		static void formDialog(Component owner, String title, String[] labels, String[] initial,
				Function<String[], String> save, Runnable onSaved) {
			Window window = SwingUtilities.getWindowAncestor(owner);
			JDialog dlg = new JDialog(window, title, Dialog.ModalityType.APPLICATION_MODAL);
			dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			JPanel root = new JPanel(new BorderLayout(0, 16));
			root.setBackground(Theme.BG);
			root.setBorder(BorderFactory.createEmptyBorder(22, 26, 18, 26));

			JPanel form = new JPanel(new GridBagLayout());
			form.setOpaque(false);
			GridBagConstraints g = new GridBagConstraints();
			g.insets = new Insets(5, 4, 5, 6);
			g.anchor = GridBagConstraints.WEST;
			List<JTextField> fields = new ArrayList<>();
			for (int i = 0; i < labels.length; i++) {
				g.gridx = 0;
				g.gridy = i;
				g.fill = GridBagConstraints.NONE;
				form.add(fieldLabel(labels[i]), g);

				g.gridx = 1;
				g.weightx = 1;
				g.fill = GridBagConstraints.HORIZONTAL;
				JTextField f = input();
				f.setText(initial[i] == null ? "" : initial[i]);
				fields.add(f);
				form.add(f, g);
			}
			g.weightx = 0;
			g.gridx = 0;
			g.gridy = labels.length;
			g.gridwidth = 2;
			g.anchor = GridBagConstraints.CENTER;
			form.add(errorLabel(), g);
			JLabel error = (JLabel) form.getComponent(form.getComponentCount() - 1);

			root.add(form, BorderLayout.CENTER);

			JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
			buttons.setOpaque(false);
			JButton cancel = button("Cancel", false);
			JButton ok = button("Save", true);
			cancel.addActionListener(e -> dlg.dispose());
			ok.addActionListener(e -> {
				String[] values = new String[fields.size()];
				for (int i = 0; i < fields.size(); i++) values[i] = fields.get(i).getText().trim();
				String problem = save.apply(values);
				if (problem == null) {
					dlg.dispose();
					onSaved.run();
				} else {
					error.setText(problem);
				}
			});
			buttons.add(cancel);
			buttons.add(ok);
			root.add(buttons, BorderLayout.SOUTH);

			dlg.setContentPane(root);
			dlg.pack();
			dlg.setResizable(false);
			dlg.setLocationRelativeTo(window);
			dlg.setVisible(true);
		}
	}

	// A clickable JLabel used for "Sign in / Create account" switching.
	static final class JLink extends JLabel {
		JLink(String text, Runnable action) {
			super(text);
			setFont(Theme.SMALL);
			setForeground(Theme.ACCENT_DARK);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			setAlignmentX(Component.CENTER_ALIGNMENT);
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					action.run();
				}

				public void mouseEntered(MouseEvent e) {
					setForeground(Theme.ACCENT);
				}

				public void mouseExited(MouseEvent e) {
					setForeground(Theme.ACCENT_DARK);
				}
			});
		}
	}

	// JTextField with a grey placeholder hint.
	static final class HintField extends JTextField {
		private final String hint;

		HintField(String hint) {
			super(14);
			this.hint = hint;
			setFont(Theme.BODY);
			setForeground(Theme.MUTED);
			setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.LINE),
				BorderFactory.createEmptyBorder(8, 10, 8, 10)));
			setText(hint);
			addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					if (isHint()) {
						setText("");
					}
					setForeground(Theme.TEXT);
				}

				public void focusLost(FocusEvent e) {
					if (getText().trim().isEmpty()) {
						setText(hint);
						setForeground(Theme.MUTED);
					}
				}
			});
		}

		private boolean isHint() {
			return getText().equals(hint);
		}

		String value() {
			return isHint() ? "" : getText().trim();
		}
	}

	// JPasswordField counterpart of HintField.
	static final class HintPassword extends JPasswordField {
		private final String hint;

		HintPassword(String hint) {
			super(14);
			this.hint = hint;
			setFont(Theme.BODY);
			setForeground(Theme.MUTED);
			setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.LINE),
				BorderFactory.createEmptyBorder(8, 10, 8, 10)));
			setText(hint);
			addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					if (isHint()) {
						setText("");
					}
					setForeground(Theme.TEXT);
				}

				public void focusLost(FocusEvent e) {
					if (empty()) {
						setText(hint);
						setForeground(Theme.MUTED);
					}
				}
			});
		}

		private boolean isHint() {
			return new String(getPassword()).equals(hint);
		}

		private boolean empty() {
			return new String(getPassword()).trim().isEmpty();
		}

		String value() {
			return isHint() ? "" : new String(getPassword()).trim();
		}
	}

	// -------- data layer ------------------------------------------------

	static final class Database {
		private static final String URL = "jdbc:sqlite:courier_management.db";
		private static Connection conn;

		private Database() {}

		static {
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("SQLite JDBC driver missing from classpath", e);
			}
		}

		static Connection get() {
			if (conn == null) {
				try {
					conn = DriverManager.getConnection(URL);
				} catch (SQLException e) {
					throw new RuntimeException("Could not open database: " + e.getMessage(), e);
				}
			}
			return conn;
		}

		static void init() {
			try (Statement st = get().createStatement()) {
				st.execute("CREATE TABLE IF NOT EXISTS item (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, weight REAL DEFAULT 0, status TEXT DEFAULT 'Pending')");
				st.execute("CREATE TABLE IF NOT EXISTS driver (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, phone TEXT, vehicle TEXT, license TEXT)");
				st.execute("CREATE TABLE IF NOT EXISTS customer (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, phone TEXT, email TEXT, address TEXT)");
				st.execute("CREATE TABLE IF NOT EXISTS admin (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL)");
				st.execute("CREATE TABLE IF NOT EXISTS delivery_session (id INTEGER PRIMARY KEY AUTOINCREMENT, driver_id INTEGER, item_id INTEGER, customer_id INTEGER, status TEXT DEFAULT 'Pending', date TEXT)");
				st.execute("CREATE TABLE IF NOT EXISTS storage_box (id INTEGER PRIMARY KEY AUTOINCREMENT, item_id INTEGER, label TEXT, location TEXT)");
			} catch (SQLException e) {
				throw new RuntimeException("Schema init failed: " + e.getMessage(), e);
			}
			if (count("admin") == 0) {
				insertAdmin("Administrator", "admin", "admin123");
			}
		}

		static int count(String table) {
			try (
				Statement st = get().createStatement();
				ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)
			) {
				return rs.next() ? rs.getInt(1) : 0;
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

		static boolean authenticate(String username, String password) {
			Admin a = findAdmin(username);
			return a != null && a.getPassword().equals(hash(password));
		}

		static boolean usernameTaken(String username) {
			return findAdmin(username) != null;
		}

		static void insertAdmin(String name, String username, String password) {
			try (PreparedStatement ps = get().prepareStatement(
					"INSERT INTO admin(name, username, password) VALUES(?,?,?)")) {
				ps.setString(1, name);
				ps.setString(2, username);
				ps.setString(3, hash(password));
				ps.executeUpdate();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

		static Admin findAdmin(String username) {
			try (PreparedStatement ps = get().prepareStatement(
					"SELECT id, name, username, password FROM admin WHERE username = ?")) {
				ps.setString(1, username);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return new Admin(rs.getInt("id"), rs.getString("name"),
							rs.getString("username"), rs.getString("password"));
					}
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return null;
		}

		static String hash(String value) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
				StringBuilder sb = new StringBuilder();
				for (byte b : digest) {
					sb.append(String.format("%02x", b));
				}
				return sb.toString();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
	}

	// ------- models -------------------------------------------------

	interface HasId {
		int getId();
	}

	static final class Item implements HasId {
		private final int id;
		private final String name;
		private final double weight;
		private final String status;

		Item(int id, String name, double weight, String status) {
			this.id = id;
			this.name = name;
			this.weight = weight;
			this.status = status;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public double getWeight() {
			return weight;
		}

		public String getStatus() {
			return status;
		}
	}

	static final class Driver implements HasId {
		private final int id;
		private final String name;
		private final String phone;
		private final String vehicle;
		private final String license;

		Driver(int id, String name, String phone, String vehicle, String license) {
			this.id = id;
			this.name = name;
			this.phone = phone;
			this.vehicle = vehicle;
			this.license = license;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getPhone() {
			return phone;
		}

		public String getVehicle() {
			return vehicle;
		}

		public String getLicense() {
			return license;
		}
	}

	static final class Customer implements HasId {
		private final int id;
		private final String name;
		private final String phone;
		private final String email;
		private final String address;

		Customer(int id, String name, String phone, String email, String address) {
			this.id = id;
			this.name = name;
			this.phone = phone;
			this.email = email;
			this.address = address;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getPhone() {
			return phone;
		}

		public String getEmail() {
			return email;
		}

		public String getAddress() {
			return address;
		}
	}

	static final class Admin implements HasId {
		private final int id;
		private final String name;
		private final String username;
		private final String password;

		Admin(int id, String name, String username, String password) {
			this.id = id;
			this.name = name;
			this.username = username;
			this.password = password;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}
	}

	static final class Session implements HasId {
		private final int id;
		private final int driverId;
		private final int itemId;
		private final int customerId;
		private final String status;
		private final String date;
		private String driverName;
		private String itemName;
		private String customerName;

		Session(int id, int driverId, int itemId, int customerId, String status, String date) {
			this.id = id;
			this.driverId = driverId;
			this.itemId = itemId;
			this.customerId = customerId;
			this.status = status;
			this.date = date;
		}

		public int getId() {
			return id;
		}

		public int getDriverId() {
			return driverId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getCustomerId() {
			return customerId;
		}

		public String getStatus() {
			return status;
		}

		public String getDate() {
			return date;
		}

		Session withNames(String driverName, String itemName, String customerName) {
			this.driverName = driverName;
			this.itemName = itemName;
			this.customerName = customerName;
			return this;
		}

		public String getDriverName() {
			return driverName;
		}

		public String getItemName() {
			return itemName;
		}

		public String getCustomerName() {
			return customerName;
		}
	}

	static final class StorageBox implements HasId {
		private final int id;
		private final int itemId;
		private final String label;
		private final String location;
		private String itemName;

		StorageBox(int id, int itemId, String label, String location) {
			this.id = id;
			this.itemId = itemId;
			this.label = label;
			this.location = location;
		}

		public int getId() {
			return id;
		}

		public int getItemId() {
			return itemId;
		}

		public String getLabel() {
			return label;
		}

		public String getLocation() {
			return location;
		}

		StorageBox withItemName(String itemName) {
			this.itemName = itemName;
			return this;
		}

		public String getItemName() {
			return itemName;
		}
	}

	// ------- generic CRUD contract + SQL helpers ---------------------

	abstract static class CrudDao<T extends HasId> {
		interface RowMapper<T> {
			T map(ResultSet rs) throws SQLException;
		}

		abstract String name();

		abstract String title();

		abstract Object[] headers();

		abstract List<T> findAll();

		abstract Object[] toRow(T entity);

		abstract String[] defaultForm();

		abstract String[] toForm(T entity);

		abstract String[] formLabels();

		abstract T fromForm(int id, String[] values);

		abstract void insert(T entity);

		abstract void update(T entity);

		abstract void delete(int id);

		protected int asInt(String s, String field) {
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(field + " must be a number.");
			}
		}

		protected double asDouble(String s, String field) {
			try {
				return Double.parseDouble(s);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(field + " must be a number.");
			}
		}

		protected int update(String sql, Object... args) {
			try (PreparedStatement ps = Database.get().prepareStatement(sql)) {
				bind(ps, args);
				return ps.executeUpdate();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

		protected <R> List<R> query(String sql, RowMapper<R> mapper, Object... args) {
			List<R> out = new ArrayList<>();
			try (PreparedStatement ps = Database.get().prepareStatement(sql)) {
				bind(ps, args);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						out.add(mapper.map(rs));
					}
				}
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return out;
		}

		private void bind(PreparedStatement ps, Object... args) throws SQLException {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof Integer n) {
					ps.setInt(i + 1, n);
				} else if (args[i] instanceof Double d) {
					ps.setDouble(i + 1, d);
				} else if (args[i] == null) {
					ps.setString(i + 1, "");
				} else {
					ps.setString(i + 1, String.valueOf(args[i]));
				}
			}
		}
	}

	// ------- concrete DAOs ---------------------------------------------

	static final class ItemDao extends CrudDao<Item> {
		String name() { return "Item"; }
		String title() { return "Manage Items"; }
		Object[] headers() { return new Object[]{"Name", "Weight (kg)", "Status"}; }
		String[] formLabels() { return new String[]{"Name", "Weight (kg)", "Status"}; }
		String[] defaultForm() { return new String[]{"", "", "Pending"}; }
		String[] toForm(Item e) { return new String[]{e.getName(), String.valueOf(e.getWeight()), e.getStatus()}; }

		Object[] toRow(Item e) {
			return new Object[]{e.getName(), String.valueOf(e.getWeight()), e.getStatus()};
		}

		List<Item> findAll() {
			return query("SELECT id, name, weight, status FROM item ORDER BY id",
				rs -> new Item(rs.getInt("id"), rs.getString("name"),
					rs.getDouble("weight"), rs.getString("status")));
		}

		Item fromForm(int id, String[] v) {
			return new Item(id, v[0], asDouble(v[1], "Weight"), v[2]);
		}

		void insert(Item e) {
			update("INSERT INTO item(name, weight, status) VALUES(?,?,?)", e.getName(), e.getWeight(), e.getStatus());
		}

		void update(Item e) {
			update("UPDATE item SET name=?, weight=?, status=? WHERE id=?",
				e.getName(), e.getWeight(), e.getStatus(), e.getId());
		}

		void delete(int id) {
			update("DELETE FROM item WHERE id=?", id);
		}
	}

	static final class DriverDao extends CrudDao<Driver> {
		String name() { return "Driver"; }
		String title() { return "Manage Drivers"; }
		Object[] headers() { return new Object[]{"Name", "Phone", "Vehicle", "License No."}; }
		String[] formLabels() { return new String[]{"Name", "Phone", "Vehicle", "License No."}; }
		String[] defaultForm() { return new String[]{"", "", "", ""}; }
		String[] toForm(Driver e) {
			return new String[]{e.getName(), e.getPhone(), e.getVehicle(), e.getLicense()};
		}

		Object[] toRow(Driver e) {
			return new Object[]{e.getName(), e.getPhone(), e.getVehicle(), e.getLicense()};
		}

		List<Driver> findAll() {
			return query("SELECT id, name, phone, vehicle, license FROM driver ORDER BY id",
				rs -> new Driver(rs.getInt("id"), rs.getString("name"), rs.getString("phone"),
					rs.getString("vehicle"), rs.getString("license")));
		}

		Driver fromForm(int id, String[] v) {
			return new Driver(id, v[0], v[1], v[2], v[3]);
		}

		void insert(Driver e) {
			update("INSERT INTO driver(name, phone, vehicle, license) VALUES(?,?,?,?)",
				e.getName(), e.getPhone(), e.getVehicle(), e.getLicense());
		}

		void update(Driver e) {
			update("UPDATE driver SET name=?, phone=?, vehicle=?, license=? WHERE id=?",
				e.getName(), e.getPhone(), e.getVehicle(), e.getLicense(), e.getId());
		}

		void delete(int id) {
			update("DELETE FROM driver WHERE id=?", id);
		}
	}

	static final class CustomerDao extends CrudDao<Customer> {
		String name() { return "Customer"; }
		String title() { return "Manage Customers"; }
		Object[] headers() { return new Object[]{"Name", "Phone", "Email", "Address"}; }
		String[] formLabels() { return new String[]{"Name", "Phone", "Email", "Address"}; }
		String[] defaultForm() { return new String[]{"", "", "", ""}; }
		String[] toForm(Customer e) {
			return new String[]{e.getName(), e.getPhone(), e.getEmail(), e.getAddress()};
		}

		Object[] toRow(Customer e) {
			return new Object[]{e.getName(), e.getPhone(), e.getEmail(), e.getAddress()};
		}

		List<Customer> findAll() {
			return query("SELECT id, name, phone, email, address FROM customer ORDER BY id",
				rs -> new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("phone"),
					rs.getString("email"), rs.getString("address")));
		}

		Customer fromForm(int id, String[] v) {
			return new Customer(id, v[0], v[1], v[2], v[3]);
		}

		void insert(Customer e) {
			update("INSERT INTO customer(name, phone, email, address) VALUES(?,?,?,?)",
				e.getName(), e.getPhone(), e.getEmail(), e.getAddress());
		}

		void update(Customer e) {
			update("UPDATE customer SET name=?, phone=?, email=?, address=? WHERE id=?",
				e.getName(), e.getPhone(), e.getEmail(), e.getAddress(), e.getId());
		}

		void delete(int id) {
			update("DELETE FROM customer WHERE id=?", id);
		}
	}

	static final class AdminDao extends CrudDao<Admin> {
		String name() { return "Admin"; }
		String title() { return "Manage Admins"; }
		Object[] headers() { return new Object[]{"Name", "Username"}; }
		String[] formLabels() { return new String[]{"Name", "Username", "Password"}; }
		String[] defaultForm() { return new String[]{"", "", ""}; }

		// Password is never shown; a blank password keeps the existing one on edit.
		String[] toForm(Admin e) {
			return new String[]{e.getName(), e.getUsername(), ""};
		}

		Object[] toRow(Admin e) {
			return new Object[]{e.getName(), e.getUsername()};
		}

		List<Admin> findAll() {
			return query("SELECT id, name, username, password FROM admin ORDER BY id",
				rs -> new Admin(rs.getInt("id"), rs.getString("name"),
					rs.getString("username"), rs.getString("password")));
		}

		Admin fromForm(int id, String[] v) {
			return new Admin(id, v[0], v[1], v[2]);
		}

		void insert(Admin e) {
			update("INSERT INTO admin(name, username, password) VALUES(?,?,?)",
				e.getName(), e.getUsername(), Database.hash(e.getPassword()));
		}

		void update(Admin e) {
			String password = e.getPassword();
			if (password == null || password.isEmpty()) {
				password = query("SELECT password FROM admin WHERE id=?",
					rs -> rs.getString("password"), e.getId()).stream().findFirst().orElse("");
			}
			update("UPDATE admin SET name=?, username=?, password=? WHERE id=?",
				e.getName(), e.getUsername(), Database.hash(password), e.getId());
		}

		void delete(int id) {
			update("DELETE FROM admin WHERE id=?", id);
		}
	}

	static final class SessionDao extends CrudDao<Session> {
		String name() { return "Delivery"; }
		String title() { return "Manage Deliveries"; }
		Object[] headers() { return new Object[]{"Driver", "Item", "Customer", "Status", "Date"}; }
		String[] formLabels() { return new String[]{"Driver ID", "Item ID", "Customer ID", "Status", "Date"}; }
		String[] defaultForm() {
			return new String[]{"", "", "", "Pending", LocalDate.now().toString()};
		}

		String[] toForm(Session e) {
			return new String[]{String.valueOf(e.getDriverId()), String.valueOf(e.getItemId()),
				String.valueOf(e.getCustomerId()), e.getStatus(), e.getDate()};
		}

		Object[] toRow(Session e) {
			return new Object[]{e.getDriverName(), e.getItemName(), e.getCustomerName(),
				e.getStatus(), e.getDate()};
		}

		List<Session> findAll() {
			String sql = "SELECT s.id, s.driver_id, s.item_id, s.customer_id, s.status, s.date, " +
				" COALESCE(d.name, 'Driver #' || s.driver_id), " +
				" COALESCE(i.name, 'Item #' || s.item_id), " +
				" COALESCE(c.name, 'Customer #' || s.customer_id) " +
				" FROM delivery_session s " +
				" LEFT JOIN driver d ON d.id = s.driver_id " +
				" LEFT JOIN item i ON i.id = s.item_id " +
				" LEFT JOIN customer c ON c.id = s.customer_id " +
				" ORDER BY s.id";
			return query(sql, rs -> new Session(rs.getInt("id"), rs.getInt("driver_id"),
				rs.getInt("item_id"), rs.getInt("customer_id"), rs.getString("status"), rs.getString("date"))
				.withNames(rs.getString(7), rs.getString(8), rs.getString(9)));
		}

		Session fromForm(int id, String[] v) {
			return new Session(id, asInt(v[0], "Driver ID"), asInt(v[1], "Item ID"),
				asInt(v[2], "Customer ID"), v[3], v[4]);
		}

		void insert(Session e) {
			update("INSERT INTO delivery_session(driver_id, item_id, customer_id, status, date) VALUES(?,?,?,?,?)",
				e.getDriverId(), e.getItemId(), e.getCustomerId(), e.getStatus(), e.getDate());
		}

		void update(Session e) {
			update("UPDATE delivery_session SET driver_id=?, item_id=?, customer_id=?, status=?, date=? WHERE id=?",
				e.getDriverId(), e.getItemId(), e.getCustomerId(), e.getStatus(), e.getDate(), e.getId());
		}

		void delete(int id) {
			update("DELETE FROM delivery_session WHERE id=?", id);
		}
	}

	static final class StorageBoxDao extends CrudDao<StorageBox> {
		String name() { return "Storage StorageBox"; }
		String title() { return "Manage Storage Boxes"; }
		Object[] headers() { return new Object[]{"Item", "Label", "Location"}; }
		String[] formLabels() { return new String[]{"Item ID", "Label", "Location"}; }
		String[] defaultForm() { return new String[]{"", "", ""}; }

		String[] toForm(StorageBox e) {
			return new String[]{String.valueOf(e.getItemId()), e.getLabel(), e.getLocation()};
		}

		Object[] toRow(StorageBox e) {
			String item = e.getItemName() != null
				? e.getItemName() + " (ID " + e.getItemId() + ")"
				: "Item #" + e.getItemId();
			return new Object[]{item, e.getLabel(), e.getLocation()};
		}

		List<StorageBox> findAll() {
			String sql = "SELECT b.id, b.item_id, b.label, b.location, i.name " +
				" FROM storage_box b LEFT JOIN item i ON i.id = b.item_id ORDER BY b.id";
			return query(sql, rs -> new StorageBox(rs.getInt("id"), rs.getInt("item_id"),
				rs.getString("label"), rs.getString("location"))
				.withItemName(rs.getString("name")));
		}

		StorageBox fromForm(int id, String[] v) {
			return new StorageBox(id, asInt(v[0], "Item ID"), v[1], v[2]);
		}

		void insert(StorageBox e) {
			update("INSERT INTO storage_box(item_id, label, location) VALUES(?,?,?)",
				e.getItemId(), e.getLabel(), e.getLocation());
		}

		void update(StorageBox e) {
			update("UPDATE storage_box SET item_id=?, label=?, location=? WHERE id=?",
				e.getItemId(), e.getLabel(), e.getLocation(), e.getId());
		}

		void delete(int id) {
			update("DELETE FROM storage_box WHERE id=?", id);
		}
	}

	// ------- reusable CRUD screen ---------------------------------------

	static final class CrudPanel<T extends HasId> extends JPanel {
		private final CrudDao<T> dao;
		private final List<T> rows = new ArrayList<>();
		private final DefaultTableModel model;
		private final JTable table;
		private final JLabel status = new JLabel(" ");
		private final JLabel recordCount = UI.subtle("");

		CrudPanel(CrudDao<T> dao) {
			this.dao = dao;
			setLayout(new BorderLayout(0, 14));
			setBorder(BorderFactory.createEmptyBorder(24, 34, 24, 34));
			setBackground(Theme.BG);
			add(buildTop(), BorderLayout.NORTH);

			model = new DefaultTableModel(dao.headers(), 0) {
				public boolean isCellEditable(int row, int col) {
					return false;
				}
			};
			table = new JTable(model);
			UI.styleTable(table);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
						edit();
					}
				}
			});
			JScrollPane scroller = new JScrollPane(table);
			scroller.setBorder(null);
			scroller.getViewport().setBackground(Theme.WHITE);
			add(scroller, BorderLayout.CENTER);

			JPanel south = new JPanel(new BorderLayout());
			south.setOpaque(false);
			status.setFont(Theme.SMALL);
			status.setForeground(Theme.MUTED);
			south.add(status, BorderLayout.WEST);
			south.add(recordCount, BorderLayout.EAST);
			add(south, BorderLayout.SOUTH);

			refresh();
		}

		private JPanel buildTop() {
			JPanel top = new JPanel(new BorderLayout());
			top.setOpaque(false);

			JPanel left = new JPanel();
			left.setOpaque(false);
			left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
			left.add(UI.h1(dao.title()));
			left.add(Box.createRigidArea(new Dimension(0, 4)));
			left.add(UI.subtle("Add, edit or remove records from the database."));
			top.add(left, BorderLayout.WEST);

			JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
			buttons.setOpaque(false);
			JButton add = UI.button("Add", true);
			JButton edit = UI.button("Edit", true);
			JButton delete = UI.button("Delete", true);
			JButton refresh = UI.button("Refresh", false);
			add.addActionListener(e -> add());
			edit.addActionListener(e -> edit());
			delete.addActionListener(e -> delete());
			refresh.addActionListener(e -> {
				refresh();
				setStatus("Refreshed.", Theme.MUTED);
			});
			buttons.add(add);
			buttons.add(edit);
			buttons.add(delete);
			buttons.add(refresh);
			top.add(buttons, BorderLayout.EAST);
			return top;
		}

		private void add() {
			UI.formDialog(this, "Add " + dao.name(), dao.formLabels(), dao.defaultForm(),
				values -> {
					try {
						dao.insert(dao.fromForm(-1, values));
						return null;
					} catch (Exception ex) {
						return ex.getMessage();
					}
				},
				() -> {
					refresh();
					setStatus("Added " + dao.name() + ".", Theme.GOOD);
				});
		}

		private void edit() {
			int idx = table.getSelectedRow();
			if (idx < 0) {
				setStatus("Select a row to edit.", Theme.ERROR);
				return;
			}
			T entity = rows.get(idx);
			UI.formDialog(this, "Edit " + dao.name(), dao.formLabels(), dao.toForm(entity),
				values -> {
					try {
						dao.update(dao.fromForm(entity.getId(), values));
						return null;
					} catch (Exception ex) {
						return ex.getMessage();
					}
				},
				() -> {
					refresh();
					setStatus("Updated " + dao.name() + ".", Theme.GOOD);
				});
		}

		private void delete() {
			int idx = table.getSelectedRow();
			if (idx < 0) {
				setStatus("Select a row to delete.", Theme.ERROR);
				return;
			}
			int choice = JOptionPane.showConfirmDialog(this,
				"Delete this " + dao.name().toLowerCase() + " permanently?",
				"Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (choice != JOptionPane.YES_OPTION) {
				return;
			}
			try {
				dao.delete(rows.get(idx).getId());
				refresh();
				setStatus("Deleted " + dao.name() + ".", Theme.GOOD);
			} catch (Exception ex) {
				setStatus(ex.getMessage(), Theme.ERROR);
			}
		}

		private void refresh() {
			rows.clear();
			rows.addAll(dao.findAll());
			model.setRowCount(0);
			for (T r : rows) {
				model.addRow(dao.toRow(r));
			}
			recordCount.setText(rows.size() + " record" + (rows.size() == 1 ? "" : "s"));
		}

		private void setStatus(String text, Color color) {
			status.setText(text);
			status.setForeground(color);
		}
	}

	// ------- navigation + screens -----------------------------------------

	static final class SideButton extends JButton {
		private boolean active;

		SideButton(String text, Runnable onClick) {
			super(text);
			setFont(Theme.BUTTON);
			setForeground(Theme.TEXT);
			setFocusPainted(false);
			setBorderPainted(false);
			setContentAreaFilled(false);
			setOpaque(false);
			setHorizontalAlignment(SwingConstants.LEFT);
			setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 12));
			setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
			setRolloverEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			addActionListener(e -> onClick.run());
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					if (!active) {
						setOpaque(true);
						setBackground(new Color(255, 255, 255, 60));
						repaint();
					}
				}

				public void mouseExited(MouseEvent e) {
					setActive(active);
				}
			});
		}

		void setActive(boolean active) {
			this.active = active;
			setOpaque(active);
			setContentAreaFilled(active);
			setBackground(active ? Theme.ACCENT : null);
			setForeground(active ? Theme.WHITE : Theme.TEXT);
			repaint();
		}
	}

	static final class DashboardPanel extends JPanel {
		DashboardPanel(String adminName) {
			setBackground(Theme.BG);
			setBorder(BorderFactory.createEmptyBorder(28, 34, 28, 34));
			setLayout(new BorderLayout(0, 24));

			JPanel head = new JPanel();
			head.setOpaque(false);
			head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
			head.add(UI.h1("Welcome back, " + adminName + "!"));
			head.add(Box.createRigidArea(new Dimension(0, 6)));
			head.add(UI.subtle("A quick overview of your courier management system."));
			add(head, BorderLayout.NORTH);

			JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
			grid.setOpaque(false);
			grid.add(card("Items", Database.count("item")));
			grid.add(card("Drivers", Database.count("driver")));
			grid.add(card("Customers", Database.count("customer")));
			grid.add(card("Deliveries", Database.count("delivery_session")));
			grid.add(card("Storage boxes", Database.count("storage_box")));
			grid.add(card("Admins", Database.count("admin")));
			add(grid, BorderLayout.CENTER);
		}

		private JPanel card(String caption, int value) {
			JPanel c = new JPanel();
			c.setBackground(Theme.CARD);
			c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
			c.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

			JLabel v = new JLabel(String.valueOf(value));
			v.setFont(new Font("Segoe UI", Font.BOLD, 34));
			v.setForeground(Theme.ACCENT_DARK);
			v.setAlignmentX(Component.CENTER_ALIGNMENT);

			JLabel cap = UI.subtle(caption);
			cap.setAlignmentX(Component.CENTER_ALIGNMENT);

			c.add(Box.createVerticalGlue());
			c.add(v);
			c.add(Box.createRigidArea(new Dimension(0, 4)));
			c.add(cap);
			c.add(Box.createVerticalGlue());
			return c;
		}
	}

	// ------- authentication screen -----------------------------------------

	static final class LoginFrame extends JFrame {
		private final CardLayout cards = new CardLayout();
		private final JPanel root = new JPanel(cards);

		LoginFrame() {
			super("Courier Management System · Sign in");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			root.setBackground(Theme.BG);
			root.add(loginPanel(), "login");
			root.add(signupPanel(), "signup");
			setContentPane(root);
			pack();
			setLocationRelativeTo(null);
		}

		private JPanel loginPanel() {
			JPanel pane = new JPanel(new GridBagLayout());
			pane.setBackground(Theme.BG);

			JPanel card = authCard();
			HintField username = UI.hintField("Username");
			HintPassword password = UI.hintPassword("Password");
			JLabel error = UI.errorLabel();

			addFormField(card, "Username", username);
			addFormField(card, "Password", password);
			card.add(Box.createRigidArea(new Dimension(0, 14)));

			JButton signIn = UI.button("Sign in", true);
			signIn.setAlignmentX(Component.CENTER_ALIGNMENT);
			signIn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
			Runnable doLogin = () -> {
				String u = username.value();
				String p = password.value();
				if (u.isEmpty() || p.isEmpty()) {
					error.setText("Please enter your username and password.");
					return;
				}
				Admin admin = Database.findAdmin(u);
				if (admin == null || !admin.getPassword().equals(Database.hash(p))) {
					error.setText("Invalid username or password.");
					return;
				}
				openMain(admin.getName());
			};
			signIn.addActionListener(e -> doLogin.run());
			password.addActionListener(e -> doLogin.run());

			card.add(signIn);
			card.add(Box.createRigidArea(new Dimension(0, 10)));
			card.add(error);
			card.add(Box.createRigidArea(new Dimension(0, 6)));
			card.add(UI.link("New here? Create an account", () -> {
				error.setText(" ");
				cards.show(root, "signup");
			}));

			pane.add(card, new GridBagConstraints());
			return pane;
		}

		private JPanel signupPanel() {
			JPanel pane = new JPanel(new GridBagLayout());
			pane.setBackground(Theme.BG);

			JPanel card = authCard();
			HintField name = UI.hintField("Full name");
			HintField username = UI.hintField("Username");
			HintPassword password = UI.hintPassword("Password");
			HintPassword confirm = UI.hintPassword("Confirm password");
			JLabel error = UI.errorLabel();

			addFormField(card, "Name", name);
			addFormField(card, "Username", username);
			addFormField(card, "Password", password);
			addFormField(card, "Confirm password", confirm);
			card.add(Box.createRigidArea(new Dimension(0, 14)));

			JButton create = UI.button("Create account", true);
			create.setAlignmentX(Component.CENTER_ALIGNMENT);
			create.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
			create.addActionListener(e -> {
				if (name.value().isEmpty() || username.value().isEmpty() || password.value().isEmpty()) {
					error.setText("All fields are required.");
					return;
				}
				if (!password.value().equals(confirm.value())) {
					error.setText("Passwords do not match.");
					return;
				}
				if (Database.usernameTaken(username.value())) {
					error.setText("That username is already taken.");
					return;
				}
				Database.insertAdmin(name.value(), username.value(), password.value());
				openMain(name.value());
			});

			card.add(create);
			card.add(Box.createRigidArea(new Dimension(0, 10)));
			card.add(error);
			card.add(Box.createRigidArea(new Dimension(0, 6)));
			card.add(UI.link("Already have an account? Sign in", () -> {
				error.setText(" ");
				cards.show(root, "login");
			}));

			pane.add(card, new GridBagConstraints());
			return pane;
		}

		private JPanel authCard() {
			JPanel card = new JPanel();
			card.setPreferredSize(new Dimension(400, 640));
			card.setBackground(Theme.BG);
			card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
			card.setBorder(BorderFactory.createEmptyBorder(34, 20, 24, 20));

			JLabel title = UI.h1("Courier Management");
			title.setFont(Theme.TITLE);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			JLabel sub = UI.subtle("Please sign in to continue");
			sub.setAlignmentX(Component.CENTER_ALIGNMENT);

			card.add(title);
			card.add(Box.createRigidArea(new Dimension(0, 6)));
			card.add(sub);
			card.add(Box.createRigidArea(new Dimension(0, 26)));
			return card;
		}

		private void addFormField(JPanel card, String labelText, JComponent input) {
			JLabel l = UI.fieldLabel(labelText);
			l.setAlignmentX(Component.LEFT_ALIGNMENT);
			input.setAlignmentX(Component.LEFT_ALIGNMENT);
			input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
			card.add(l);
			card.add(Box.createRigidArea(new Dimension(0, 6)));
			card.add(input);
			card.add(Box.createRigidArea(new Dimension(0, 14)));
		}

		private void openMain(String adminName) {
			new MainFrame(adminName).setVisible(true);
			dispose();
		}
	}

	// ------- main workspace screen ------------------------------------------

	static final class MainFrame extends JFrame {
		private final CardLayout contentLayout = new CardLayout();
		private final JPanel content = new JPanel(contentLayout);
		private final List<SideButton> nav = new ArrayList<>();

		MainFrame(String adminName) {
			super("Courier Management System");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setMinimumSize(new Dimension(980, 620));
			setSize(1150, 700);
			setLocationRelativeTo(null);
			setLayout(new BorderLayout());
			add(buildSide(adminName), BorderLayout.WEST);
			content.setBackground(Theme.BG);
			content.add(new DashboardPanel(adminName), "dashboard");
			content.add(new CrudPanel<>(new ItemDao()), "items");
			content.add(new CrudPanel<>(new DriverDao()), "drivers");
			content.add(new CrudPanel<>(new CustomerDao()), "customers");
			content.add(new CrudPanel<>(new SessionDao()), "deliveries");
			content.add(new CrudPanel<>(new StorageBoxDao()), "boxes");
			content.add(new CrudPanel<>(new AdminDao()), "admins");
			add(content, BorderLayout.CENTER);
		}

		private JPanel buildSide(String adminName) {
			JPanel side = new JPanel();
			side.setBackground(Theme.SIDE);
			side.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.ACCENT),
				BorderFactory.createEmptyBorder(26, 20, 22, 20)));
			side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
			side.setPreferredSize(new Dimension(230, 700));

			JLabel brand = new JLabel("Courier");
			brand.setFont(Theme.TITLE);
			brand.setForeground(Theme.TEXT);
			brand.setAlignmentX(Component.LEFT_ALIGNMENT);
			side.add(brand);

			JLabel brandSub = new JLabel("Management System");
			brandSub.setFont(Theme.SMALL);
			brandSub.setForeground(Theme.MUTED);
			brandSub.setAlignmentX(Component.LEFT_ALIGNMENT);
			side.add(brandSub);
			side.add(Box.createRigidArea(new Dimension(0, 26)));

			String[][] sections = {
				{"dashboard", "Dashboard"},
				{"items", "Items"},
				{"drivers", "Drivers"},
				{"customers", "Customers"},
				{"deliveries", "Deliveries"},
				{"boxes", "Storage Boxes"},
				{"admins", "Admins"},
			};
			for (String[] s : sections) {
				SideButton[] holder = new SideButton[1];
				SideButton b = new SideButton(s[1], () -> {
					contentLayout.show(content, s[0]);
					for (SideButton x : nav) {
						x.setActive(x == holder[0]);
					}
				});
				holder[0] = b;
				b.setAlignmentX(Component.LEFT_ALIGNMENT);
				side.add(b);
				side.add(Box.createRigidArea(new Dimension(0, 4)));
				nav.add(b);
			}

			side.add(Box.createVerticalGlue());

			JLabel who = UI.subtle("Signed in · " + adminName);
			who.setAlignmentX(Component.LEFT_ALIGNMENT);
			side.add(who);
			side.add(Box.createRigidArea(new Dimension(0, 12)));

			JButton logout = UI.button("Sign out", true);
			logout.setAlignmentX(Component.LEFT_ALIGNMENT);
			logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
			logout.addActionListener(e -> {
				dispose();
				new LoginFrame().setVisible(true);
			});
			side.add(logout);

			if (!nav.isEmpty()) {
				nav.get(0).setActive(true);
			}
			return side;
		}
	}

	// ------- data-layer smoke test ------------------------------------------

	static void selfTest() {
		try {
			Database.init();

			if (!Database.authenticate("admin", "admin123")) {
				throw new RuntimeException("seed admin login failed");
			}

			ItemDao itemDao = new ItemDao();
			itemDao.insert(new Item(-1, "Test Item", 3.5, "Pending"));
			Item first = itemDao.findAll().get(0);
			itemDao.update(new Item(first.getId(), first.getName(), first.getWeight(), "Transit"));
			first = itemDao.findAll().get(0);
			if (!"Transit".equals(first.getStatus())) {
				throw new RuntimeException("item update failed");
			}
			itemDao.delete(first.getId());

			new DriverDao().insert(new Driver(-1, "Test Driver", "000", "Van", "DL-1"));
			new CustomerDao().insert(new Customer(-1, "Test Customer", "000", "t@t.com", "Nowhere"));
			new StorageBoxDao().insert(new StorageBox(-1, 1, "A-1", "Shelf 1"));
			new SessionDao().insert(new Session(-1, 1, 1, 1, "Pending", LocalDate.now().toString()));

			AdminDao adminDao = new AdminDao();
			String testerUser = "tester" + System.currentTimeMillis();
			adminDao.insert(new Admin(-1, "Test Admin", testerUser, "pw"));
			if (!Database.authenticate(testerUser, "pw")) {
				throw new RuntimeException("inserted admin auth failed");
			}

			System.out.println("selftest: OK - database, CRUD and auth all working");
		} catch (Exception e) {
			System.out.println("selftest: FAIL - " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
}