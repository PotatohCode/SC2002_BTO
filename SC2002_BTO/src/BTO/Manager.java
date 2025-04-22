package BTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Manager extends Users implements Admin {
	
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
	
	// setter
	public void addManagingBTO(int btoId) {
		this.managingId.add(btoId);
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
//	public List<Application> viewOfficers(BTO bto, String filter) {
//		int count = 1;
//		List<Application> applicationList = bto.getOfficerApplications();
//		if (filter != null) {
//			applicationList = applicationList.stream().filter(a -> a.getStatus() == filter).toList();
//		} else {
//			applicationList.sort((s1, s2) -> s1.getStatus().compareToIgnoreCase(s2.getStatus())); // sort by status
//		}
//		for (Application application : applicationList) {
//			Officer officer = (Officer) application.getApplicant();
//			System.out.println(count++ + ". Name: " + officer.getName() + " Status: " + application.getStatus());
//		}
//		return applicationList; // for update status
//	}
	
	// update officer status
//	public void updateOfficerStatus(BTO bto) {
//		List<Application> applicationList = this.viewOfficers(bto, "pending");
//		System.out.print("Select officer to update: ");
//		int option = sc.nextInt() - 1;
//		System.out.print("Enter 1 to accept and 0 to reject");
//		int status = sc.nextInt();
//		switch (status) {
//			case 0: 
//				applicationList.get(option).setStatus("unsuccessful", this.getRole());
//				break;
//			case 1:
//				if (bto.getNumOfficer() < bto.getMaxOfficer()) {
//					applicationList.get(option).setStatus("successful", this.getRole());
//				} else {
//					System.out.println("There is no more slots for officers.");
//				}
//				break;
//		}
//	}
	
	// view all bto applications
//	public List<Application> viewApplications(BTO bto, String filter) {
//		int count = 1;
//		List<Application> applicationList = bto.getApplications();
//		if (filter != null) {
//			applicationList = applicationList.stream().filter(a -> a.getStatus() == filter).toList();
//		} else {
//			applicationList.sort((s1, s2) -> s1.getStatus().compareToIgnoreCase(s2.getStatus())); // sort by status
//		}
//		for (Application application : applicationList) {
//			Applicant applicant = (Applicant) application.getApplicant();
//			System.out.println(count++ + ". Name: " + applicant.getName() + " Room Type: " + application.getRoomType() + " Status: " + application.getStatus() + " Age: " + applicant.getAge() + " Maritial Status: " + (applicant.getMarried() ? "Married" : "Single"));
//		}
//		return applicationList; // for update status
//	}
	
	// update application status
//	public void updateApplicationStatus(BTO bto) {
//		System.out.println("2-Room flat left: " + bto.getNum2Rooms() + " 3-Room flat left: " + bto.getNum3Rooms());
//		List<Application> applicationList = this.viewApplications(bto, "pending");
//		System.out.print("Select application to update: ");
//		int option = sc.nextInt() - 1;
//		System.out.print("Enter 1 to accept and 0 to reject");
//		int status = sc.nextInt();
//		Application application = applicationList.get(option);
//		switch (status) {
//			case 0: 
//				application.setStatus("unsuccessful", this.getRole());
//				break;
//			case 1:
//				if ((application.getRoomType() == 2 && bto.getNum2Rooms() > 0) || (application.getRoomType() == 3 && bto.getNum3Rooms() > 0)) {
//					applicationList.get(option).setStatus("successful", this.getRole());
//				} else {
//					System.out.println("There is not enough rooms available.");
//				}
//				break;
//		}
//	}
	
	// update withdraw status
//	public void updateWithdrawStatus(BTO bto, List<Application> appList) {
//		List<Application> applicationList = this.viewApplications(bto, "withdraw");
//		System.out.print("Select application to update: ");
//		int option = sc.nextInt() - 1;
//		System.out.print("Enter 1 to accept and 0 to reject");
//		int status = sc.nextInt();
//		switch (status) {
//			case 0: 
//				applicationList.get(option).setStatus("successful", this.getRole());
//				break;
//			case 1:
////				appList.remove(application);
//				break;
//		}
//	}
	
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
	public BTO createBTO(Project<Users> userProj, List<BTO> managingBTO) throws ParseException, InputMismatchException, InvalidInput  {
			Date applicationStart, applicationEnd, now = new Date();
			System.out.print("Enter BTO name: ");
			String name = sc.nextLine();
			System.out.print("Enter BTO neighbourhood: ");
			String neighbourhood = sc.nextLine();
			System.out.print("Enter number of 2 rooms: ");
			int num2Rooms = sc.nextInt();
			sc.nextLine();
			System.out.print("Enter number of 3 rooms: ");
			int num3Rooms = sc.nextInt();
			sc.nextLine();
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
			
			System.out.print("Enter application start date (dd/MM/yyyy): ");
			String start = sc.nextLine();
			applicationStart = formatter.parse(start);
			
			System.out.print("Enter application end date (dd/MM/yyyy): ");
			String end = sc.nextLine();
			applicationEnd = formatter.parse(end);
				
				
	
			System.out.print("Enter manager NRIC (leave blank if you are manager): ");
			String managerNric = sc.nextLine();
			
			Users u = userProj.getApplicantByNric(managerNric);
			Manager manager = null;
			if (u.getRole() == "manager") manager = (Manager) u;
			if (manager == null) throw new InvalidInput("manager");
			
			System.out.print("Enter max officers: ");
			int maxOfficer = sc.nextInt();
			sc.nextLine();
			
			BTO newBTO = new BTO(name, neighbourhood, num2Rooms, num3Rooms, applicationStart, applicationEnd, this, maxOfficer, (now.compareTo(applicationStart) > 0 && now.compareTo(applicationStart) <= 0));
			return newBTO;
		
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
	
	public void showMenu(Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj, Project<Users> userProj) {
		boolean run = true;
		while (run) {
			try {
				System.out.println(ANSI_CYAN + "Menu:" + ANSI_RESET);
				System.out.println("1. View all BTO");
				System.out.println("2. Access your BTO");
				System.out.println("3. Create BTO");
				System.out.println("4. View all applicant bookings");
				System.out.println("5. Logout");
				
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int menuOption = sc.nextInt();
				sc.nextLine(); // capture extra \n
				System.out.println();

				switch(menuOption) {
					case 1:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> allBTOs = btoProj.getItems();
						if (allBTOs.size() <= 0) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						btoProj.printBTOs(allBTOs, true);
						break;
					case 2:
						managingBTO(this.getManaging(), btoProj, appProj, enquiryProj);
						break;
						
					case 3:
						List<BTO> managingBTO = btoProj.getItems().stream().filter(b -> managingId.contains(b.getId())).toList();
						BTO newBTO = createBTO(userProj, managingBTO);
						if (!clashApplication(newBTO, managingBTO)) {
							this.managingId.add(newBTO.getId());
							btoProj.addItem(newBTO);
						} else {
							System.out.println(ANSI_RED + "This project timeline clashes with another project" + ANSI_RESET);
						}
						break;
						
					case 4:
						List<Application> bookApp = new ArrayList<>();
						for (int btoId : this.getManaging()) {
							bookApp.addAll(appProj.getAppByBTO(btoId, "bto", "booked"));
						}
						for (Application app : bookApp) {
							System.out.println("Project Name: " + appProj.getBTOById(app.getBTOId()).getName());
							app.printApplication();
							Applicant applicant = (Applicant) app.getApplicant();
							applicant.printApplicant();
							System.out.println();
						}
						break;
						
					case 5:
						return;
					default: throw new InvalidInput("option");
				}
			} catch (ParseException pe) {
				System.out.println(ANSI_RED + "Date format is wrong" + ANSI_RESET);
			} catch (InputMismatchException ime) {
				System.out.println(ANSI_RED + "Invalid input!" + ANSI_RESET);
			} catch (InvalidInput ii) {
				System.out.println(ANSI_RED + ii.getMessage() + "\n" + ANSI_RESET);
			}
		}
	}

	@Override
	public void managingBTO(List<Integer> managingId, Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj) throws InvalidInput {
		List<BTO> managingBTO = btoProj.getItems().stream().filter(b -> managingId.contains(b.getId())).toList();
		System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
		if (managingBTO.size() <= 0) {
			System.out.println(ANSI_RED + "No managing BTO available\n" + ANSI_RESET);
			return;
		}
		btoProj.printBTOs(managingBTO);
		
		System.out.println("What would you like to do?\n"
							+ "1. Access enquries\n"
							+ "2. Access applicant applications\n"
							+ "3. Access officer applications"
							+ "4. Toggle visibility\n"
							+ "5. Edit BTO details\n"
							+ "6. Delete BTO\n"
							+ "7. Return to menu");
		
		System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
		int manageMenu = sc.nextInt();
		sc.nextLine();
		
		if (manageMenu == 7) return;
		if (manageMenu < 1 && manageMenu > 3) throw new InvalidInput("option");
		
		System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
		int inpBTOId = sc.nextInt();
		sc.nextLine();
		if (!managingId.contains(inpBTOId)) throw new InvalidInput("BTO");
		System.out.println();
		
		switch (manageMenu) {
			case 1:
				System.out.println(ANSI_CYAN + "===== Enquiries =====" + ANSI_RESET);
				List<Enquiries> btoEnquiries = enquiryProj.getEnquiryByBTO(inpBTOId);
				if (btoEnquiries.size() <= 0) {
					System.out.println(ANSI_RED + "No enquiries for this BTO\n" + ANSI_RESET);
					return;
				}
				enquiryProj.printEnquiries(btoEnquiries, btoProj.getItems());
				
				System.out.println("What would you like to do?\n"
							+ "1. Reply enquiry\n"
							+ "2. Return to menu");
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int manageMenu2 = sc.nextInt();
				sc.nextLine();
				if (manageMenu2 == 2) return;
				if (manageMenu2 < 1 && manageMenu > 2) throw new InvalidInput("option");
				replyEnquiry(this.getId(), this.getRole(), enquiryProj, sc);
				break;
				
			case 2:
				System.out.println(ANSI_CYAN + "===== Applicants =====" + ANSI_RESET);
				break;
		}
		
	}
	
}
