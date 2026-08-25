package pawan;

import java.util.ArrayList;
import java.util.List;

import Person;
/**
 * A Seller is a merchant that hands over parcels to the courier company.
 */
public class Seller extends Person {
    private String shopName;
    private String businessType;
    private String pickupArea;
    private double walletBalance;
    private boolean verified;
    private final List<String> parcelIds = new ArrayList<>();
    public Seller() {
        super();
    }
    public Seller(String id, String name, String email, String phone, String address,
                  String shopName, String businessType, String pickupArea) {
        super(id, name, email, phone, address);
        this.shopName = shopName;
        this.businessType = businessType;
        this.pickupArea = pickupArea;
        this.walletBalance = 0.0;
        this.verified = false;
    }
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    public String getPickupArea() {
        return pickupArea;
    }
    public void setPickupArea(String pickupArea) {
        this.pickupArea = pickupArea;
    }
    public double getWalletBalance() {
        return walletBalance;
    }
    public boolean isVerified() {
        return verified;
    }
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    public List<String> getParcelIds() {
        return parcelIds;
    }
    @Override
    public String getRole() {
        return "SELLER";
    }
    @Override
    public boolean isValid() {
        return super.isValid() && shopName != null && !shopName.isBlank();
    }
    /** Registers a new parcel handed to the courier company. */
    public void addParcel(String parcelId) {
        if (parcelId != null && !parcelId.isBlank() && !parcelIds.contains(parcelId)) {
            parcelIds.add(parcelId);
        }
    }
    public boolean removeParcel(String parcelId) {
        return parcelIds.remove(parcelId);
    }
    /** COD money collected by the courier is credited to the seller wallet. */
    public void creditCod(double amount) {
        if (amount > 0) {
            walletBalance += amount;
        }
    }
    /** Seller withdraws the collected money. */
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > walletBalance) {
            return false;
        }
        walletBalance -= amount;
        return true;
    }
    public String summary() {
        return String.format("%s | shop=%s | area=%s | parcels=%d | wallet=%.2f | %s",
                getName(), shopName, pickupArea, parcelIds.size(), walletBalance,
                verified ? "VERIFIED" : "PENDING");
    }
    @Override
    public String toString() {
        return super.toString() + " | " + summary();
    }
}