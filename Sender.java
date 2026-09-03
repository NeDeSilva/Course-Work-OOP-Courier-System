/**
 * Sender - a customer who sends parcels.
 */
public class Sender extends User {

	public Sender() {
		super();
	}

	public Sender(String username, String password, String name, String email,
			String address, String phone, int age) {
		super(username, password, name, email, address, phone, age);
	}
}
