import java.util.List;
public class RestoreJson {
    public static void main(String[] args) throws Exception {
        DAO dao = new DAO("data.json");
        ItemBox items = dao.loadItemBox();
        List<Person> users = dao.loadUsers();
        System.out.println("Restoring JSON with " + items.getAllItems().size() + " items and " + users.size() + " users");
        dao.persistUsersJson(items, users);
        System.out.println("Restore complete");
    }
}
