package BTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Officer extends Applicant {
	
	private List<Integer> managingId = new ArrayList<>();
	private List<Integer> applicationId = new ArrayList<>();
	private Scanner sc = new Scanner(System.in);

	public Officer (String nric, String name, int age, String password, boolean married) {
		super(nric, name, age, password, married, "officer");
	}
	
	// getter
	public List<Integer> getManaging() {
		return this.managingId;
	}
	
	public List<Integer> getApplication() {
		return this.applicationId;
	}
	
	// check bto qualification
	public boolean canApplyBTO(BTO bto, int roomType) {
		boolean can = false;
		// check user qualification
		if (this.btoId == -1 && managingId.contains(bto.getId()) && (this.getMarried() && this.getAge() >= 21) || (!this.getMarried() && this.getAge() >= 35 && roomType == 2)) {
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
	
	public boolean canApplyOfficer(int btoId, boolean clash) {
		return !clash && btoId != this.btoId && !managingId.contains(btoId);
	}
	
	// application functions
	public Application createOfficerApplication(BTO bto, boolean clash) {
		if (this.canApplyOfficer(bto.getId(), clash)) {
			Application apply = new Application(this, bto.getId(), "pending", "officer");
			this.btoId = bto.getId();
			this.applicationId.add(bto.getId());
			return apply;
		} else {
			System.out.println("You are not applicable to apply for this BTO project");
			return null;
		}
	}
	
	// view projects
	public void viewManagingProject(List<BTO> btoList) {
		List<BTO> availList = btoList.stream().filter(b -> this.clashApplication(b.getApplicationStart(), b.getApplicationEnd(), this.getManaging(), btoList)).toList();
		List<BTO> viewList = new ArrayList<>();
		for (BTO bto: availList) {
			if (bto.getVisible()) {
				viewList.add(bto);
			} else if (this.managingId.contains(bto.getId())) {
				viewList.add(bto);
			}
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
	public void bookRoom(BTO bto, Applicant applicant, Application application) {
		if (application.getStatus() == "successful") {
			application.setStatus("booked", "officer");
			switch (application.getRoomType()) {
				case 2: bto.reduceNum2Rooms(this.getId());
				break;
				case 3: bto.reduceNum3Rooms(this.getId());
				break;
			}
		}
	}
	
	// generate receipt
	public void applicantReceipt(BTO bto, Applicant applicant, Application application) {
		System.out.println("Applicant: " + applicant.getName() + "\n"
							+ "NRIC: " + applicant.getNric() + "\n"
							+ "Age: " + applicant.getAge() + "\n"
							+ "Martial Status: " + (applicant.getMarried() ? "Married" : "Single") + "\n"
							+ "Project: " + bto.getName() + "\n"
							+ "Neighbourhood: " + bto.getNeighbourhood() + "\n"
							+ "Flat Type: " + application.getRoomType());
	}

}
