import java.util.ArrayList;
import java.util.List;

public class ItemBox {
    public ArrayList<Items> items;

    public ItemBox() {
        this.items = new ArrayList<>();
    }

    public void addItem(Items item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        items.add(item);
    }

    public boolean removeItem(String itemID) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).itemID.equals(itemID)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public Items findItem(String itemID) {
        for (Items item : items) {
            if (item.itemID.equals(itemID)) {
                return item;
            }
        }
        return null;
    }

    public List<Items> getAllItems() {
        return new ArrayList<>(items);
    }

    public int getItemCount() {
        return items.size();
    }

    public int getTotalStock() {
        int total = 0;
        for (Items item : items) {
            total += item.stockCount;
        }
        return total;
    }

    public float getTotalInventoryValue() {
        float total = 0f;
        for (Items item : items) {
            total += item.itemPrice * item.stockCount;
        }
        return total;
    }

    public boolean reduceStock(String itemID, int quantity) {
        Items item = findItem(itemID);
        if (item == null || quantity <= 0) {
            return false;
        }
        if (item.stockCount < quantity) {
            return false;
        }
        item.stockCount -= quantity;
        return true;
    }

    public void updateItem(Items updatedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).itemID.equals(updatedItem.itemID)) {
                items.set(i, updatedItem);
                return;
            }
        }
        addItem(updatedItem);
    }
}