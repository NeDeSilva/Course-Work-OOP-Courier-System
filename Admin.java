class Admin extends Person{

	private String employerID;
	private boolean activeAdmin;

	Admin(String govID, String name, int age, String address, String phoneNumber, String emailAddress, String userName, String password, String employerID, boolean activeEmployer) {
		super(govID, name, age, address, phoneNumber, emailAddress, userName, password);
		this.employerID = employerID;
		this.activeAdmin = activeEmployer;
	}

	public String getEmployerID() {
		return employerID;
	}

	public boolean isActiveAdmin() {
		return activeAdmin;
	}
}