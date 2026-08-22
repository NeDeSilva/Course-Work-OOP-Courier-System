/**
 * Items
 */
public class Items {

	String itemID;
	String itemName;
	String Description;
	int itemWeight;
	int itemSize;
	float itemPrice;
	int itemDiscount;
	boolean itemAvailability;
	int stockCount;
	int expireDate;
	int manufactureDate;

	public Items(String itemName) {
		this.itemName = itemName;
	}
}
