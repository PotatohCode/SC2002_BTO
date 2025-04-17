package BTO;

/**
 * 
 *  @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 *  @version 16/4/25
 *  
 *  Applicant.java: generic class for all users
 * 
 */
public class Applicant extends Users{
	
	private int applicationId;
	private int[] enquiriesId;
	private int btoId;

	public Applicant(String nric, String name, String password, boolean married) {
		super(nric, name, password, married, "applicant");
	}
	
	public Applicant(String nric, String name, String password, boolean married, String role) {
		super(nric, name, password, married, role);
	}
	
	// functions
	public BTO getBTODetails() {
		
	}
	
	public Application getApplication() {
		
	}
	
	public void createApplication() {
		
	}
	
	public void removeApplication(int id) {
		
	}
	
	public boolean canApply(BTO bto) {
		
	}
	
	public void viewEnquiries() {
		
	}

}
