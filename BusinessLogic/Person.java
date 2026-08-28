package BusinessLogic;
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

	public void setGovID(String govID) {
    this.govID = govID;
	}

	public void setName(String name) {
    this.name = name;
	}

	public void setAge(int age) {
    this.age = age;
	}

	public void setAddress(String address) {
    this.address = address;
	}

	public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
	}

	public void setPassword(String password) {
    this.password = password;
	}

	public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
	}

	public void setUserName(String userName) {
    this.userName = userName;
	}
}