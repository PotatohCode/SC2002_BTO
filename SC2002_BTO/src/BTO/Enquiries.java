package BTO;

import java.util.List;

public class Enquiries implements Search<Integer> {
	
	private int enquiryId;
	private int btoId;
	private static int idCounter = 0;
	private int enquirierId;
    private int replierId = -1;
    private String enquiry;
    private String reply;
    
    public Enquiries(String enquiry, int enquirierId, int btoId) {
    	this.enquiry = enquiry;
    	this.enquirierId = enquirierId;
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
    
    public int getReplierId() {
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
    	if ((role == "officer" || role == "manager")) {
    		this.reply = reply;	
    		this.replierId = id;
    		System.out.println("\u001B[32m" + "Reply submitted!\n" + "\u001B[0m");
    	} else {
    		System.out.println("\u001B[31m" + "Unable to reply\n" + "\u001B[0m");
    	}
    }
    
    // functions
    public void printEnquiry() {
    	System.out.println("Enquiry Id: " + this.enquiryId
    						+ "\nEnquiry: " + this.enquiry
    						+ (this.replierId > -1 ? "\nReply: " + this.reply : ""));
    }

	@Override
	public boolean getById(Integer id) {
		return this.enquiryId == id;
	}
	
	@Override
	public boolean getByBTO(Integer id) {
		return this.btoId == id;
	}
	
	@Override
	public boolean getByUser(Integer id) {
		return this.enquirierId == id;
	}
}
