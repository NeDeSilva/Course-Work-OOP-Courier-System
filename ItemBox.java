/**
 * ItemBox
 */

 import java.util.ArrayList;
public class ItemBox {
	ArrayList<Items> itemsInBox = new ArrayList<Items>();

	ItemBox() {
		
	}

	void addItem(Items item) {
		itemsInBox.add(item);
	}
	
	Items removeItem(Items item) {
		itemsInBox.remove(item);
		return item;
	}
	
	int getItemCount() {
		return itemsInBox.size();
	}
	
	float getTotalPrice() {
		float total = 0;
		for (Items item : itemsInBox) {
			total += item.getItemPriceAfterDiscount();
		}
		return total;
	}
	
	public ArrayList<Items> getItems() {
		return itemsInBox;
	}
	
}