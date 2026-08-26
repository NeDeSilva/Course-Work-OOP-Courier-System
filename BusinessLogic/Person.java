class Person {

	String govID;
	String name;
	int age;
	String address;
	String phoneNumber;
	String emailAddress;
	String userName;
	String password;

	Person(String govID, String name, int age, String address, String phoneNumber, String emailAddress, String userName, String password) {
		this.govID = govID;
		this.name = name;
		this.age = age;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.userName = userName;
		this.password = password;
	}

	public String getName() {
    return name;
	}

	public String getAddress() {
    return address;
	}

	public int getAge() {
    return age;
	}

	public String getGovID() {
    return govID;
	}

	public String getEmailAddress() {
    return emailAddress;
	}

	public String getPhoneNumber() {
    return phoneNumber;
	}

	public String getUserName() {
    return userName;
	}

	public String getPassword() {
    return password;
	}
}