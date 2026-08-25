public class PrintUsers {
    public static void main(String[] args) {
        AppController ctrl = new AppController();
        System.out.println("Loaded users:");
        for (Person p : ctrl.getPeople()) {
            System.out.printf("govID=%s, name=%s, username=%s, password=%s, class=%s\n",
                    p.getGovID(), p.getName(), p.getUserName(), p.getPassword(), p.getClass().getSimpleName());
        }
    }
}
