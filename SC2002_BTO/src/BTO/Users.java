package BTO;

import java.util.Objects;

/**
 * A base class representing all users in the BTO system.
 * 
 * This class encapsulates common user data such as NRIC, name, age, marital status,
 * and role, along with methods for password checking and searching by NRIC.
 * 
 * It serves as the parent class for role-specific user types such as:
 * <ul>
 *   <li>{@link Applicant}</li>
 *   <li>{@link Officer}</li>
 *   <li>{@link Manager}</li>
 * </ul>
 * 
 * Each user is automatically assigned a unique ID.
 * The class also supports ANSI formatting constants for coloured console output.
 * 
 * @see Applicant
 * @see Officer
 * @see Manager
 * 
 * This class is intended to be extended rather than used directly.
 * 
 * 
 * @version 16/4/25
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 */

public class Users implements Search<String> {
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	
	private int userId;
	private static int idCounter = 0;
	protected String nric;
	private String name;
	private int age;
	private String password = "password";
	private boolean isMarried;
	private String filter = "alpha";
	private String role;
	private String filterNeighbourhood = "";
	private int filterRoomType = -1; // -1 = any, 2 = 2-room only, 3 = 3-room only
	private boolean filterOpenOnly = true;
	private String filterProjectName = "";


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

	/**
	 * Returns the neighbourhood keyword filter applied by the user.
	 *
	 * @return keyword for neighbourhood filtering
	 */
	public String getFilterNeighbourhood() {
	    return this.filterNeighbourhood;
	}
	
	/**
	 * Returns the selected room type filter.
	 *
	 * @return 2, 3, or -1 for "any"
	 */
	public int getFilterRoomType() {
	    return this.filterRoomType;
	}
	
	/**
	 * Returns whether the user wants to see only currently open BTO applications.
	 *
	 * @return true if filtering to open applications only
	 */
	public boolean isFilterOpenOnly() {
	    return this.filterOpenOnly;
	}
	
	/**
	 * Returns the project name keyword filter applied by the user.
	 *
	 * @return keyword for project name filtering
	 */
	public String getFilterProjectName() {
	    return this.filterProjectName;
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
	
	/**
	 * Check Password: check password without need to return password from this class
	 * @param input_password : password input from user
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
	 * Updates the user's password to a new value.
	 * 
	 * @param new_password the new password to be set
	 */
	public void changePassword(String new_password) {
		this.password = new_password;
	}
	
	/**
	 * Sets the neighbourhood keyword filter for BTO search.
	 *
	 * @param filterNeighbourhood the filter keyword
	 */
	public void setFilterNeighbourhood(String filterNeighbourhood) {
	    this.filterNeighbourhood = filterNeighbourhood;
	}
	
	
	/**
	 * Sets the room type filter (2, 3, or -1).
	 *
	 * @param filterRoomType the room type filter value
	 */
	public void setFilterRoomType(int filterRoomType) {
	    this.filterRoomType = filterRoomType;
	}
	
	/**
	 * Sets whether the BTO filter should include only currently open projects.
	 *
	 * @param filterOpenOnly true to filter by open projects only
	 */
	public void setFilterOpenOnly(boolean filterOpenOnly) {
	    this.filterOpenOnly = filterOpenOnly;
	}
	

	/**
	 * Sets the keyword used to filter project names in BTO search.
	 *
	 * @param filterProjectName the project name filter keyword
	 */
	public void setFilterProjectName(String filterProjectName) {
	    this.filterProjectName = filterProjectName;
	}
	
	/**
	 * Search for user by NRIC
	 * @param nric : get nric input
	 * @return boolean : whether the current object is the user
	 */
	public boolean searchNric(String nric) {
		return this.nric == nric;
	}
	
	/**
	 * Returns true if this user matches the given ID (for interface-based search).
	 *
	 * @param id the NRIC to match
	 * @return true if matched, false otherwise
	 */
	@Override
	public boolean getById(String id) {
		return this.nric.equals(id);
	}
}
