package BTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 *  @version 16/4/25
 *  
 *  Applicant.java: generic class for all users
 * 
 */
public class Applicant extends Users {
	
	private int applicationId;
	private List<Integer> enquiriesId = new ArrayList<>();
	protected int btoId = -1;

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
	
	// functions
//	public BTO getBTODetails() {
//		
//	}
//	
//	public Application getApplication() {
//		
//	}
	
	// viewing bto
	public void viewBTO(List<BTO> btoList) {
		List<BTO> viewList = new ArrayList<>();
		for (BTO bto: btoList) {
			if (bto.getVisible()) {
				if (!this.getMarried() && this.getAge() >= 35 && bto.getNum2Rooms() > 0) {
					// single
					if (bto.getNum2Rooms() > 0) {
						viewList.add(bto);
					}
				} else if (this.getMarried() && this.getAge() >= 21) {
					// married
					if (bto.getNum2Rooms() > 0 || bto.getNum3Rooms() > 0) {
						viewList.add(bto);
					}
				}
			}
			if (bto.getId() == this.btoId) {
				viewList.add(bto);
			}
		}
	}
	
	// check qualification
	public boolean canApplyBTO(BTO bto, int roomType) {
		boolean can = false;
		// check user qualification
		if (this.btoId == -1 && (this.getMarried() && this.getAge() >= 21) || (!this.getMarried() && this.getAge() >= 35 && roomType == 2)) {
			// check bto qualification
			switch (roomType) {
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
			return apply;
		} else {
			System.out.println("You are not applicable to apply for this BTO project");
			return null;
		}
	}
	
	public void withdrawApplication(Application application) {
		if (application.getId() == this.applicationId && application.getApplicant().getId() == this.getId() && application.getStatus() == "successful") {
			application.setStatus("withdraw", this.getRole());
		} else {
			System.out.println("Unable to withdraw");
		}
	}
	
	// enquiry function
	public Enquiries createEnquiries(String inpEnquiry, int btoId) {
		if (this.btoId == btoId) {
			Enquiries enquiry = new Enquiries(inpEnquiry);
			this.enquiriesId.add(enquiry.getId());
			return enquiry;
		} else {
			System.out.println("Something went wrong");
			return null;
		}
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
