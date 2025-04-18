package BTO;

public class Manager extends Users {
	
	private int managingId;

	public Manager(String nric, String name, String password) {
		super(nric, name, password, "manager");
	}

}
