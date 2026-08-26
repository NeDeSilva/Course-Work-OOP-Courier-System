package BusinessLogic;
class Session {

	Admin admin;
	Seller seller;
	Customer customer;
	Driver driver;
	Box itemBox;

	Session(
		Admin admin,
		Seller seller,
		Customer customer,
		Driver driver,
		Box itemBox
	) {
		this.admin = admin;
		this.itemBox = itemBox;
		this.seller = seller;
		this.customer = customer;
		this.driver = driver;
	}

	String getSession(){
		return "Session";
	}
}
