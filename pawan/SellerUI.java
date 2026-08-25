package pawan;
import java.util.ArrayList;
import java.util.List;

import CoreUI;
import model.Seller;
/**
 * Console screen used by the operator to manage sellers.
 */
public class SellerUI extends CoreUI {
    private final List<Seller> sellers = new ArrayList<>();
    private int sequence = 1;
    public SellerUI() {
        super("Seller Management");
    }
    @Override
    public void showMenu() {
        System.out.println(" 1. Register seller");
        System.out.println(" 2. List sellers");
        System.out.println(" 3. Verify seller");
        System.out.println(" 4. Add parcel to seller");
        System.out.println(" 5. Credit COD to wallet");
        System.out.println(" 6. Withdraw from wallet");
        System.out.println(" 7. Delete seller");
        System.out.println(" 0. Back");
        line();
    }
    @Override
    public boolean handle(int choice) {
        switch (choice) {
            case 1 -> register();
            case 2 -> list();
            case 3 -> verify();
            case 4 -> addParcel();
            case 5 -> creditCod();
            case 6 -> withdraw();
            case 7 -> delete();
            case 0 -> {
                return false;
            }
            default -> error("Unknown option.");
        }
        return true;
    }
    private void register() {
        Seller seller = new Seller(
                String.format("SL-%03d", sequence),
                prompt("Name"),
                prompt("Email"),
                prompt("Phone"),
                prompt("Address"),
                prompt("Shop name"),
                prompt("Business type"),
                prompt("Pickup area"));
        if (!seller.isValid()) {
            error("Invalid seller data, registration cancelled.");
            return;
        }
        sellers.add(seller);
        sequence++;
        success("Seller registered with id " + seller.getId());
    }
    private void list() {
        if (sellers.isEmpty()) {
            info("No sellers registered yet.");
            return;
        }
        sellers.forEach(s -> System.out.println(" - " + s));
    }
    private Seller find(String id) {
        return sellers.stream()
                .filter(s -> s.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
    private void verify() {
        Seller seller = find(prompt("Seller id"));
        if (seller == null) {
            error("Seller not found.");
            return;
        }
        seller.setVerified(true);
        success(seller.getName() + " is now verified.");
    }
    private void addParcel() {
        Seller seller = find(prompt("Seller id"));
        if (seller == null) {
            error("Seller not found.");
            return;
        }
        seller.addParcel(prompt("Parcel id"));
        success("Parcel attached. Total: " + seller.getParcelIds().size());
    }
    private void creditCod() {
        Seller seller = find(prompt("Seller id"));
        if (seller == null) {
            error("Seller not found.");
            return;
        }
        seller.creditCod(promptDouble("Amount"));
        success(String.format("Wallet balance: %.2f", seller.getWalletBalance()));
    }
    private void withdraw() {
        Seller seller = find(prompt("Seller id"));
        if (seller == null) {
            error("Seller not found.");
            return;
        }
        if (seller.withdraw(promptDouble("Amount"))) {
            success(String.format("Withdrawn. Balance: %.2f", seller.getWalletBalance()));
        } else {
            error("Insufficient balance.");
        }
    }
    private void delete() {
        Seller seller = find(prompt("Seller id"));
        if (seller == null) {
            error("Seller not found.");
            return;
        }
        sellers.remove(seller);
        success("Seller removed.");
    }
    public static void main(String[] args) {
        new SellerUI().run();
    }
}