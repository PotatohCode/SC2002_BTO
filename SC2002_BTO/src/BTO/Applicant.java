import java.util.ArrayList;
import java.util.List;
package BTO;

public class Applicant extends Users{
	private boolean visibility;
	private boolean hasBooked;
	private String application;
	private List<Enquiry> enquiries;
	private BTO appliedBTO;
	
	public Applicant(String nric, String password, int age, boolean married) {
		// TODO Auto-generated constructor stub
		super(nric, password, age, married);
		this.visibility = true;
		this.hasBooked = false;
		this.applicationStatus = "Pending";
		this.enquiries = new ArrayList<>();
		
	}
	
	//view the list of projects
	public List<BTO> viewBTOs(List<BTO> allBTOs) {
		List<BTO> visibleBTOs = new ArrayList<>();
        for (BTO bto : allBTOs) {
        	// if their visibility has been toggled “on” and the project is open to their user group (Single or Married)
            if () {
                visibleBTOs.add(bto);
            }
        }
        return visibleProjects;
	}
	
	public void applyForProject(BTO bto) {
		// if a vacancy exists
		if (this.appliedBTO != null) {
            System.out.println("You have already applied for a project.");
            return;
        }
		
		// based on age and marital status
	}
	
	// View the applied BTO project and its status
    public void viewAppliedBTOs() {
        if (this.appliedBTO != null) {
            System.out.println("Applied Project: " + this.appliedBTO.getName());
            System.out.println("Status: " + this.applicationStatus);
        } 
        else {
            System.out.println("No project applied yet.");
        }
    }
    
    // Book a flat with help from HDB officer
    public void bookFlat(Officer officer) {
    	
    }
    
    // Withdraw application
    public void withdrawApplication() {
    	if (this.appliedBTO != null) {
    		this.appliedBTO.removeApplicant(); // replace by any BTO function of removal.
            this.appliedBTO = null;
            this.applicationStatus = "Withdrawn";
            System.out.println("Your application has been withdrawn.");
            } 
    	else {
            System.out.println("No project to withdraw from.");
           }
    }
    
    // Submit an enquiry
    public void submitEnquiry(String enquiryString) {
        Enquiry enquiry = new Enquiry(enquiryString);
        this.enquiries.add(enquiry);
        System.out.println("Enquiry submitted.");
    }

    // View all enquiries
    public void viewEnquiries() {
        for (Enquiry enquiry : this.enquiries) {
            System.out.println(enquiry.getEnquiry());
        }
    }

    // Edit an enquiry
    public void editEnquiry(int index, String newEnquiry) {
        if (index >= 0 && index < this.enquiries.size()) {
            this.enquiries.get(index).setEnquiry(newEnquiry);
            System.out.println("Enquiry edited.");
        } else {
            System.out.println("Invalid enquiry index.");
        }
    }

    // Delete an enquiry
    public void deleteEnquiry(int index) {
        if (index >= 0 && index < this.enquiries.size()) {
            this.enquiries.remove(index);
            System.out.println("Enquiry deleted.");
        } else {
            System.out.println("Invalid enquiry index.");
        }
    }
	
    // Toggle visibility
    public void toggleVisibility() {
        this.visibility = !this.visibility;
    }

    // Helper method to get marital status (true for married, false for single)
    public boolean getMarriedStatus() {
        return this.getMarried();
    }		
}
