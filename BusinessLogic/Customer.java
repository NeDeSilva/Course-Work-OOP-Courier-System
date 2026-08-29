package BusinessLogic;

public class Customer extends Person {
    private String customerId;
    private String deliveryAddress;

  
    public Customer(String name, String email, String phoneNumber, String customerId, String deliveryAddress) {
        super(name, email, phoneNumber);
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
    }

    
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

   
    public void placeOrder(String parcelDetails) {
        System.out.println("Order placed for parcel: " + parcelDetails);
    }
}
