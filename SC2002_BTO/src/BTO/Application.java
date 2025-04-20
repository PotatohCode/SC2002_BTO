package BTO;

public class Application implements Searchable {
	
	private int applicationId;
	private static int idCounter = 0;
	private Users applicant;
	private int btoId;
	private int roomType;
	private String status = "pending";
	private String type = "bto";
	
	public Application(Users applicant, int btoId, String status, String type) {
		this.applicant = applicant;
		this.btoId = btoId;
		this.status = status;
		this.type = type;
		this.applicationId = idCounter++;
	}
	
	public Application(Users applicant, int btoId, int roomType) {
		this.applicant = applicant;
		this.btoId = btoId;
		this.roomType = roomType;
		this.applicationId = idCounter++;
	}
	
	// getter
	public int getId() {
		return this.applicationId; 
	}
	
	public Users getApplicant() {
		return this.applicant;
	}
	
	public int getBTOId() {
		return this.btoId;
	}
	
	public int getRoomType() {
		return this.roomType;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	// setter
//	public void setRoomType(int roomType) {
//		if (roomType == 2 || roomType == 3) {
//			this.roomType = roomType;
//		}
//	}
	
	public void setStatus(String status, String role) {
		if (type == "bto" && (role == "manager" || role == "officer")) {
			this.status = status; 
		} else if (type == "bto" && role == "applicant" && status == "withdraw") {
			this.status = status;
		} else if (type == "officer" && role == "manager") {
			this.status = status;
		}
	}
	
	// functions
	public void printApplication() {
		System.out.println("Application Id: " + this.applicationId + " Status: " + this.status);
	}
	
	// search by id
	@Override
	public boolean searchId(int id) {
		return this.applicationId == id;
	}

	@Override
	public boolean searchOtherId(int id) {
		// TODO Auto-generated method stub
		return false;
	}
}
