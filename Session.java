class Session{

	Admin admin;
	ItemBox itemBox;

	Session(Admin admin, ItemBox itemBox) {
		this.admin = admin;
		this.itemBox = itemBox;	}
	
	public Admin getAdmin() {
		return admin;
	}
	
	public ItemBox getItemBox() {
		return itemBox;
	}
}