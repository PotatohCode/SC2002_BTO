package BTO;

public class Enquiries {
	private Applicant applicant;
    private BTO bto;
    private Users replier;  
    private String enquiry;
    private String reply;
    
    public Enquiries(Applicant applicant, BTO bto, String enquiry) {
        this.applicant = applicant;
        this.bto = bto;
        this.enquiry = enquiry;
        this.reply = ""; // not sure what to put here
        this.replier = null; // not sure what to put here
    }
    public String getEnquiry() {
        return this.enquiry;
    }

    public String getReply() {
        return this.reply;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public Users getReplier() {
        return this.replier;
    }

    public void setEnquiry(String enquiry) {
        this.enquiry = enquiry;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setReplier(Users user) {
        this.replier = user;
    }
    
    public void printEnquiry() {
        System.out.println("Applicant: " + applicant.getNric());
        System.out.println("BTO Project: " + bto.getName());
        System.out.println("Enquiry: " + enquiry);
        if (reply.isEmpty()) {
            System.out.println("Reply: No reply yet");
        } else {
            System.out.println("Reply: " + reply);
        }
        if (replier != null)
            System.out.println("Replied by: " + replier.getNric());
    }

}
