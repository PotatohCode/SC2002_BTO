package BTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 *  @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 *  @version 16/4/25
 *  
 *  Applicant.java: generic class for all users
 * 
 */
public class Applicant extends Users {
	
	private int applicationId = -1;
	private List<Integer> enquiriesId = new ArrayList<>();
	protected int btoId = -1;
	
	private Scanner sc = new Scanner(System.in);

	public Applicant(String nric, String name, int age, String password, boolean married) {
		super(nric, name, age, password, married, "applicant");
	}
	
	public Applicant(String nric, String name, int age, String password, boolean married, String role) {
		super(nric, name, age, password, married, role);
	}
	
	// getters
	public int getApplicationId() {
		return this.applicationId;
	}
	
	public List<Integer> getEnquiriesId() {
		return this.enquiriesId;
	}
	
	public int getBTOId() {
		return this.btoId;
	}
	
	// functions
	// viewing bto
	public List<BTO> getApplicableBTOs(List<BTO> btoList) {
		return btoList.stream().filter(b -> this.canApplyBTO(b, (this.getMarried() ? -1 : 2))).toList();
	}
	
	// check qualification
	public boolean canApplyBTO(BTO bto, int roomType) {
		boolean can = false;
		// check user qualification
		if (this.btoId == -1 && bto.getVisible()
				&& ((this.getMarried() && this.getAge() >= 21) 
				|| (!this.getMarried() && this.getAge() >= 35 && roomType == 2))) {
			// check bto qualification
			switch (roomType) {
				case -1: can = bto.getNum2Rooms() > 0 || bto.getNum3Rooms() > 0; // for married to see both
				break;
				case 2: can = bto.getNum2Rooms() > 0;
				break;
				case 3: can = bto.getNum3Rooms() > 0;
				break;
			}
		}
		return can;
	}
	
	// application functions
	public Application createApplication(BTO bto, int roomType) {
		if (this.canApplyBTO(bto, roomType)) {
			Application apply = new Application(this, bto.getId(), roomType);
			this.btoId = bto.getId();
			this.applicationId = apply.getId();
			System.out.println("Application submitted!");
			return apply;
		} else {
			System.out.println("You are not applicable to apply for this BTO project");
			return null;
		}
	}
	
	public void withdrawApplication(Application application) {
		if (application.getId() == this.applicationId 
				&& application.getApplicant().getId() == this.getId() 
				&& (application.getStatus() == "successful" || application.getStatus() == "booked")) {
			application.setStatus("withdraw", this.getRole());
			this.applicationId = -1;
			this.btoId = -1;
			System.out.println("Withdrawal submitted!");
		} else {
			System.out.println("Unable to withdraw");
		}
	}
	
	// enquiry function
	public Enquiries createEnquiries(String inpEnquiry, int btoId) {
		Enquiries enquiry = new Enquiries(inpEnquiry, btoId);
		this.enquiriesId.add(enquiry.getId());
		System.out.println("Enquiry submitted!");
		return enquiry;
	}
	
	public void editEnquiries(Enquiries enquiry, String inpEnquiry) {
		if (this.enquiriesId.contains(enquiry.getId()) && enquiry.getEnquirierId() == this.getId()) {
			enquiry.setEnquiry(inpEnquiry);
		}
	}
	
	public void deleteEnquiries(int enquiryId) {
		this.enquiriesId.remove(enquiryId); // need to remove at BTOApp
	}
	
//	public void viewEnquiries() {
//		
//	}

}
