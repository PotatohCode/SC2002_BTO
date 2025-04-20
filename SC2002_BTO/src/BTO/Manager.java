package BTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Manager extends Users {
	
	private List<Integer> managingId = new ArrayList<>();
	private Scanner sc;
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public Manager(String nric, String name, int age, String password, boolean married) {
		super(nric, name, age, password, married, "manager");
		sc = new Scanner(System.in);
	}
	
	// getter
	public List<Integer> getManaging() {
		return this.managingId;
	}
	
	// functions
	// view all BTO
	public void viewBTOs(List<BTO> btoList, boolean own) {
		int count = 1;
		for (BTO bto : btoList) {
			if (own) {
				if (bto.getManager().getId() == this.getId()) {
					System.out.println(count++ + ". " + bto.getName() + " at " + bto.getNeighbourhood() + " managed by " + bto.getManager().getName());
				}
			} else {
				System.out.println(count++ + ". " + bto.getName() + " at " + bto.getNeighbourhood() + " managed by " + bto.getManager().getName());
			}
		}
	}
	
	// view officer applications
	public List<Application> viewOfficers(BTO bto, String filter) {
		int count = 1;
		List<Application> applicationList = bto.getOfficerApplications();
		if (filter != null) {
			applicationList = applicationList.stream().filter(a -> a.getStatus() == filter).toList();
		} else {
			applicationList.sort((s1, s2) -> s1.getStatus().compareToIgnoreCase(s2.getStatus())); // sort by status
		}
		for (Application application : applicationList) {
			Officer officer = (Officer) application.getApplicant();
			System.out.println(count++ + ". Name: " + officer.getName() + " Status: " + application.getStatus());
		}
		return applicationList; // for update status
	}
	
	// update officer status
	public void updateOfficerStatus(BTO bto) {
		List<Application> applicationList = this.viewOfficers(bto, "pending");
		System.out.print("Select officer to update: ");
		int option = sc.nextInt() - 1;
		System.out.print("Enter 1 to accept and 0 to reject");
		int status = sc.nextInt();
		switch (status) {
			case 0: 
				applicationList.get(option).setStatus("unsuccessful", this.getRole());
				break;
			case 1:
				if (bto.getNumOfficer() < bto.getMaxOfficer()) {
					applicationList.get(option).setStatus("successful", this.getRole());
				} else {
					System.out.println("There is no more slots for officers.");
				}
				break;
		}
	}
	
	// view all bto applications
	public List<Application> viewApplications(BTO bto, String filter) {
		int count = 1;
		List<Application> applicationList = bto.getApplications();
		if (filter != null) {
			applicationList = applicationList.stream().filter(a -> a.getStatus() == filter).toList();
		} else {
			applicationList.sort((s1, s2) -> s1.getStatus().compareToIgnoreCase(s2.getStatus())); // sort by status
		}
		for (Application application : applicationList) {
			Applicant applicant = (Applicant) application.getApplicant();
			System.out.println(count++ + ". Name: " + applicant.getName() + " Room Type: " + application.getRoomType() + " Status: " + application.getStatus() + " Age: " + applicant.getAge() + " Maritial Status: " + (applicant.getMarried() ? "Married" : "Single"));
		}
		return applicationList; // for update status
	}
	
	// update application status
	public void updateApplicationStatus(BTO bto) {
		System.out.println("2-Room flat left: " + bto.getNum2Rooms() + " 3-Room flat left: " + bto.getNum3Rooms());
		List<Application> applicationList = this.viewApplications(bto, "pending");
		System.out.print("Select application to update: ");
		int option = sc.nextInt() - 1;
		System.out.print("Enter 1 to accept and 0 to reject");
		int status = sc.nextInt();
		Application application = applicationList.get(option);
		switch (status) {
			case 0: 
				application.setStatus("unsuccessful", this.getRole());
				break;
			case 1:
				if ((application.getRoomType() == 2 && bto.getNum2Rooms() > 0) || (application.getRoomType() == 3 && bto.getNum3Rooms() > 0)) {
					applicationList.get(option).setStatus("successful", this.getRole());
				} else {
					System.out.println("There is not enough rooms available.");
				}
				break;
		}
	}
	
	// update withdraw status
	public void updateWithdrawStatus(BTO bto) {
		List<Application> applicationList = this.viewApplications(bto, "withdraw");
		System.out.print("Select application to update: ");
		int option = sc.nextInt() - 1;
		System.out.print("Enter 1 to accept and 0 to reject");
		int status = sc.nextInt();
		switch (status) {
			case 0: 
				applicationList.get(option).setStatus("successful", this.getRole());
				break;
			case 1:
				bto.removeApplication(applicationList.get(option));
				break;
		}
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
	
	// create BTO project
	public BTO createBTO(boolean clash, String name, String neighbourhood, int num2Rooms, int num3Rooms, Date applicationStart, Date applicationEnd, Manager manager, int maxOfficer, boolean visible) {
		if (!clash) {
			Manager btoManager = manager == null ? this : manager; // if null : use current manager
			BTO newBTO = new BTO(name, neighbourhood, num2Rooms, num3Rooms, applicationStart, applicationEnd, btoManager, maxOfficer, visible);
			return newBTO;
		} else {
			System.out.println("This project timeline clashes with another project");
			return null;
		}
	}
	
	// edit BTO project
	public void editBTO(BTO bto, List<Users> userList) throws ParseException {
		if (managingId.contains(bto.getId()) && bto.getManager().getId() == this.getId()) {
			System.out.println("Which field would you like to edit?\n"
								+ "1. Name: " + bto.getName()
								+ "\n2. Neighbourhood: " + bto.getNeighbourhood()
								+ "\n3. No. of 2 Rooms: " + bto.getNum2Rooms()
								+ "\n4. No. of 3 Rooms: " + bto.getNum3Rooms()
								+ "\n5. Application Start Date: " + df.format(bto.getApplicationStart())
								+ "\n6. Application End Date: " + df.format(bto.getApplicationEnd())
								+ "\n7. Manager: " + bto.getManager().getName()
								+ "\n8. No. of Officers: " + bto.getMaxOfficer()
								+ "\n9. Visibility: " + bto.getVisible());
			int option = sc.nextInt();
		
			switch (option) {
				case 1: 
					System.out.print("Enter new name: ");
					String name = sc.nextLine();
					bto.setName(name, this.getId());
					break;
				case 2:
					System.out.print("Enter new neighbourhood: ");
					String neighbourhood = sc.nextLine();
					bto.setNeighbourhood(neighbourhood, this.getId());
					break;
				case 3:
					System.out.print("Enter new no of 2 rooms: ");
					int num2 = sc.nextInt();
					bto.setNum2Rooms(num2, this.getId());
					break;
				case 4:
					System.out.print("Enter new no of 3 rooms: ");
					int num3 = sc.nextInt();
					bto.setNum3Rooms(num3, this.getId());
					break;
				case 5:
					System.out.print("Enter new start date (dd/MM/yyyy): ");
					String startDate = sc.nextLine();
					bto.setApplicationStart(df.parse(startDate), this.getId());
					break;
				case 6:
					System.out.print("Enter new end date (dd/MM/yyyy): ");
					String endDate = sc.nextLine();
					bto.setApplicationEnd(df.parse(endDate), this.getId());
					break;
				case 7:
					System.out.print("Enter new manager nric: ");
					String nric = sc.nextLine();
					Manager manager = (Manager) userList.stream().filter(user -> user.searchNric(nric)).findFirst().orElse(this);
					bto.setManager(manager, this.getId());
					break;
				case 8:
					System.out.print("Enter new max no. of officers: ");
					int num = sc.nextInt();
					bto.setMaxOfficer(num, this.getId());
					break;
				case 9:
					System.out.print("Visibility has been toggled");
					bto.setVisible(!bto.getVisible(), this.getId());
					break;
			}
		}
	}
	
	// delete BTO
	public void deleteBTO(int btoId) {
		this.managingId.remove(btoId); // need to remove at BTOApp
	}
	
}
