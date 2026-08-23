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
	int stockCount;

	Items(String itemID, String itemName, String Description, int itemWeight, int itemSize, float itemPrice, int itemDiscount, int stockCount) {
		this.itemID = itemID;
		this.itemName = itemName;
		this.Description = Description;
		this.itemWeight = itemWeight;
		this.itemSize = itemSize;
		this.itemPrice = itemPrice;
		this.itemDiscount = itemDiscount;
		this.stockCount = stockCount;
	}

	public String getDescription() {
    return Description;
	}

	public int getItemDiscount() {
    return itemDiscount;
	}

	public String getItemID() {
    return itemID;
	}

	public String getItemName() {
    return itemName;
	}

	public float getItemPrice() {
    return itemPrice;
	}

	public int getItemSize() {
    return itemSize;
	}

	public int getItemWeight() {
    return itemWeight;
	}

	public int getStockCount() {
    return stockCount;
	}
	
	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}
	
	public float getItemPriceAfterDiscount() {
		return itemPrice * (1 - itemDiscount / 100.0f);
	}
	
	public void setItemDiscount(int itemDiscount) {
		this.itemDiscount = itemDiscount;
	}
	
	public void setItemPrice(float itemPrice) {
		this.itemPrice = itemPrice;
	}
}