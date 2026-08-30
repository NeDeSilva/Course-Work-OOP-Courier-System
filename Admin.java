/**
 * Admin - an Employer with administrative rights. Can view all active parcels.
 */
public class Admin extends Employer {

	public Admin() {
		super();
	}

	public Admin(String username, String password, String name, String email,
			String address, String phone, int age, double salary, String joiningDate) {
		super(username, password, name, email, address, phone, age, salary, joiningDate);
	}
}
