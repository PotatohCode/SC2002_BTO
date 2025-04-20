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
	private int roomType = -1;
	
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
	
	public int getRoomType() {
		return this.roomType;
	}
	
	// setter
	public void setRoomType(int roomType, String nric, String role) {
		if (role.equals("officer") && nric.equals(this.nric)) {
			this.roomType = roomType;
		}
	}
	
	// functions
	// viewing bto
	public List<BTO> getApplicableBTOs(List<BTO> btoList) {
		return btoList.stream().filter(b -> canApplyBTO(b, (this.getMarried() ? -1 : 2))).toList();
	}
	
	// check qualification
	public boolean canApplyBTO(BTO bto, int roomType) {
		boolean can = false;
		// check user qualification
		if (bto.getVisible()
				&& (this.btoId != bto.getId() || this.btoId == -1)
				&& ((this.getMarried() && this.getAge() >= 21) 
				|| (!this.getMarried() && this.getAge() >= 35 && roomType == 2))) {
			// check bto qualification
			if (this.getRole().equals("officer")) {
				Officer officer = (Officer) this;
				if (officer.getManaging().contains(bto.getId()) || officer.getOfficerApp().contains(bto.getId())) return false;
			}
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
		if (canApplyBTO(bto, roomType)) {
			Application apply = new Application(this, bto.getId(), roomType);
			this.btoId = bto.getId();
			this.applicationId = apply.getId();
			System.out.println(ANSI_GREEN + "Application submitted!\n" + ANSI_RESET);
			return apply;
		} else {
			System.out.println(ANSI_RED + "You are not applicable to apply for this BTO project\n" + ANSI_RESET);
			return null;
		}
	}
	
	public void withdrawApplication(Application application) {
		if (application.getId() == this.applicationId 
				&& application.getApplicant().getId() == this.getId() 
				&& (application.getStatus() == "successful" || application.getStatus() == "booked")) {
			application.setStatus("withdraw", this.getRole());
			System.out.println(ANSI_GREEN + "Withdrawal submitted!\n" + ANSI_RESET);
		} else {
			System.out.println(ANSI_RED + "Unable to withdraw\n" + ANSI_RESET);
		}
	}
	
	public void bookBTO(Application application) {
		if (application.getId() == this.applicationId 
				&& application.getApplicant().getId() == this.getId() 
				&& application.getStatus() == "successful") {
			application.setStatus("booking", this.getRole());
			System.out.println(ANSI_GREEN + "Booking requested!\n" + ANSI_RESET);
		} else {
			System.out.println(ANSI_RED + "Unable to book\n" + ANSI_RESET);
		}
	}
	
	// enquiry function
	public Enquiries createEnquiries(String inpEnquiry, int btoId) {
		Enquiries enquiry = new Enquiries(inpEnquiry, this.getId(), btoId);
		this.enquiriesId.add(enquiry.getId());
		System.out.println(ANSI_GREEN + "Enquiry submitted!\n" + ANSI_RESET);
		return enquiry;
	}
	
	public void editEnquiries(Enquiries enquiry, String inpEnquiry) {
		if (this.enquiriesId.contains(enquiry.getId()) && enquiry.getEnquirierId() == this.getId()) {
			enquiry.setEnquiry(inpEnquiry);
			System.out.println(ANSI_GREEN + "Enquiry updated!\n" + ANSI_RESET);
		}
	}
	
	public void deleteEnquiries(int enquiryId) {
		this.enquiriesId.remove(enquiryId);
		System.out.println(ANSI_GREEN + "Enquiry deleted!\n" + ANSI_RESET);
	}

	public void printApplicant() {
		System.out.println("Applicant: " + this.getName() + "\n"
				+ "NRIC: " + this.getNric() + "\n"
				+ "Age: " + this.getAge() + "\n"
				+ "Martial Status: " + (this.getMarried() ? "Married" : "Single")
				+ (this.applicationId > -1 ? "\nApplication Id: " + this.applicationId : "")
				+ (this.roomType > -1 ? "\nRoom Type: " + this.roomType + "-Room" : ""));
	}
}