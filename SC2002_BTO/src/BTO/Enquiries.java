package BTO;

import java.util.List;


/**
 * Represents an enquiry submitted by a user regarding a BTO project.
 * 
 * Each enquiry contains a user-submitted question or concern, the user ID of the enquirer,
 * and optionally a reply from an officer or manager. The enquiry is linked to a specific
 * BTO project by ID, and can be edited or deleted by the enquirer.
 * 
 * Replies can only be submitted by users with the "officer" or "manager" role.
 * 
 * Each enquiry is uniquely identified by an auto-incremented enquiry ID.
 * 
 * This class supports printing formatted enquiry information for display purposes.
 * 
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 */
public class Enquiries implements Search<Integer> {
	
	private int enquiryId;
	private int btoId;
	private static int idCounter = 0;
	private int enquirierId;
    private int replierId = -1;
    private String enquiry;
    private String reply;
    
    /**
     * Constructs a new enquiry associated with a given BTO project and enquirer.
     *
     * @param enquiry the question or message submitted by the user
     * @param enquirierId the ID of the user submitting the enquiry
     * @param btoId the ID of the BTO project this enquiry is about
     */
    public Enquiries(String enquiry, int enquirierId, int btoId) {
    	this.enquiry = enquiry;
    	this.enquirierId = enquirierId;
    	this.btoId = btoId;
    	this.enquiryId = idCounter++;
    }
    
    // getter
	
	/**
	 * Returns the unique ID assigned to this enquiry.
	 *
	 * @return the enquiry ID
	 */
    public int getId() {
    	return this.enquiryId;
    }
    
	
	/**
	 * Retrieves the content of the enquiry submitted by the user.
	 *
	 * @return the enquiry message
	 */
    public String getEnquiry() {
        return this.enquiry;
    }
    
    /**
     * Retrieves the reply to the enquiry, if any.
     *
     * @return the reply message, or null if no reply has been submitted
     */
    public String getReply() {
        return this.reply;
    }
    

	/**
	 * Returns the ID of the user who submitted the enquiry.
	 *
	 * @return the enquirer's user ID
	 */
    public int getEnquirierId() {
    	return this.enquirierId;
    }
    
    /**
     * Returns the ID of the user who replied to the enquiry.
     *
     * @return the replier's user ID, or -1 if no reply has been made
     */
    public int getReplierId() {
    	return this.replierId;
    }
    
	
	/**
	 * Returns the ID of the BTO project associated with this enquiry.
	 *
	 * @return the BTO project ID
	 */
    public int getBTOId() {
    	return this.btoId;
    }

    // setter
    
    /**
     * Updates the enquiry content.
     *
     * @param enquiry the new enquiry message
     */
    public void setEnquiry(String enquiry) {
        this.enquiry = enquiry;
    }
    
    /**
     * Sets a reply to the enquiry if the role is authorised.
     * Only "officer" or "manager" roles can reply.
     *
     * @param reply the reply content
     * @param id the user ID of the replier
     * @param role the role of the replier ("officer" or "manager")
     */
    public void setReply(String reply, int id, String role) {
    	if ((role == "officer" || role == "manager")) {
    		this.reply = reply;	
    		this.replierId = id;
    		System.out.println("\u001B[32m" + "Reply submitted!\n" + "\u001B[0m");
    	} else {
    		System.out.println("\u001B[31m" + "Unable to reply\n" + "\u001B[0m");
    	}
    }
    
    // functions
    /**
     * Prints the enquiry details to the console.
     * Includes the enquiry ID, message, and optionally the reply if present.
     */
    public void printEnquiry() {
    	System.out.println("Enquiry Id: " + this.enquiryId
    						+ "\nEnquiry: " + this.enquiry
    						+ (this.replierId > -1 ? "\nReply: " + this.reply : ""));
    }
    
    /**
     * Checks if this enquiry matches the given ID.
     *
     * @param id the enquiry ID to check
     * @return true if this enquiry has the matching ID, false otherwise
     */
	@Override
	public boolean getById(Integer id) {
		return this.enquiryId == id;
	}
	
	/**
	 * Checks if this enquiry belongs to the specified BTO project.
	 *
	 * @param id the BTO project ID
	 * @return true if the enquiry is associated with the given BTO ID, false otherwise
	 */
	@Override
	public boolean getByBTO(Integer id) {
		return this.btoId == id;
	}
	
	/**
	 * Checks if this enquiry was submitted by the specified user.
	 *
	 * @param id the user ID to match
	 * @return true if the user submitted this enquiry, false otherwise
	 */
	@Override
	public boolean getByUser(Integer id) {
		return this.enquirierId == id;
	}
}
