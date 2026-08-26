package BusinessLogic;

public class Driver extends Person {
    private String vehicleType;
    private String licenseNumber;

    public Driver(String id, String name, String vehicleType, String licenseNumber) {
        super(id, name);
        this.vehicleType = vehicleType;
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}