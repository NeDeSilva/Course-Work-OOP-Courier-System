import java.util.List;

/**
 * Test - a small command-line test suite that verifies the core logic of the
 * Courier Management System against the SQLite database. It does not open any
 * UI windows.
 */
public class Test {

	private static int passed = 0;
	private static int failed = 0;

	public static void main(String[] args) {
		try {
			DBmanage.init();
			System.out.println("== Courier Management System - Tests ==");

			testAdminLogin();
			testDriverLogin();
			testSenderLogin();
			testReserverLogin();
			testTrackExistingParcel();
			testTrackUnknownParcel();
			testSampleData();
			testSenderCreatesParcel();
			testStatusUpdate();
			testActiveList();

			System.out.println("\nPassed: " + passed + ", Failed: " + failed);
			if (failed > 0) {
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			DBmanage.close();
		}
	}

	private static void testAdminLogin() throws Exception {
		boolean ok = new AdminDAO().isValid("admin", "admin123");
		check("Admin login with valid credentials", ok);
		check("Admin login with wrong password", !new AdminDAO().isValid("admin", "nope"));
	}

	private static void testDriverLogin() throws Exception {
		check("Driver login with valid credentials", new DriverDAO().isValid("driver", "driver123"));
	}

	private static void testSenderLogin() throws Exception {
		check("Sender login with valid credentials", new SenderDAO().isValid("sender", "sender123"));
	}

	private static void testReserverLogin() throws Exception {
		check("Reserver login with valid credentials", new ReserverDAO().isValid("reserver", "reserver123"));
	}

	private static void testTrackExistingParcel() throws Exception {
		Parcel p = new ParcelDAO().findByTracking("CMS000001");
		check("Existing parcel can be tracked", p != null && p.getName() != null);
		if (p != null) {
			Session s = new SessionDAO().findByTracking(p.getTrackingNumber());
			check("Existing parcel has a session", s != null);
		}
	}

	private static void testTrackUnknownParcel() throws Exception {
		check("Unknown tracking id returns null", new ParcelDAO().findByTracking("CMS999999") == null);
	}

	private static void testSampleData() throws Exception {
		String[] samples = { "CMS000001", "CMS000002", "CMS000003", "CMS000004", "CMS000005" };
		boolean allPresent = true;
		for (String t : samples) {
			if (new ParcelDAO().findByTracking(t) == null) {
				allPresent = false;
			}
		}
		check("All sample parcels are present and traceable", allPresent);

		int total = new ParcelDAO().countAll();
		check("Parcel count is at least the number of samples (>= " + samples.length + ")", total >= samples.length);

		// statuses in sessions are drawn from the lifecycle
		Session s = new SessionDAO().findByTracking("CMS000005");
		check("Delivered sample has a 'Delivered' status",
				s != null && "Delivered".equals(s.getStatus()));
	}

	private static void testSenderCreatesParcel() throws Exception {
		Sender s = new SenderDAO().findByCredentials("sender", "sender123");
		Parcel p = new Parcel();
		p.setName("Test Book");
		p.setWeight(0.8);
		p.setSize(30);
		p.setSenderAddress("7 Market St, Galle");
		p.setReceiverAddress("9 Ocean Rd, Hikkaduwa");
		p.setDescription("A test parcel");
		p.setSenderId(s.getId());
		p.setReserverId(-1);
		String tracking = new ParcelDAO().add(p, "Registered");
		check("Sender can create parcel and get tracking id", tracking != null && tracking.startsWith("CMS"));
		check("Newly created parcel is traceable", new ParcelDAO().findByTracking(tracking) != null);
	}

	private static void testStatusUpdate() throws Exception {
		List<Parcel> all = new ParcelDAO().listAllActive();
		if (!all.isEmpty()) {
			String tracking = all.get(0).getTrackingNumber();
			new SessionDAO().updateStatus(tracking, "Out for Delivery");
			Session s = new SessionDAO().findByTracking(tracking);
			check("Driver can update parcel status", s != null && "Out for Delivery".equals(s.getStatus()));
		} else {
			check("At least one active parcel exists for status update", false);
		}
	}

	private static void testActiveList() throws Exception {
		List<Parcel> active = new ParcelDAO().listAllActive();
		check("Active parcels list is not empty", active != null && !active.isEmpty());
	}

	private static void check(String name, boolean ok) {
		if (ok) {
			passed++;
			System.out.println("[PASS] " + name);
		} else {
			failed++;
			System.out.println("[FAIL] " + name);
		}
	}
}
