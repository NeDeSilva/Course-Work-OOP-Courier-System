package BusinessLogic;

public class Customer extends Person {
    private String address;
    private String contactNumber;

    public Customer(String id, String name, String address, String contactNumber) {
        super(id, name);
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
