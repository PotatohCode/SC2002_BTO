package BTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class BTOApp {
	
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
	
	public static void main(String[] args) {
		// initialisation
		Project<BTO> btoList = new Project<>();
		Project<Users> userList = new Project<>();
		Project<Enquiries> enquiryList = new Project<>();
	}

}
