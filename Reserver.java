/**
 * Reserver - a customer who tracks an existing parcel by its tracking id.
 */
public class Reserver extends User {

	public Reserver() {
		super();
	}

	public Reserver(String username, String password, String name, String email,
			String address, String phone, int age) {
		super(username, password, name, email, address, phone, age);
	}
}
