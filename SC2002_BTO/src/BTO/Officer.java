package BTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Officer extends Applicant {
	
	private List<Integer> managingId = new ArrayList<>();
	private List<Integer> officerAppId = new ArrayList<>();
	private Scanner sc = new Scanner(System.in);

	public Officer (String nric, String name, int age, String password, boolean married) {
		super(nric, name, age, password, married, "officer");
	}
	
	// getter
	public List<Integer> getManaging() {
		return this.managingId;
	}
	
	public List<Integer> getOfficerApp() {
		return this.officerAppId;
	}
	
	// setter
	public void addManagingBTO(int btoId) {
		this.managingId.add(btoId);
	}
	
	// functions
	// check criteria to apply officer
	public boolean canApplyOfficer(BTO bto, boolean clash) {
		return !clash && bto.getId() != this.btoId && !this.managingId.contains(bto.getId()) && !this.officerAppId.contains(bto.getId()) && bto.getMaxOfficer() > 0;
	}
	
	// application functions
	public Application createOfficerApplication(BTO bto, boolean clash) {
		if (this.canApplyOfficer(bto, clash)) {
			Application apply = new Application(this, bto.getId(), "pending", "officer");
			this.btoId = bto.getId();
			this.officerAppId.add(bto.getId());
			System.out.println(ANSI_GREEN + "Application submitted!\n"+ ANSI_RESET);
			return apply;
		} else {
			System.out.println(ANSI_RED + "You are not applicable to apply for this BTO project\n"+ ANSI_RESET);
			return null;
		}
	}
	
	public boolean clashApplication(Date start, Date end, List<Integer> btoIdList, List<BTO> btoList) {
		for (BTO bto : btoList) {
			if (btoIdList.contains(bto.getId())) {
				if (start.after(bto.getApplicationStart()) && end.before(bto.getApplicationEnd())) {
					return true;
				}
			}
		}
		return false;
	}
	
	// view all enquiries
//	public List<Enquiries> viewEnquiries(BTO bto, String filter) {
//		int count = 1;
//		List<Enquiries> enquiryList = bto.getEnquiries();
//		if (filter == "unread") {
//			enquiryList = enquiryList.stream().filter(e -> e.getReply() == null).toList();
//		} else {
//			enquiryList.sort((s1, s2) -> s1.getReply().compareToIgnoreCase(s2.getReply())); // sort by reply
//		}
//		for (Enquiries enquiry : enquiryList) {
//			System.out.println(count++ + ". Enquiry: " + enquiry.getEnquiry() + enquiry.getReply() != null ? ("\nReply: " + enquiry.getReply()) : "");
//		}
//		return enquiryList; // for update status
//	}
	
	// reply enquiry
//	public void replyEnquiries(BTO bto) {
//		List<Enquiries> enquiryList = this.viewEnquiries(bto, "unread");
//		System.out.print("Select enquiry to reply: ");
//		int option = sc.nextInt() - 1;
//		System.out.print("Enter reply: ");
//		String reply = sc.nextLine();
//		enquiryList.get(option).setReply(reply, this.getId(), this.getRole());
//	}
	
	// book room
	public String bookBTO(BTO bto, Application application) {
		if (application.getStatus() == "booking" && this.managingId.contains(bto.getId())) {
			switch (application.getRoomType()) {
				case 2: 
					if (bto.getNum2Rooms() > 0) {
						bto.reduceNum2Rooms(this.getId());
					} else {
						System.out.println("Unable to book");
						return null;
					}
				break;
				case 3: 
					if (bto.getNum3Rooms() > 0) {
						bto.reduceNum3Rooms(this.getId());
					} else {
						System.out.println("Unable to book");
						return null;
					}
				break;
			}
			Applicant applicant = (Applicant) application.getApplicant();
			System.out.println(ANSI_GREEN + "Room numbers updated!"+ ANSI_RESET);
			application.setStatus("booked", "officer");
			System.out.println(ANSI_GREEN + "Application status updated!"+ ANSI_RESET);
			applicant.setRoomType(application.getRoomType(), applicant.getNric(), this.getRole());
			System.out.println(ANSI_GREEN + "Applicant profile updated!"+ ANSI_RESET);
			System.out.println(ANSI_GREEN + "Booked successfully!"+ ANSI_RESET);
			System.out.println(ANSI_GREEN + "Applicant NRIC: " + ANSI_RESET + applicant.getNric());
			System.out.println();
			return applicant.getNric();
		} else {
			System.out.println(ANSI_RED + "Unable to book"+ ANSI_RESET);
			return null;
		}
	}
	
	// generate receipt
	public void applicantReceipt(BTO bto, Applicant applicant) {
		applicant.printApplicant();
		bto.printBTO();
		System.out.println();
	}

}
