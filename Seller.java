public class Seller extends Person {
    private String shopName;

    public Seller(String govID, String name, int age, String address, String phoneNumber, String emailAddress, String userName, String password, String shopName) {
        super(govID, name, age, address, phoneNumber, emailAddress, userName, password);
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
