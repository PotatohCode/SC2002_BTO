package BTO;

public class Enquiries implements Searchable {
	
	private int enquiryId;
	private int btoId;
	private static int idCounter = 0;
	private int enquirierId;
    private int replierId = -1;
    private String enquiry;
    private String reply;
    
    public Enquiries(String enquiry, int btoId) {
    	this.enquiry = enquiry;
    	this.btoId = btoId;
    	this.enquiryId = idCounter++;
    }
    
    // getter
    public int getId() {
    	return this.enquiryId;
    }
    
    public String getEnquiry() {
        return this.enquiry;
    }

    public String getReply() {
        return this.reply;
    }
    
    public int getEnquirierId() {
    	return this.enquirierId;
    }
    
    public int getRepierId() {
    	return this.replierId;
    }
    
    public int getBTOId() {
    	return this.btoId;
    }

    // setter
    public void setEnquiry(String enquiry) {
        this.enquiry = enquiry;
    }

    public void setReply(String reply, int id, String role) {
    	if (role == "officer" || role == "manager" ) {
    		this.reply = reply;	
    		this.replierId = id;
    	}
    }
    
    // functions
    public void printEnquiry() {
    	System.out.println("Enquiry Id: " + this.enquirierId
    						+ "\nEnquiry: " + this.enquiry
    						+ (this.replierId > -1 ? "\nReply: " + this.reply : "")
    						+ "\n");
    }
    
	// search by id
	@Override
	public boolean searchId(int id) {
		return this.enquiryId == id;
	}

	@Override
	public boolean searchOtherId(int id) {
		return this.btoId == id;
	}
    
    // function
//    public void printEnquiry() {
//        System.out.println("Applicant: " + applicant.getNric());
//        System.out.println("BTO Project: " + bto.getName());
//        System.out.println("Enquiry: " + enquiry);
//        if (reply.isEmpty()) {
//            System.out.println("Reply: No reply yet");
//        } else {
//            System.out.println("Reply: " + reply);
//        }
//        if (replier != null)
//            System.out.println("Replied by: " + replier.getNric());
//    }

}
