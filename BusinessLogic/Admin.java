package BusinessLogic;

class Admin extends Person{

	private String employerID;

	Admin(String govID, String name, int age, String address, String phoneNumber, String emailAddress, String userName, String password, String employerID) {
		super(govID, name, age, address, phoneNumber, emailAddress, userName, password);
		this.employerID = employerID;

	}

	public String getEmployerID() {
		return employerID;
	}

	public void setEmployerID(String employerID) {
		this.employerID = employerID;
	}
}