package BusinessLogic;

/**
 * Driver
 */
public class Driver extends Person
{
	public String driverID;
	public Driver(String govID, String name, int age, String address, String phoneNumber, String emailAddress, String userName, String password, String driverID) {
		super(govID, name, age, address, phoneNumber, emailAddress, userName, password);
		this.driverID = driverID;
	}

	public String getDriverID() {
		return driverID;
	}

	public void setDriverID(String driverID) {
		this.driverID = driverID;
	}
	
}