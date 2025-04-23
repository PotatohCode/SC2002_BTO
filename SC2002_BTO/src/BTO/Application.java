package BTO;

/**
 * Represents an application in the BTO system.
 * 
 * This class handles both BTO housing applications and officer applications.
 * Each application tracks the user who applied, the BTO project associated with the application,
 * the type of application (e.g., "bto" or "officer"), and its current status.
 * 
 * Applications are uniquely identified using an auto-incremented ID.
 * 
 * Implements {@link Search} to support filtered access by application ID, BTO ID, or user ID.
 * 
 * @see Users
 * @see BTO
 * @see Officer
 * @see Applicant
 * 
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 */
public class Application implements Search<Integer> {
	
	private int applicationId;
	private static int idCounter = 0;
	private Users applicant;
	private int btoId;
	private int roomType;
	private String status = "pending";
	private String type = "bto";
	
	// officer application
	/**
	 * Constructs an officer application.
	 *
	 * @param applicant the user applying to be an officer
	 * @param btoId the ID of the BTO project the user is applying to
	 * @param status the initial status of the application
	 * @param type the type of the application (should be "officer")
	 */
	public Application(Users applicant, int btoId, String status, String type) {
		this.applicant = applicant;
		this.btoId = btoId;
		this.status = status;
		this.type = type;
		this.applicationId = idCounter++;
	}
	
	// bto application

	/**
	 * Constructs a BTO flat application.
	 *
	 * @param applicant the user applying for the BTO flat
	 * @param btoId the ID of the BTO project
	 * @param roomType the room type requested (2 or 3)
	 */
	public Application(Users applicant, int btoId, int roomType) {
		this.applicant = applicant;
		this.btoId = btoId;
		this.roomType = roomType;
		this.applicationId = idCounter++;
	}
	
	// getter
	
	/**
	 * Returns the unique application ID.
	 * @return the application ID
	 */
	public int getId() {
		return this.applicationId; 
	}
	
	/**
	 * Returns the applicant who submitted this application.
	 * @return the applicant as a {@link Users} object
	 */
	public Users getApplicant() {
		return this.applicant;
	}
	
	/**
	 * Returns the BTO project ID associated with this application.
	 * @return the BTO project ID
	 */
	public int getBTOId() {
		return this.btoId;
	}
	
	/**
	 * Returns the requested room type (for BTO applications only).
	 * @return the room type (2 or 3), or 0 for officer applications
	 */
	public int getRoomType() {
		return this.roomType;
	}
	
	/**
	 * Returns the current status of the application.
	 * @return the status string (e.g., "pending", "successful")
	 */
	public String getStatus() {
		return this.status;
	}
	
	/**
	 * Returns the type of application ("bto" or "officer").
	 * @return the application type
	 */
	public String getType() {
		return this.type;
	}
	
	// setter
//	public void setRoomType(int roomType) {
//		if (roomType == 2 || roomType == 3) {
//			this.roomType = roomType;
//		}
//	}
	
	/**
	 * Updates the status of the application based on user role and type of application.
	 *
	 *
	 * @param status the new status to apply
	 * @param role the role of the user updating the application
	 */
	public void setStatus(String status, String role) {
		if (type == "bto" && (role == "manager" || role == "officer")) {
			this.status = status; 
		} else if (type == "bto" && role == "applicant" && (status.equals("withdrawing") || status.equals("booking"))) {
			this.status = status;
		} else if (type == "officer" && role == "manager") {
			this.status = status;
		}
	}
	
	// functions
	
	/**
	 * Prints the application details, including ID, status, and room type if applicable.
	 */
	public void printApplication() {
		System.out.println("Application Id: " + this.applicationId 
							+ "\nStatus: " + this.status
							+ "\nApplicant Details: " + this.applicant.getName() + " (Age: " + this.applicant.getAge() + ") " + (this.applicant.getMarried() ? "Married" : "Single") 
							+ (this.type.equals("bto") ? "\nRoom Type: "  + this.roomType : ""));
	}
	
	/**
	 * Checks if this application matches the given application ID.
	 * 
	 * @param id the application ID to check
	 * @return true if it matches, false otherwise
	 */
	@Override
	public boolean getById(Integer id) {
		return this.applicationId == id;
	}
	
	/**
	 * Checks if this application is for the specified BTO project.
	 * 
	 * @param id the BTO project ID
	 * @return true if the BTO ID matches, false otherwise
	 */
	@Override
	public boolean getByBTO(Integer id) {
		return this.btoId == id;
	}
	
	/**
	 * Checks if this application belongs to the specified user.
	 * 
	 * @param id the user ID
	 * @return true if the user ID matches the applicant, false otherwise
	 */
	@Override
	public boolean getByUser(Integer id) {
		return this.applicant.getId() == id;
	}
}
