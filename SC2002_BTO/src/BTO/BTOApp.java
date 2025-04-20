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
	//user csv header positions
	public static final int nameIdx = 0, nricIdx = 1, ageIdx = 2, marriedIdx = 3, passwordIdx = 4;
	//bto csv header positions
	public static final int projNameIdx = 0, neighborIdx = 1, t1Idx = 2, t1NumIdx = 3, t1PriceIdx = 4, t2Idx = 5, t2NumIdx = 6, t2PriceIdx = 7, appOpenIdx = 8, appCloseIdx = 9, managerIdx = 10, officerSlotsIdx = 11, officersIdx = 12;

	private static Scanner sc = new Scanner(System.in);
	
	// aesthetic
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_CYAN = "\u001B[36m";
	
	public static void csvParser(String filename, Project<Users> userList, String role) {
		File file = new File(filename);
		try (Scanner sc = new Scanner(file)) {
			int loopCount = 0;
			while (sc.hasNextLine()) {

				String line = sc.nextLine();
				String[] value = line.split(",");
				if (loopCount == 0) {
					loopCount++;
					continue;
				}
				boolean married;
				if (value[marriedIdx].equals("Single")) {
					married = false;
				} else {
					married = true;
				}
				Users newUser;
				if (role.toLowerCase().equals("applicant")) {
					newUser = new Applicant(value[nricIdx],value[nameIdx],Integer.parseInt(value[ageIdx]),value[passwordIdx],married);
					userList.addItem(newUser);
				} else if (role.toLowerCase().equals("officer")) {
					newUser = new Officer(value[nricIdx],value[nameIdx],Integer.parseInt(value[ageIdx]),value[passwordIdx],married);
					userList.addItem(newUser);
				} else if (role.toLowerCase().equals("manager")) {
					newUser = new Manager(value[nricIdx],value[nameIdx],Integer.parseInt(value[ageIdx]),value[passwordIdx],married);
					userList.addItem(newUser);
				} else {
					System.out.printf("Error: Tried to add unrecognised role %s\n", role);
					return;
				}
				
				
			}
			sc.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

	}
	
	public static void csvParser(String filename, Project<BTO> btoList, Project<Users> userList) {
	    File file = new File(filename);
	    try (Scanner sc = new Scanner(file)) {
	        int lineNum = 0;
	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            if (lineNum++ == 0) continue; // skip header
	            String[] value = line.split(",");
	            int num2Rooms, num3Rooms, price2Rooms, price3Rooms;
	            if (value[t1Idx].equals("2")) {
	            	num2Rooms = Integer.parseInt(value[t1NumIdx]);
	            	num3Rooms = Integer.parseInt(value[t2NumIdx]);
	            	price2Rooms = Integer.parseInt(value[t1PriceIdx]);
	            	price3Rooms = Integer.parseInt(value[t2PriceIdx]);
	            } else {
	            	num2Rooms = Integer.parseInt(value[t2NumIdx]);
	            	num3Rooms = Integer.parseInt(value[t1NumIdx]);
	            	price2Rooms = Integer.parseInt(value[t2PriceIdx]);
	            	price3Rooms = Integer.parseInt(value[t1PriceIdx]);
	            }
	            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
	            Date dateOpen = null;
	            Date dateClose = null;
	            Date now = new Date();

	            try {
	                dateOpen = formatter.parse(value[appOpenIdx]);
	                dateClose = formatter.parse(value[appCloseIdx]);
	            } catch (ParseException e) {
	                e.printStackTrace(); // or log it, or throw a runtime exception
	            }
	            Manager BtoManager = null;
	            for ( Users user : userList.getItems()) {
	            	if (user instanceof Manager && user.getRole().equals("manager") && user.getName().equals(value[managerIdx])) {
	            		BtoManager = (Manager) user;
	            	}
	            }
	            if (BtoManager == null) {
	            	System.out.printf("Error: Manager %s from BTO %s not found in user list.\n",value[managerIdx], value[projNameIdx]);
	            	return;
	            }
	            ArrayList<Officer> BtoOfficers = new ArrayList<>();
	            String[] nameArray = value[officersIdx].split(",");
	            for (String name : nameArray) {
	            	for (Users user : userList.getItems()) {
		            	if (user instanceof Officer && user.getRole().equals("officer") && user.getName().equals(name)) {
		            		BtoOfficers.add((Officer) user);
		            	}
	            	}
	            }
	            
	            BTO bto = new BTO(value[projNameIdx], value[neighborIdx], num2Rooms, num3Rooms, dateOpen, dateClose, BtoManager, Integer.parseInt(value[officerSlotsIdx]), now.compareTo(dateOpen) <= 0);
	            btoList.addItem(bto);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	// get BTO by id
	public static BTO getBTOById(int id, List<BTO> btoList) {
		return btoList.stream()
				.filter(bto -> bto.searchId(id))
				.findFirst().orElse(null);
	}
	
	// get applicant by nric
	public static Applicant getApplicantByNric(String nric, List<Users> userList) {
		return (Applicant) userList.stream()
				.filter(applicant -> applicant.searchNric(nric))
				.findFirst().orElse(null);
	}
	
	// get enquiries by id
	public static Enquiries getEnquiryById(int id, List<Enquiries> enquiryList) {
		return enquiryList.stream()
				.filter(enquiry -> enquiry.searchId(id))
				.findFirst().orElse(null);
	}
	
	// get enquiries by bto projects
	public static List<Enquiries> getEnquiryByBTO(int btoId, List<Enquiries> enquiryList) {
		return enquiryList.stream().filter(enquiry -> enquiry.searchOtherId(btoId)).toList();
	}
	
	// get enquiries by users
	public static List<Enquiries> getEnquiryByUser(int userId, List<Enquiries> enquiryList) {
		return enquiryList.stream().filter(enquiry -> enquiry.getEnquirierId() == userId).toList();
	}
	
	// print given btoList
	public static void printBTOs(List<BTO> btoList) {
		for (BTO b : btoList) {
			b.printBTO();
		}
	}
	
	// print given enquiryList with bto name
	public static void printEnquiries(List<Enquiries> enquiryList, List<BTO> btoList) {
		for (Enquiries e : enquiryList) {
			System.out.println("Project Name: " + getBTOById(e.getBTOId(), btoList).getName());
			e.printEnquiry();
		}
	}
	
	public static Users login(List<Users> userList) {
		System.out.print("Enter username: ");
		String inpName = sc.nextLine();
		System.out.print("Enter password: ");
		String inpPwd = sc.nextLine();
		Users user = userList.stream().filter(u -> u.getName().equals(inpName)).findFirst().orElse(null);
		if (user == null) {
			System.out.println("User does not exist");
			return login(userList);
		} else if (!user.checkPassword(inpPwd)){
			System.out.println("Incorrect Password!");
			return login(userList);
		}
		return user;
	}
	
	public static boolean clashApplication(Date start, Date end, List<Integer> btoIdList, List<BTO> btoList) {
		for (BTO bto : btoList) {
			if (btoIdList.contains(bto.getId())) {
				if (start.after(bto.getApplicationStart()) && end.before(bto.getApplicationEnd())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String capitalize(String s) {
		return s.substring(0,1).toUpperCase() + s.substring(1);
	}
	
	public static void applicantMenu(List<BTO> btoList, List<Enquiries> enquiryList, Applicant a) {
		boolean run = true;
		while (run) {
			try {
				System.out.println("\nWhat would you like to do?");
				System.out.println("1. View available BTOs");
//				System.out.println((a.getApplicationId() > -1 ? "\u001B[31m" : "") + "2. Apply for BTO" + "\u001B[0m");
				System.out.println((a.getApplicationId() > -1 ? "2. Access application" : "2. Apply for BTO"));
				System.out.println("3. Submit enquiry");
				System.out.println("4. Access enquiries");
			
				System.out.print(ANSI_CYAN + "Enter option: " + ANSI_RESET);
				int menuOption = sc.nextInt();
				sc.nextLine(); // capture extra \n
				System.out.println();
				
				switch (menuOption) {
					case 1: printBTOs(a.getApplicableBTOs(btoList));
						break;
						
					case 2: 
						if (a.getApplicationId() == -1) { // create application
							List<BTO> applyList = a.getApplicableBTOs(btoList);
							printBTOs(applyList);
							System.out.print(ANSI_CYAN + "Enter BTO Id: " + ANSI_RESET);
							int inpBTOId = sc.nextInt();
							sc.nextLine();
							
							BTO applyBTO = getBTOById(inpBTOId, btoList);
							if (applyBTO == null) throw new InvalidInput("BTO"); // invalid capture
							
							int inpRT = 2;
							if (a.getMarried()) {
								System.out.print(ANSI_CYAN + "Enter room type: " + ANSI_RESET);
								inpRT = sc.nextInt();
								sc.nextLine();
								if (inpRT != 2 || inpRT != 3) throw new InvalidInput("room type"); // invalid capture
							}
							
							Application application = a.createApplication(applyBTO, inpRT);
							if (application != null) applyBTO.addApplication(application);
						} else { // access application
							BTO appliedBTO = getBTOById(a.getBTOId(), btoList);
							appliedBTO.printBTO();
							Application application = appliedBTO.getApplicationById(a.getApplicationId());
							application.printApplication();
							
							if (application.getStatus() == "successful" || application.getStatus() == "booked") {
								System.out.println("1. Book flat with an Officer"); // TODO
								System.out.println("2. Withdraw application");
								System.out.print(ANSI_CYAN + "Enter option: " + ANSI_RESET);
								int applicationMenu = sc.nextInt();
								sc.nextLine();
								System.out.println();
								
								switch (applicationMenu) {
									case 2: a.withdrawApplication(application);
									break;
								}
							}
						}
						break;
						
					case 3:
						List<BTO> applyList = a.getApplicableBTOs(btoList);
						printBTOs(applyList);
						System.out.print(ANSI_CYAN + "Enter BTO Id: " + ANSI_RESET);
						int inpBTOId = sc.nextInt();
						sc.nextLine();
						BTO applyBTO = getBTOById(inpBTOId, btoList);
						if (applyBTO == null) throw new InvalidInput("BTO"); // invalid capture
						
						System.out.print(ANSI_CYAN + "Enter enquiry: " + ANSI_RESET);
						String inpEnq = sc.nextLine();
						if (inpEnq.length() <= 0) throw new InvalidInput("enquiry"); // invalid capture
						enquiryList.add(a.createEnquiries(inpEnq, inpBTOId));
						break;
						
					case 4:
						List<Enquiries> userEnquiry = getEnquiryByUser(a.getId(), enquiryList);
						if (userEnquiry.size() == 0) {
							System.out.println(ANSI_RED + "No exisiting enquiry" + ANSI_RESET);
							break;
						}
						printEnquiries(userEnquiry, btoList);
						System.out.print(ANSI_CYAN + "Enter enquiry id to access: " + ANSI_RESET);
						int enquiryId = sc.nextInt();
						sc.nextLine();
						Enquiries enquiry = getEnquiryById(enquiryId, enquiryList);
						if (enquiry == null) throw new InvalidInput("enquiry"); // invalid capture
						
						System.out.println("1. Edit\n2. Delete");
						System.out.print(ANSI_CYAN + "Enter option: " + ANSI_RESET);
						int enquiryMenu = sc.nextInt();
						sc.nextLine();
						switch(enquiryMenu) {
							case 1:
								System.out.print(ANSI_CYAN + "Enter enquiry: " + ANSI_RESET);
								String editEnq = sc.nextLine();
								if (editEnq.length() <= 0) throw new InvalidInput("enquiry"); // invalid capture
								a.editEnquiries(enquiry, editEnq);
								System.out.println("Enquiry updated!");
								break;
							case 2:
								a.deleteEnquiries(enquiryId);
								enquiryList.remove(enquiry);
								System.out.println("Enquiry deleted!");
								break;
						}
				}
			} catch (InputMismatchException ime) {
				System.out.println("Invalid input!");
			} catch (InvalidInput ii) {
				System.out.println(ANSI_RED + ii.getMessage() + ANSI_RESET);
			}
		}
	}
	
	public static void main(String[] args) {
		// initialisation
		Project<BTO> btoList = new Project<>();
		Project<Users> userList = new Project<>();
		Project<Enquiries> enquiryList = new Project<>();
		
		//input users
		csvParser(applicantPath, userList, "applicant");
		csvParser(officerPath, userList, "officer");
		csvParser(managerPath, userList, "manager");
		//input bto project
		csvParser(projectPath, btoList, userList);
		
		Users user = login(userList.getItems());
		System.out.println("\nWelcome " + capitalize(user.getRole()) + " " + user.getName() + "!");
		
		if (user.getRole().equals("applicant")) {
			Applicant applicant = (Applicant) user;
			applicantMenu(btoList.getItems(), enquiryList.getItems(), applicant);
		}
	}

}
