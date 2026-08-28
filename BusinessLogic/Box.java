package BusinessLogic;
/**
 * ItemBox
 */

 import java.util.ArrayList;
public class Box {
	ArrayList<Item> itemsInBox = new ArrayList<Item>();
	private int boxID;
	private int customerID;
	private float weight;
	private float length;
	private float width;
	private float height;
	private String description;
	private String status;
	
	Box(int boxID, int customerID, float weight, float length, float width, float height, String description, String status) {
		this.boxID = boxID;
		this.customerID = customerID;
		this.weight = weight;
		this.length = length;
		this.width = width;
		this.height = height;
		this.description = description;
		this.status = status;
	}

	void addItem(Item item) {
		itemsInBox.add(item);
	}
	
	Item removeItem(Item item) {
		itemsInBox.remove(item);
		return item;
	}
	
	int getItemCount() {
		return itemsInBox.size();
	}
	
	float getTotalPrice() {
		float total = 0;
		for (Item item : itemsInBox) {
			total += item.getItemPriceAfterDiscount();
		}
		return total;
	}
	
	public ArrayList<Item> getItems() {
		return itemsInBox;
	}
	
}