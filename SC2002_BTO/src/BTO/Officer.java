package BTO;

public class Officer extends Applicant {
	
	private int btoId;
	private String status;

	public Officer (String nric, String name, String password, boolean married) {
		super(nric, name, password, married, "officer");
	}
	
	

}
