package BTO;

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
