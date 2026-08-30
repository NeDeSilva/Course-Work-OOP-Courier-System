/**
 * Employer - an employee of the courier company (Admin or Driver).
 * Extends User with salary and joining date.
 */
public class Employer extends User {
	private double salary;
	private String joiningDate;

	public Employer() {
		super();
	}

	public Employer(String username, String password, String name, String email,
			String address, String phone, int age, double salary, String joiningDate) {
		super(username, password, name, email, address, phone, age);
		this.salary = salary;
		this.joiningDate = joiningDate;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}
}
