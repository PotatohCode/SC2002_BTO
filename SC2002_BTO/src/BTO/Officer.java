package BTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Officer extends Applicant {
	
	private List<Integer> managingId = new ArrayList<>();
	private List<Integer> applicationId = new ArrayList<>();

	public Officer (String nric, String name, int age, String password, boolean married) {
		super(nric, name, age, password, married, "officer");
	}
	
	// getter
	public List<Integer> getManaging() {
		return this.managingId;
	}
	
	public List<Integer> getApplication() {
		return this.applicationId;
	}
	
	// check bto qualification
	public boolean canApplyBTO(BTO bto, int roomType) {
		boolean can = false;
		// check user qualification
		if (this.btoId == -1 && managingId.contains(bto.getId()) && (this.getMarried() && this.getAge() >= 21) || (!this.getMarried() && this.getAge() >= 35 && roomType == 2)) {
			// check bto qualification
			switch (roomType) {
				case 2: can = bto.getNum2Rooms() > 0;
				break;
				case 3: can = bto.getNum3Rooms() > 0;
				break;
			}
		}
		return can;
	}
	
	public boolean canApplyOfficer(int btoId, boolean clash) {
		return !clash && btoId != this.btoId && managingId.contains(btoId);
	}
	
	// application functions
	public void createOfficerApplication(BTO bto, boolean clash) {
		if (this.canApplyOfficer(bto.getId(), clash)) {
			Application apply = new Application(this.getId(), bto.getId(), "officer");
			this.btoId = bto.getId();
			this.applicationId.add(bto.getId());
		} else {
			System.out.println("You are not applicable to apply for this BTO project");
		}
	}
	
	// book room
	public void bookRoom(BTO bto, Applicant applicant, Application application) {
		if (application.getStatus() == "successful") {
			application.setStatus("booked", "officer");
			switch (application.getRoomType()) {
				case 2: bto.reduceNum2Rooms(this.getId());
				break;
				case 3: bto.reduceNum3Rooms(this.getId());
				break;
			}
		}
	}
	
	// generate receipt
	public void applicantReceipt(BTO bto, Applicant applicant, Application application) {
		System.out.println("Applicant: " + applicant.getName() + "\n"
							+ "NRIC: " + applicant.getNric() + "\n"
							+ "Age: " + applicant.getAge() + "\n"
							+ "Martial Status: " + (applicant.getMarried() ? "Married" : "Single") + "\n"
							+ "Project: " + bto.getName() + "\n"
							+ "Neighbourhood: " + bto.getNeighbourhood() + "\n"
							+ "Flat Type: " + application.getRoomType());
	}

}
