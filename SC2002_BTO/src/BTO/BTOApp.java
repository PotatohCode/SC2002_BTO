package BTO;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
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
	private static List session = new ArrayList<>();
	
	// get application by id
	public static Application getApplicationById(int id, List<Application> applicationList) {
		return applicationList.stream()
				.filter(application -> application.searchId(id))
				.findFirst().orElse(null);
	}
	
	// get BTO by id
	public static BTO getBTOById(int id, List<BTO> btoList) {
		return btoList.stream()
				.filter(bto -> bto.searchId(id))
				.findFirst().orElse(null);
	}
	
	// get enquiries by id
	public static Enquiries getEnquiryById(int id, List<Enquiries> enquiryList) {
		return enquiryList.stream()
				.filter(enquiry -> enquiry.searchId(id))
				.findFirst().orElse(null);
	}
	
	// get applicant by nric
	public static Applicant getApplicantByNric(String nric, List<Users> userList) {
		return (Applicant) userList.stream()
				.filter(applicant -> applicant.searchNric(nric))
				.findFirst().orElse(null);
	}
	
	// get enquiries by bto projects
	public static List<Enquiries> getEnquiryByBTO(List<Integer> btoList, List<Enquiries> enquiryList) {
		List <Enquiries> enquiries = new ArrayList<>();
		for (int btoId : btoList) {
			enquiries.addAll(enquiryList.stream().filter(enquiry -> enquiry.searchOtherId(btoId)).toList());
		}
		return enquiries;
	}
	
	public static Users login(List<Users> userList) {
		System.out.print("Enter username: ");
		String inpName = sc.nextLine();
		System.out.print("Enter password: ");
		String inpPwd = sc.nextLine();
		Users user = userList.stream().filter(u -> u.getName() == inpName).findFirst().orElse(null);
		if (user == null) {
			System.out.print("User does not exist");
			return login(userList);
		} else if (!user.checkPassword(inpPwd)){
			System.out.print("Incorrect Password!");
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
	            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	            Date dateOpen = null;
	            Date dateClose = null;

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
	            
	            BTO bto = new BTO(value[projNameIdx], value[neighborIdx], num2Rooms, num3Rooms, dateOpen, dateClose, BtoManager, Integer.parseInt(value[officerSlotsIdx]), true);
	            btoList.addItem(bto);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
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
		

	}

}
