package BTO;

import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Admin
 * Interface for administrative functionalities in the BTO system.
 * 
 * This interface provides default and abstract methods for managing
 * BTO projects, replying to enquiries, and handling officer interactions.
 * 
 * Implementing classes (e.g., Manager, Officer) gain access to reusable
 * logic such as overlap checks and enquiry replies.
 * 
 * Authorised roles: "officer", "manager".
 */
public interface Admin {
	/**
	 * Checks if the given BTO project overlaps in application period with any existing managed projects.
	 * 
	 * @param bto the new BTO project to check
	 * @param managingBTO list of BTO projects the user is currently managing
	 * @return true if the application dates clash, false otherwise
	 */
	default boolean clashApplication(BTO bto, List<BTO> managingBTO) {
		for (BTO managing : managingBTO) {
			if ((bto.getApplicationStart().after(managing.getApplicationStart()) && bto.getApplicationStart().before(managing.getApplicationEnd())
					|| (bto.getApplicationEnd().after(managing.getApplicationStart()) && bto.getApplicationEnd().before(managing.getApplicationEnd())))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Abstract method to access and manage BTO projects.
	 * 
	 * This method is implemented by roles such as Manager or Officer to view or manage projects.
	 * 
	 * @param managingId list of project IDs the user manages
	 * @param btoProj the project list containing all BTOs
	 * @param appProj the project list containing applications
	 * @param enquiryProj the project list containing enquiries
	 * @throws InputMismatchException if user input format is incorrect
	 * @throws InvalidInput if a required field is invalid
	 */
	public void managingBTO(List<Integer> managingId, Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj)  throws InputMismatchException, InvalidInput;
	
	/**
	 * Replies to a specific enquiry if it hasn't been answered yet.
	 * 
	 * Prompts user for reply input and updates the corresponding enquiry record.
	 * 
	 * @param enquiryId the ID of the enquiry to reply to
	 * @param role the user role performing the reply
	 * @param enquiryProj the list of all enquiries
	 * @param sc scanner for user input
	 * @throws InvalidInput if reply content is invalid or empty
	 */
	default void replyEnquiry(int enquiryId, String role, Project<Enquiries> enquiryProj, Scanner sc) throws InvalidInput {
		System.out.print(Users.ANSI_YELLOW + "Enter enquiry id: " + Users.ANSI_RESET);
		int inpEnqId = sc.nextInt();
		sc.nextLine();
		Enquiries enquiry = enquiryProj.getEnquiryById(inpEnqId);
		enquiry.printEnquiry();
		if (enquiry.getReplierId() > -1) {
			System.out.println(Users.ANSI_RED + "Enquiry already has a reply\n" + Users.ANSI_RESET);
			return;
		}
		System.out.print(Users.ANSI_YELLOW + "Enter reply: " + Users.ANSI_RESET);
		String reply = sc.nextLine();
		if (reply.length() <= 0) throw new InvalidInput("reply");
		enquiry.setReply(reply, enquiryId, role);
	}
}
