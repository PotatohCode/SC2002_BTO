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
			
			System.out.print("Enter application start date (dd/MM/yyyy): ");
			String start = sc.nextLine();
			applicationStart = df.parse(start);
			
			System.out.print("Enter application end date (dd/MM/yyyy): ");
			String end = sc.nextLine();
			applicationEnd = df.parse(end);
			
			if (applicationEnd.compareTo(applicationStart) < 0) throw new InvalidInput("dates");
				
			System.out.print("Enter manager NRIC (leave blank if you are manager): ");
			String managerNric = sc.nextLine();
			if (managerNric.length() > 0) {
				Users u = userProj.getApplicantByNric(managerNric);
				Manager manager = null;
				if (u.getRole() == "manager") manager = (Manager) u;
				if (manager == null) throw new InvalidInput("manager");
			}
			
			System.out.print("Enter max officers: ");
			int maxOfficer = sc.nextInt();
			sc.nextLine();
			
			BTO newBTO = new BTO(name, neighbourhood, num2Rooms, num3Rooms, applicationStart, applicationEnd, this, maxOfficer, (now.compareTo(applicationStart) > 0 && now.compareTo(applicationStart) <= 0));
			return newBTO;
		
	}
	
	// edit BTO project
	public void editBTO(BTO bto) throws InputMismatchException, InvalidInput {
		if (managingId.contains(bto.getId()) && bto.getManager().getId() == this.getId()) {
			System.out.println("Which field would you like to edit?\n"
								+ "1. Name: " + bto.getName()
								+ "\n2. Neighbourhood: " + bto.getNeighbourhood()
								+ "\n3. No. of 2 Rooms: " + bto.getNum2Rooms()
								+ "\n4. No. of 3 Rooms: " + bto.getNum3Rooms()
								+ "\n5. Application Start Date: " + df.format(bto.getApplicationStart())
								+ "\n6. Application End Date: " + df.format(bto.getApplicationEnd())
								+ "\n7. No. of Officers: " + bto.getMaxOfficer()
								+ "\n8. Visibility: " + bto.getVisible()
								+ "\n9. Return to menu");
			System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
			int option = sc.nextInt();
		
			try {
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
						Date applicationStart = df.parse(startDate);
						if (bto.getApplicationEnd().compareTo(applicationStart) < 0) throw new InvalidInput("dates");
						bto.setApplicationStart(df.parse(startDate), this.getId());
						break;
					case 6:
						System.out.print("Enter new end date (dd/MM/yyyy): ");
						String endDate = sc.nextLine();
						Date applicationEnd = df.parse(endDate);
						if (applicationEnd.compareTo(bto.getApplicationStart()) < 0) throw new InvalidInput("dates");
						bto.setApplicationEnd(df.parse(endDate), this.getId());
						break;
					case 7:
						System.out.print("Enter new max no. of officers: ");
						int num = sc.nextInt();
						bto.setMaxOfficer(num, this.getId());
						break;
					case 8:
						bto.toggleVisible(this.getId());
						break;
					case 9:
						break;
					default: throw new InvalidInput("options");
				}
				System.out.println(ANSI_GREEN + "BTO edited!\n" + ANSI_RESET);
			} catch (ParseException pe) {
				System.out.println(ANSI_RED + "Date format is wrong\n" + ANSI_RESET);
			}
		}
	}
	
	// delete BTO
	public void deleteBTO(int btoId) {
		this.managingId.remove(btoId); // need to remove BTO for all existing application and user
	}
	
	public void showMenu(Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj, Project<Users> userProj) {
		boolean run = true;
		while (run) {
			try {
				System.out.println(ANSI_CYAN + "Menu:" + ANSI_RESET);
				System.out.println("1. View all BTOs");
				System.out.println("2. Access your BTOs");
				System.out.println("3. Create BTO");
				System.out.println("4. View all applicant bookings");
				System.out.println("5. View all enquiries");
				System.out.println("6. View officers");
				System.out.println("7. Logout");
				
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
						List<BTO> managingBTO = btoProj.getItems().stream().filter(b -> this.managingId.contains(b.getId())).toList();
						BTO newBTO = createBTO(userProj, managingBTO);
						if (!clashApplication(newBTO, managingBTO)) {
							this.managingId.add(newBTO.getId());
							btoProj.addItem(newBTO);
							System.out.println(ANSI_GREEN + "BTO created!\n" + ANSI_RESET);
						} else {
							System.out.println(ANSI_RED + "This project timeline clashes with another project\n" + ANSI_RESET);
						}
						break;
						
					case 4:
						List<Application> bookApp = new ArrayList<>();
						for (int btoId : this.getManaging()) {
							bookApp.addAll(appProj.getAppByBTO(btoId, "bto", "booked"));
						}
						if (bookApp.size() <= 0) {
							System.out.println(ANSI_RED + "No bookings\n" + ANSI_RESET);
							break;
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
						if (enquiryProj.getCount() > 0) {
							enquiryProj.printEnquiries(enquiryProj.getItems(), btoProj.getItems());
						} else {
							System.out.println(ANSI_RED + "No enquiries\n" + ANSI_RESET);
						}
						break;
					
					case 6:
						List<BTO> managingBTO2 = btoProj.getItems().stream().filter(b -> this.managingId.contains(b.getId())).toList();
						if (managingBTO2.size() <= 0) {
							System.out.println(ANSI_RED + "No BTOs\n" + ANSI_RESET);
						}
						for (BTO bto : managingBTO2) {
							if (bto.getOfficers().size() > 0) {
								System.out.print("BTO Name: " + bto.getName() + " Officers: ");
								for (Officer o : bto.getOfficers()) {
									System.out.print(o.getName() + " ");
								}
								System.out.println("\n");
							} else {
								System.out.println("BTO Name: " + bto.getName() + " Officers: No Officers");
							}
						}
						break;
					case 7:
						return;
					default: throw new InvalidInput("option");
				}
			} catch (ParseException pe) {
				System.out.println(ANSI_RED + "Date format is wrong!\n" + ANSI_RESET);
			} catch (InputMismatchException ime) {
				System.out.println(ANSI_RED + "Invalid input!\n" + ANSI_RESET);
			} catch (InvalidInput ii) {
				System.out.println(ANSI_RED + ii.getMessage() + "\n" + ANSI_RESET);
			}
		}
	}

	@Override
	public void managingBTO(List<Integer> managingId, Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj) throws InputMismatchException, InvalidInput {
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
							+ "3. Access officer applications\n"
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
		BTO bto = btoProj.getBTOById(inpBTOId);
		if (bto == null) throw new InvalidInput("BTO");
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
				int enqMenu = sc.nextInt();
				sc.nextLine();
				if (enqMenu == 2) return;
				if (enqMenu < 1 && enqMenu > 2) throw new InvalidInput("option");
				replyEnquiry(this.getId(), this.getRole(), enquiryProj, sc);
				break;
				
			case 2:
				System.out.println(ANSI_CYAN + "===== Applicant Applications =====" + ANSI_RESET);
				List<Application> appApp = appProj.getAppByBTO(inpBTOId, "bto", null);
				if (appApp.size() <= 0) {
					System.out.println(ANSI_RED + "No applications\n" + ANSI_RESET);
					return;
				}
				appProj.printBTOApp(appApp, btoProj.getItems());
				
				System.out.println("What would you like to do?\n"
						+ "1. Accept application\n"
						+ "2. Reject application\n"
						+ "3. Return to menu");
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int appMenu = sc.nextInt();
				sc.nextLine();
				if (appMenu == 3) return;
				if (appMenu < 1 && appMenu > 3) throw new InvalidInput("option");
				System.out.print("Enter application id: ");
				int selAppId = sc.nextInt();
				sc.nextLine();
				Application app = appProj.getAppById(selAppId);
				if (app == null || !appApp.contains(app)) throw new InvalidInput("application");
				
				updateAppStatus(app, appMenu, bto);
				break;
			
			case 3:
				System.out.println(ANSI_CYAN + "===== Officer Applications =====" + ANSI_RESET);
				List<Application> offApps = appProj.getAppByBTO(inpBTOId, "officer", null);
				if (offApps.size() <= 0) {
					System.out.println(ANSI_RED + "No applications\n" + ANSI_RESET);
					return;
				}
				appProj.printBTOApp(offApps, btoProj.getItems());
				
				System.out.println("What would you like to do?\n"
						+ "1. Accept application\n"
						+ "2. Reject application\n"
						+ "3. Return to menu");
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int offMenu = sc.nextInt();
				sc.nextLine();
				if (offMenu == 3) return;
				if (offMenu < 1 && offMenu > 3) throw new InvalidInput("option");
				System.out.print(ANSI_YELLOW + "Enter application id: " + ANSI_RESET);
				int selAppId2 = sc.nextInt();
				sc.nextLine();
				Application offApp = appProj.getAppById(selAppId2);
				if (offApp == null || !offApps.contains(offApp)) throw new InvalidInput("application");
				
				updateAppStatus(offApp, offMenu, bto);
				break;
			
			case 4:
				bto.toggleVisible(this.getId());
				System.out.println(ANSI_GREEN + "BTO is now " + (bto.getVisible() ? "Visible" : "Not Visible" + ANSI_RESET));
				break;
				
			case 5: editBTO(bto);
				break;
		}
		
	}
	
	public void updateAppStatus(Application app, int status, BTO bto) {
		if (app.getType().equals("officer")) {
			if (bto.getNumOfficer() >= bto.getMaxOfficer()) {
				System.out.println(ANSI_RED + "Insufficient slots available\n" + ANSI_RESET);
				return;
			}
			if (app.getApplicant() instanceof Officer) {
				Officer o = (Officer) app.getApplicant();
				bto.addOfficer(o);
			} else {
				System.out.println(ANSI_RED + "Something went wrong\n" + ANSI_RESET);
				return;
			}
		} else {
			if (app.getStatus().equals("booking") || app.getStatus().equals("booked")) {
				System.out.println(ANSI_RED + "Flat has already been booked\n" + ANSI_RESET);
				return;
			}
			if (app.getStatus().equals("withdrawing")) {
				app.setStatus(status == 1 ? "withdrawn" : "pending", this.getRole());
				Applicant applicant = (Applicant) app.getApplicant();
				applicant.withdraw();
				System.out.println(ANSI_GREEN + "Application updated!\n" + ANSI_RESET);
				return;
			}
			if (app.getRoomType() == 2 && bto.getNum2Rooms() <= 0) {
				System.out.println(ANSI_RED + "Insufficient rooms available\n" + ANSI_RESET);
				return;
			} else if (app.getRoomType() == 3 && bto.getNum3Rooms() <= 0) {
				System.out.println(ANSI_RED + "Insufficient rooms available\n" + ANSI_RESET);
				return;
			}
		}
		app.setStatus(status == 1 ? "successful" : "unsuccessful", this.getRole());
		System.out.println(ANSI_GREEN + "Application updated!\n" + ANSI_RESET);
	}
	
}
