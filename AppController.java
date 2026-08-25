import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Simple controller for prototype: holds model and DAO and provides operations
public class AppController {
    private final ItemBox itemBox;
    private final List<Person> people;
    private final DAO dao;

    public AppController() {
        this.itemBox = new ItemBox();
        this.people = new ArrayList<>();
        this.dao = new DAO("data.json");
        try {
            ItemBox loaded = dao.loadItemBox();
            this.itemBox.items.addAll(loaded.getAllItems());
            this.people.addAll(dao.loadUsers());
        } catch (IOException e) {
            // ignore for prototype; UI will show status
        }
    }

    public ItemBox getItemBox() {
        return itemBox;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void addItem(Items item) {
        itemBox.addItem(item);
    }

    public void addUser(Person p) {
        people.add(p);
    }

    public boolean saveAll() {
        try {
            dao.saveData(itemBox, people);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}