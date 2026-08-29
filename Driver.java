public class Driver extends Person {
    private String licenseNumber;
    private String vehicleNumber;

    public Driver(String govID, String name, int age, String address, String phoneNumber, String emailAddress, String userName, String password, String licenseNumber, String vehicleNumber) {
        super(govID, name, age, address, phoneNumber, emailAddress, userName, password);
        this.licenseNumber = licenseNumber;
        this.vehicleNumber = vehicleNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
