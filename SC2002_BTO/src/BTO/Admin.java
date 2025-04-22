package BTO;

import java.util.List;
import java.util.Scanner;

public interface Admin {
	
	default boolean clashApplication(BTO bto, List<BTO> managingBTO) {
		for (BTO managing : managingBTO) {
			if ((bto.getApplicationStart().after(managing.getApplicationStart()) && bto.getApplicationStart().before(managing.getApplicationEnd())
					|| (bto.getApplicationEnd().after(managing.getApplicationStart()) && bto.getApplicationEnd().before(managing.getApplicationEnd())))) {
				return true;
			}
		}
		return false;
	}
	
	public void managingBTO(List<Integer> managingId, Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj) throws InvalidInput;
	
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
