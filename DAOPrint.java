import java.util.List;
public class DAOPrint {
    public static void main(String[] args) {
        try {
            DAO dao = new DAO("data.json");
            List<Person> users = dao.loadUsers();
            System.out.println("DAO.loadUsers returned " + users.size() + " users");
            for (Person p : users) {
                System.out.printf("govID=%s, name=%s, username=%s, password=%s, class=%s\n",
                        p.getGovID(), p.getName(), p.getUserName(), p.getPassword(), p.getClass().getSimpleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
