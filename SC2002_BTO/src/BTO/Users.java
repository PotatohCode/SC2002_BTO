package BTO;

import java.util.Objects;

public class Users {
	
	private String nric;
	private String password = "password";
	private int age;
	private boolean isMarried;
	private String filter = "alpha";
	private String role = "applicant";

	public Users(String nric, String password, int age, boolean married) {
		this.nric = nric;
		this.password = password;
		this.age = age;
		this.isMarried = married;
	}
	
	public String getNric() {
		return this.nric;
	}

	public boolean checkPassword(String input_password) {
		if (Objects.equals(input_password, this.password)) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getAge() {
		return this.age;
	}
	
	public boolean getMarried() {
		return this.isMarried;
	}
	
	public String getFilter() {
		return this.filter;
	}
	
	public void setFilter(String new_filter) {
		this.filter = new_filter;
	}
	
	public String getRole() {
		return this.role;
	}
}
