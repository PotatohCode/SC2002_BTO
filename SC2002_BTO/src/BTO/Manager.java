package BTO;

public class Manager extends Users {
	
	private int btoId;

	public Manager(String nric, String name, String password) {
		super(nric, name, password, "manager");
	}

}
