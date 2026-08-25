public class Shipment {
    private final String id;
    private final String trackingCode;
    private final String customerGovID;
    private String sellerGovID;
    private String driverGovID;
    private String status;
    private String description;

    public Shipment(String id, String trackingCode, String customerGovID, String sellerGovID, String description) {
        this.id = id;
        this.trackingCode = trackingCode;
        this.customerGovID = customerGovID;
        this.sellerGovID = sellerGovID;
        this.description = description;
        this.status = "Created";
    }

    public String getId() { return id; }
    public String getTrackingCode() { return trackingCode; }
    public String getCustomerGovID() { return customerGovID; }
    public String getSellerGovID() { return sellerGovID; }
    public void setSellerGovID(String s) { this.sellerGovID = s; }
    public String getDriverGovID() { return driverGovID; }
    public void setDriverGovID(String d) { this.driverGovID = d; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }

    @Override
    public String toString() {
        return trackingCode + " — " + description + " (" + status + ")";
    }
}
