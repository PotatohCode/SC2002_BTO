package BTO;

import java.util.Objects;

/**
 * 
 *  @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 *  @version 16/4/25
 *  
 *  Users.java: generic class for all users
 * 
 */
public class Users {
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_GREEN = "\u001B[32m";
	
	private int userId;
	private static int idCounter = 0;
	protected String nric;
	private String name;
	private int age;
	private String password = "password";
	private boolean isMarried;
	private String filter = "alpha";
	private String role;

	/**
	 * Constructor class
	 * @param nric : get nric of user
	 * @param name : get name of user
	 * @param age : get age of user
	 * @param password : get password of user
	 * @param married : get marriage status of user
	 * @param role : get role of user; auto fill from child class
	 * id : identifier that is auto incremented for each creation of user
	 */
	public Users(String nric, String name, int age, String password, boolean married, String role) {
		this.nric = nric;
		this.name = name;
		this.age = age;
		this.password = password;
		this.isMarried = married;
		this.role = role;
		this.userId = idCounter++;
	}
	
	// getters
	/**
	 * Get User Id
	 * @return userId
	 */
	public int getId() {
		return this.userId;
	}
	/**
	 * Get NRIC
	 * @return nric
	 */
	protected String getNric() {
		return this.nric;
	}
	
	/**
	 * Get Name
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get Age
	 * @return age
	 */
	public int getAge() {
		return this.age;
	}
	
	/**
	 * Get Married Status
	 * @return isMarried
	 */
	public boolean getMarried() {
		return this.isMarried;
	}
	
	/**
	 * Get Filter
	 * @return filter
	 */
	public String getFilter() {
		return this.filter;
	}
	
	/**
	 * Get Role
	 * @return role
	 */
	public String getRole() {
		return this.role;
	}
	
	// setter
	/**
	 * Set Married
	 * @param new_married : input of new married value
	 */
	public void setMarried(boolean new_married) {
		this.isMarried = new_married;
	}
	
	/**
	 * Set Filter
	 * @param new_filter : input of new filter value
	 */
	public void setFilter(String new_filter) {
		this.filter = new_filter;
	}
	
	// functions
	public void menu() {};
	
	/**
	 * Check Password: check passowrd without need to return password from this class
	 * @param input_password : password inputted from user
	 * @return boolean valid : whether password is valid
	 */
	public boolean checkPassword(String input_password) {
		if (Objects.equals(input_password, this.password)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Search for user by NRIC
	 * @param nric : get nric input
	 * @return boolean : whether the current object is the user
	 */
	public boolean searchNric(String nric) {
		return this.nric == nric;
	}
}
