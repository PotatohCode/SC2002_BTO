package BTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BTOApp {
	public static final String applicantPath = "src/BTO/data/ApplicantList.csv";
	public static final String managerPath = "src/BTO/data/ManagerList.csv";
	public static final String officerPath = "src/BTO/data/OfficerList.csv";
	public static final int nameIdx = 0;
	public static final int nricIdx = 1;
	public static final int ageIdx = 2;
	public static final int marriedIdx = 3;
	public static final int passwordIdx = 4;
	public static void login() {
		
	}
	
	public static void csvParser(String filename, Project<Users> userList, String role) {
		File file = new File(filename);
		Scanner sc;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		int loopCount = 0;
		while (sc.hasNextLine()) {

			String line = sc.nextLine();
			String[] value = line.split(",");
			if (loopCount == 0) {
				loopCount++;
				continue;
			}
			boolean married;
			if (value[marriedIdx] == "Single") {
				married = false;
			} else {
				married = true;
			}
			Users newUser;
			if (role.toLowerCase() == "applicant") {
				newUser = new Applicant(value[nricIdx],value[nameIdx],value[passwordIdx],married);
				userList.addItem(newUser);
			} else if (role.toLowerCase() == "officer") {
				newUser = new Officer(value[nricIdx],value[nameIdx],value[passwordIdx],married);
				userList.addItem(newUser);
			} else if (role.toLowerCase() == "manager") {
				newUser = new Manager(value[nricIdx],value[nameIdx],value[passwordIdx]);
				userList.addItem(newUser);
			} else {
				System.out.printf("Error: Tried to add unrecognised role %s\n", role);
				return;
			}
			
			
		}
		sc.close();
	}
	
	public static void main(String[] args) {
		// initialisation
		Project<BTO> btoList = new Project<>();
		Project<Users> userList = new Project<>();
		Project<Enquiries> enquiryList = new Project<>();
		
		csvParser(applicantPath, userList, "applicant");
		csvParser(officerPath, userList, "officer");
		csvParser(managerPath, userList, "manager");
		for (Users user : userList.getItems()) {
			System.out.println(user.getRole());
		}
	}

}
