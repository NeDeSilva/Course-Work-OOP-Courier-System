import java.util.List;
public class PersistRolesNow {
    public static void main(String[] args) {
        try {
            DAO dao = new DAO("data.json");
            List<Person> users = dao.loadUsers();
            System.out.println("Loaded " + users.size() + " users, persisting back to JSON with inferred roles");
            dao.persistUsersJson(new ItemBox(), users);
            System.out.println("Persist complete. Please inspect data.json");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
