public class Session {
    public Admin admin;
    public ItemBox itemBox;

    public Session(Admin admin, ItemBox itemBox) {
        this.admin = admin;
        this.itemBox = itemBox;
    }

    public Session(Person user, ItemBox itemBox) {
        if (user instanceof Admin) {
            this.admin = (Admin) user;
        } else {
            this.admin = new Admin(user.govID, user.name, user.age, user.address, user.phoneNumber, user.emailAddress, user.userName, user.password);
        }
        this.itemBox = itemBox;
    }

    public Admin getAdmin() {
        return admin;
    }

    public ItemBox getItemBox() {
        return itemBox;
    }
}
