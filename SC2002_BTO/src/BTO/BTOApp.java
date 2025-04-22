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
	
	public static void csvParser(String filename, Project<Users> userList, String role) {
		File file = new File(filename);
		try (Scanner sc = new Scanner(file)) {
			int loopCount = 0;
			while (sc.hasNextLine()) {

				String line = sc.nextLine();
				String[] value = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
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
	            String[] value = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
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
	            if (value.length >= 13) { // in case officers is blank in project.csv
		            String[] nameArray = value[officersIdx].replaceAll("\"", "").split(",");
		            for (String name : nameArray) {
		            	for (Users user : userList.getItems()) {
			            	if (user instanceof Officer && user.getRole().equals("officer") && user.getName().equals(name)) {
			            		BtoOfficers.add((Officer) user);
			            	}
		            	}
		            }
	            }
	            
	            BTO bto = new BTO(value[projNameIdx], value[neighborIdx], num2Rooms, num3Rooms, dateOpen, dateClose, BtoManager, Integer.parseInt(value[officerSlotsIdx]), now.compareTo(dateOpen) <= 0);
	            btoList.addItem(bto);
	            BtoManager.addManagingBTO(bto.getId());
	            for (Officer o : BtoOfficers) {
	            	o.addManagingBTO(bto.getId());
	            }
	            bto.setOfficers(BtoOfficers, BtoManager.getId());
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	public static Users login() {
		List<Users> userList = userProj.getItems();
		System.out.print(ANSI_YELLOW + "Enter NRIC: " + ANSI_RESET);
		String inpNric = sc.nextLine();
		System.out.print(ANSI_YELLOW + "Enter password: " + ANSI_RESET);
		String inpPwd = sc.nextLine();
		Users user = userList.stream().filter(u -> u.getNric().equals(inpNric)).findFirst().orElse(null);
		if (user == null) {
			System.out.println("User does not exist");
			return login();
		} else if (!user.checkPassword(inpPwd)){
			System.out.println("Incorrect Password!");
			return login();
		}
		return user;
	}
	
	public static String capitalize(String s) {
		return s.substring(0,1).toUpperCase() + s.substring(1);
	}
	
	public static void main(String[] args) {
		//input users
		csvParser(applicantPath, userProj, "applicant");
		csvParser(officerPath, userProj, "officer");
		csvParser(managerPath, userProj, "manager");
		//input bto project
		csvParser(projectPath, btoProj, userProj);
		
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
					applicant.showMenu(btoProj, appProj, enquiryProj);
				} else if (user.getRole().equals("officer")) {
					
					System.out.println("Which menu would you like to access?\n"
										+ "1. Applicant\n"
										+ "2. Officer");
					System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
					int menuSelect = sc.nextInt();
					sc.nextLine();
					
					if (menuSelect == 1) {
						Applicant officer = (Applicant) user;
						officer.showMenu(btoProj, appProj, enquiryProj);
					} else {
						Officer officer = (Officer) user;
						officer.showMenu(btoProj, appProj, enquiryProj, userProj.getItems());
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
