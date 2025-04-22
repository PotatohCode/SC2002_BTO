package BTO;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class BTOApp {
	//constants
	public static final String applicantPath = "src/BTO/data/ApplicantList.csv";
	public static final String managerPath = "src/BTO/data/ManagerList.csv";
	public static final String officerPath = "src/BTO/data/OfficerList.csv";
	public static final String projectPath = "src/BTO/data/ProjectList.csv";

	// aesthetic
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	
	// scanner
	private static Scanner sc = new Scanner(System.in);
	
	// initialisation
	private static Project<BTO> btoProj = new Project<>();
	private static Project<Users> userProj = new Project<>();
	private static Project<Enquiries> enquiryProj = new Project<>();
	private static Project<Application> appProj = new Project<>();
	
	// get BTO by id
	public static BTO getBTOById(int id) {
		return btoProj.getItems().stream()
				.filter(b -> b.getId() == id)
				.findFirst().orElse(null);
	}
	
	// get applicant by nric
	public static Applicant getApplicantByNric(String nric) {
		return (Applicant) userProj.getItems().stream()
				.filter(a -> a.getNric().equals(nric))
				.findFirst().orElse(null);
	}
	
	// get enquiries by id
	public static Enquiries getEnquiryById(int id) {
		return enquiryProj.getItems().stream()
				.filter(e -> e.getId() == id)
				.findFirst().orElse(null);
	}
	
	// get enquiries by bto projects
	public static List<Enquiries> getEnquiryByBTO(int btoId) {
		return enquiryProj.getItems().stream()
				.filter(e -> e.getBTOId() == btoId).toList();
	}
	
	// get enquiries by users
	public static List<Enquiries> getEnquiryByUser(int userId) {
		return enquiryProj.getItems().stream()
				.filter(e -> e.getEnquirierId() == userId).toList();
	}
	
	// get application by id
	public static Application getAppById(int id) {
		return appProj.getItems().stream()
				.filter(a -> a.getId() == id)
				.findFirst().orElse(null);
	}
	
	// get application by bto projects
	public static List<Application> getAppByBTO(int btoId, String type, String status) {
		return appProj.getItems().stream()
				.filter(a -> a.getBTOId() == btoId && a.getType().equals(type) && (status == null || (a.getStatus() == status))).toList();
	}
	
	// get application by users
	public static List<Application> getAppByUser(int userId, String type) {
		return appProj.getItems().stream()
				.filter(a -> a.getApplicant().getId() == userId && a.getType().equals(type)).toList();
	}
	
	// print given btoList
	public static void printBTOs(List<BTO> btoList) {
		for (BTO b : btoList) {
			b.printBTO();
			System.out.println();
		}
	}
	
	// print given enquiryList with bto name
	public static void printEnquiries(List<Enquiries> enquiryList) {
		for (Enquiries e : enquiryList) {
			System.out.println("Project Name: " + getBTOById(e.getBTOId()).getName());
			e.printEnquiry();
			System.out.println();
		}
	}
	
	// print given application list with bto name
	public static void printBTOApp(List<Application> appList) {
		for (Application a : appList) {
			System.out.println("Project Name: " + getBTOById(a.getBTOId()).getName());
			a.printApplication();
			System.out.println();
		}
	}
	
	public static Users login() {
		List<Users> userList = userProj.getItems();
		System.out.print(ANSI_YELLOW + "Enter username: " + ANSI_RESET);
		String inpName = sc.nextLine();
		System.out.print(ANSI_YELLOW + "Enter password: " + ANSI_RESET);
		String inpPwd = sc.nextLine();
		Users user = userList.stream().filter(u -> u.getName().equals(inpName)).findFirst().orElse(null);
		if (user == null) {
			System.out.println("User does not exist");
			return login();
		} else if (!user.checkPassword(inpPwd)){
			System.out.println("Incorrect Password!");
			return login();
		}
		return user;
	}
	
	public static boolean clashApplication(List<Integer> btoIdList, int btoId) {
		BTO bto = getBTOById(btoId);
		for (int managingId : btoIdList) {
			BTO managingBTO = getBTOById(managingId);
			if ((bto.getApplicationStart().after(managingBTO.getApplicationStart()) && bto.getApplicationStart().before(managingBTO.getApplicationEnd())
					|| (bto.getApplicationEnd().after(managingBTO.getApplicationStart()) && bto.getApplicationEnd().before(managingBTO.getApplicationEnd())))) {
				return true;
			}
		}
		return false;
	}
	
	public static String capitalize(String s) {
		return s.substring(0,1).toUpperCase() + s.substring(1);
	}
	
	public static void applicantMenu(Applicant a) {
		boolean run = true;
		while (run) {
			try {
				System.out.println(ANSI_CYAN + "Menu:" + ANSI_RESET);
				System.out.println("1. View available BTOs");
				System.out.println((a.getApplicationId() > -1 ? "2. Access application" : "2. Apply for BTO"));
				System.out.println("3. Submit enquiry");
				System.out.println("4. Access enquiries");
				System.out.println("5. Logout");
			
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int menuOption = sc.nextInt();
				sc.nextLine(); // capture extra \n
				System.out.println();
				
				switch (menuOption) {
					case 1: 
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> availBTO = a.getApplicableBTOs(btoProj.getItems());
						if (availBTO.size() == 0) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						printBTOs(availBTO);
						break;
						
					case 2: 
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						if (a.getApplicationId() == -1) { // create application
							List<BTO> applyList = a.getApplicableBTOs(btoProj.getItems());
							if (applyList.size() == 0) {
								System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
								break;
							}
							printBTOs(applyList);
							
							System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
							int inpBTOId = sc.nextInt();
							sc.nextLine();
							BTO selectBTO = getBTOById(inpBTOId);
							if (selectBTO == null) throw new InvalidInput("BTO"); // invalid capture
							
							int inpRT = 2;
							if (a.getMarried()) {
								System.out.print(ANSI_YELLOW + "Enter room type: " + ANSI_RESET);
								inpRT = sc.nextInt();
								sc.nextLine();
								if (inpRT != 2 && inpRT != 3) throw new InvalidInput("room type"); // invalid capture
							}
							
							Application application = a.createApplication(selectBTO, inpRT);
							if (application != null) appProj.addItem(application);
						} else { // access application
							BTO appliedBTO = getBTOById(a.getBTOId());
							appliedBTO.printBTO();
							Application application = getAppById(a.getApplicationId());
							application.printApplication();
							System.out.println();
							if (application.getStatus().equals("successful") || application.getStatus().equals("booking")) {
								System.out.println(ANSI_CYAN + "BTO Menu:" + ANSI_RESET);
								System.out.println("1. Withdraw application");
								if (application.getStatus().equals("successful")) {
									System.out.println("2. Book flat with an officer");
									System.out.println("3. Return to menu");
								} else {
									System.out.println("2. Return to menu");
								}
								System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
								int applicationMenu = sc.nextInt();
								sc.nextLine();
								
								switch (applicationMenu) {
									case 1:
										a.withdrawApplication(application);
										break;
									case 2: 
										if (!application.getStatus().equals("successful")) break;
										a.bookBTO(application);
										break;
									case 3: if (!application.getStatus().equals("successful")) throw new InvalidInput("option");
										break;
									default: throw new InvalidInput("option");
								}
							}
						}
						break;
						
					case 3:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> btoList = a.getApplicableBTOs(btoProj.getItems());
						if (a.getBTOId() > -1) getBTOById(a.getBTOId()).printBTO();
						printBTOs(btoList);
						System.out.println();
						System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
						int inpBTOId = sc.nextInt();
						sc.nextLine();
						BTO applyBTO = getBTOById(inpBTOId);
						if (applyBTO == null) throw new InvalidInput("BTO"); // invalid capture
						
						System.out.print(ANSI_YELLOW + "Enter enquiry: " + ANSI_RESET);
						String inpEnq = sc.nextLine();
						if (inpEnq.length() <= 0) throw new InvalidInput("enquiry"); // invalid capture
						enquiryProj.addItem(a.createEnquiries(inpEnq, inpBTOId));
						break;
						
					case 4:
						System.out.println(ANSI_CYAN + "===== Enquiries =====" + ANSI_RESET);
						List<Enquiries> userEnquiry = getEnquiryByUser(a.getId());
						if (userEnquiry.size() == 0) {
							System.out.println(ANSI_RED + "No exisiting enquiry\n" + ANSI_RESET);
							break;
						}
						printEnquiries(userEnquiry);
						
						System.out.print(ANSI_YELLOW + "Enter enquiry id or -1 to return: " + ANSI_RESET);
						int enquiryId = sc.nextInt();
						sc.nextLine();
						if (enquiryId == -1) break; 
						Enquiries enquiry = getEnquiryById(enquiryId);
						if (enquiry == null) throw new InvalidInput("enquiry"); // invalid capture
						
						System.out.println(ANSI_CYAN + "Enquiry Menu:" + ANSI_RESET);
						System.out.println("1. Edit\n2. Delete\n3. Return to menu");
						System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
						int enquiryMenu = sc.nextInt();
						sc.nextLine();
						switch(enquiryMenu) {
							case 1:
								System.out.print(ANSI_YELLOW + "Enter enquiry: " + ANSI_RESET);
								String editEnq = sc.nextLine();
								if (editEnq.length() <= 0) throw new InvalidInput("enquiry"); // invalid capture
								a.editEnquiries(enquiry, editEnq);
								break;
							case 2:
								a.deleteEnquiries(enquiryId);
								enquiryProj.removeItem(enquiry);
								break;
							case 3:
								break;
							default: throw new InvalidInput("option");
						}
						break;
					case 5: return;
					default: throw new InvalidInput("option");
				}
			} catch (InputMismatchException ime) {
				System.out.println("Invalid input!");
			} catch (InvalidInput ii) {
				System.out.println(ANSI_RED + ii.getMessage() + "\n" + ANSI_RESET);
			}
		}
	}
	
	public static void officerMenu(Officer o) {
		boolean run = true;
		while (run) {
			try {			
				System.out.println(ANSI_CYAN + "Menu:" + ANSI_RESET);
				System.out.println("1. View BTOs availble for officer application");
				System.out.println("2. Register as officer");
				System.out.println("3. Access officer applications");
				System.out.println("4. Access officiating BTOs");
				System.out.println("5. Generate booking receipt");
				System.out.println("6. Logout");
			
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int menuOption = sc.nextInt();
				sc.nextLine(); // capture extra \n
				System.out.println();
				
				switch(menuOption) {
					case 1:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> availBTO = btoProj.getItems().stream()
												.filter(b -> o.canApplyOfficer(b, clashApplication(o.getManaging(), b.getId()))).toList();
						if (availBTO.size() <= 0) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						printBTOs(availBTO);
						break;
						
					case 2:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> applyBTO = btoProj.getItems().stream().filter(b -> o.canApplyOfficer(b, clashApplication(o.getManaging(), b.getId()))).toList();
						if (applyBTO.size() == 0) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						printBTOs(applyBTO);
						
						System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
						int inpBTOId = sc.nextInt();
						sc.nextLine();
						BTO selectBTO = getBTOById(inpBTOId);
						if (selectBTO == null) throw new InvalidInput("BTO"); // invalid capture
						
						Application application = o.createOfficerApplication(selectBTO, clashApplication(o.getManaging(), selectBTO.getId()));
						if (application != null) appProj.addItem(application);
						break;
					
					case 3:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<Application> officerAppList = getAppByUser(o.getId(), "officer");
						if (officerAppList.size() <= 0) {
							System.out.println(ANSI_RED + "No applications\n" + ANSI_RESET);
						}
						printBTOApp(officerAppList);
						break;
						
					case 4:
						managingBTO(o.getManaging(), o, null);
						break;
					case 5:
						System.out.print(ANSI_YELLOW + "Enter applicant nric: " + ANSI_RESET);
						String appNric = sc.nextLine();
						Applicant applicant = getApplicantByNric(appNric);
						if (applicant == null) throw new InvalidInput("applicant");
						System.out.println(ANSI_CYAN + "===== Applicant Booking Receipt =====" + ANSI_RESET);
						o.applicantReceipt(getBTOById(applicant.getBTOId()), applicant);
						break;
					case 6: return;
					default: throw new InvalidInput("option");
				}
			} catch (InputMismatchException ime) {
				System.out.println("Invalid input!");
			} catch (InvalidInput ii) {
				System.out.println(ANSI_RED + ii.getMessage() + "\n" + ANSI_RESET);
			}
		}
	}
	
	public static void managingBTO(List<Integer> managingId, Officer o, Manager m) throws InvalidInput {
		if (o == null && m == null) {
			System.out.println(ANSI_RED + "Error!\n" + ANSI_RESET);
			return;
		}
		System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
		List<BTO> managingBTO = btoProj.getItems().stream()
										.filter(b -> managingId.contains(b.getId())).toList();
		if (managingBTO.size() <= 0) {
			System.out.println(ANSI_RED + "No managing BTOs\n" + ANSI_RESET);
			return;
		}
		printBTOs(managingBTO);
		
		System.out.println("What would you like to do?\n"
						+ "1. Access enquries\n"
						+ "2. Accept booking requests\n"
						+ "3. Return to menu");
		System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
		int manageMenu = sc.nextInt();
		sc.nextLine();
		if (manageMenu == 3) return;
		if (manageMenu < 1 && manageMenu > 3) throw new InvalidInput("option");
		
		System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
		int inpBTOId = sc.nextInt();
		sc.nextLine();
		if (!managingId.contains(inpBTOId)) throw new InvalidInput("BTO");
		System.out.println();
		
		if (manageMenu == 1) {
			System.out.println(ANSI_CYAN + "===== Enquiries =====" + ANSI_RESET);
			List<Enquiries> btoEnquiries = getEnquiryByBTO(inpBTOId);
			if (btoEnquiries.size() <= 0) {
				System.out.println(ANSI_RED + "No enquiries for this BTO\n" + ANSI_RESET);
				return;
			}
			printEnquiries(btoEnquiries);
			
			System.out.println("What would you like to do?\n"
						+ "1. Reply enquiry\n"
						+ "2. Return to menu");
			System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
			int manageMenu2 = sc.nextInt();
			sc.nextLine();
			if (manageMenu2 == 2) return;
			if (manageMenu2 < 1 && manageMenu > 2) throw new InvalidInput("option");
			
			System.out.print(ANSI_YELLOW + "Enter enquiry id: " + ANSI_RESET);
			int inpEnqId = sc.nextInt();
			sc.nextLine();
			Enquiries enquiry = getEnquiryById(inpEnqId);
			enquiry.printEnquiry();
			if (enquiry.getReplierId() > -1) {
				System.out.println(ANSI_RED + "Enquiry already has a reply\n" + ANSI_RESET);
				return;
			}
			System.out.print(ANSI_YELLOW + "Enter reply: " + ANSI_RESET);
			String reply = sc.nextLine();
			if (reply.length() <= 0) throw new InvalidInput("reply");
			enquiry.setReply(reply, o.getId(), o.getRole());
		} else if(manageMenu == 2 && o.getRole().equals("officer")) {
			System.out.println(ANSI_CYAN + "===== Booking Applications =====" + ANSI_RESET);
			List<Application> bookingApps = getAppByBTO(inpBTOId, "bto", "booking");
			if (bookingApps.size() <= 0) {
				System.out.println(ANSI_RED + "No booking applications\n" + ANSI_RESET);
				return;
			}
			printBTOApp(bookingApps);
			
			System.out.print(ANSI_YELLOW + "Enter application id or -1 to return: " + ANSI_RESET);
			int inpAppId = sc.nextInt();
			sc.nextLine();
			if (inpAppId == -1) return;
			BTO bto = getBTOById(inpBTOId);
			Application bookingApp = getAppById(inpAppId);
			if (bookingApp == null) throw new InvalidInput("application");
			o.bookBTO(bto, bookingApp);
		}
	}
	
	public static void main(String[] args) {
		//input users
		CsvParser.parseUsersCsv(applicantPath, userProj, "applicant");
		CsvParser.parseUsersCsv(officerPath, userProj, "officer");
		CsvParser.parseUsersCsv(managerPath, userProj, "manager");
		CsvParser.parseProjectsCsv(projectPath, btoProj, userProj);

		boolean run = true;
		while (run) {
			System.out.println("1. Login\n2. Close");
			System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
			int start = sc.nextInt();
			sc.nextLine();
			if (start == 1) {
				Users user = login();
				System.out.println(ANSI_CYAN + "\nWelcome " + capitalize(user.getRole()) + " " + user.getName() + "!" + ANSI_RESET);
				
				if (user.getRole().equals("applicant")) {
					Applicant applicant = (Applicant) user;
					applicantMenu(applicant);
				} else if (user.getRole().equals("officer")) {
					Officer officer = (Officer) user;
					System.out.println("Which menu would you like to access?\n"
										+ "1. Applicant\n"
										+ "2. Officer");
					System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
					int menuSelect = sc.nextInt();
					sc.nextLine();
					
					if (menuSelect == 1) {
						applicantMenu(officer);
					} else {
						officerMenu(officer);
					}
				}
			} else {
				System.out.println(ANSI_RED + "End of program\n" + ANSI_RESET);
				sc.close();
				run = false;
			}
		}
	}

}
