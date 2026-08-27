package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Merchant who hands parcels to the courier company.
 * Wallet is credited only via COD collections; withdrawals are explicit.
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
        this.shopName = trimToNull(shopName);
        this.businessType = trimToNull(businessType);
        this.pickupArea = trimToNull(pickupArea);
        this.walletBalance = 0.0;
        this.verified = false;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = trimToNull(shopName);
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = trimToNull(businessType);
    }

    public String getPickupArea() {
        return pickupArea;
    }

    public void setPickupArea(String pickupArea) {
        this.pickupArea = trimToNull(pickupArea);
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

    /** Defensive copy — callers cannot mutate the internal list. */
    public List<String> getParcelIds() {
        return Collections.unmodifiableList(parcelIds);
    }

    public int getParcelCount() {
        return parcelIds.size();
    }

    @Override
    public String getRole() {
        return "SELLER";
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && isPresent(shopName)
                && isPresent(pickupArea);
    }

    /** Registers a parcel handed to the courier. Ignores null, blank, and duplicates. */
    public boolean addParcel(String parcelId) {
        String id = trimToNull(parcelId);
        if (id == null || parcelIds.contains(id)) {
            return false;
        }
        parcelIds.add(id);
        return true;
    }

    public boolean removeParcel(String parcelId) {
        return parcelIds.remove(parcelId);
    }

    public boolean hasParcel(String parcelId) {
        return parcelIds.contains(parcelId);
    }

    /** COD collected by the courier is credited to the seller wallet. */
    public boolean creditCod(double amount) {
        if (amount <= 0) {
            return false;
        }
        walletBalance += amount;
        return true;
    }

    public boolean canWithdraw(double amount) {
        return amount > 0 && amount <= walletBalance;
    }

    /** Seller withdraws collected COD. Returns false if amount is invalid. */
    public boolean withdraw(double amount) {
        if (!canWithdraw(amount)) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seller seller)) {
            return false;
        }
        return Objects.equals(getId(), seller.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }
}
