package BTO;

public class Users {
	private String nric;
	private String password = "password";
	private int age;
	private boolean married;

	public Users(String nric, String password, int age, boolean married, boolean test) {
		this.nric = nric;
		this.password = password;
		this.age = age;
		this.married = married;
	}

}
