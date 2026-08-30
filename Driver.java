/**
 * Driver - an Employer who delivers parcels and updates their status.
 */
public class Driver extends Employer {
	private String licenseNo;

	public Driver() {
		super();
	}

	public Driver(String username, String password, String name, String email,
			String address, String phone, int age, double salary, String joiningDate,
			String licenseNo) {
		super(username, password, name, email, address, phone, age, salary, joiningDate);
		this.licenseNo = licenseNo;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
}
