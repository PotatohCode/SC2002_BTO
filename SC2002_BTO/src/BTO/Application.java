package BTO;

public class Application implements Search<Integer> {
	
	private int applicationId;
	private static int idCounter = 0;
	private Users applicant;
	private int btoId;
	private int roomType;
	private String status = "pending";
	private String type = "bto";
	
	// officer application
	public Application(Users applicant, int btoId, String status, String type) {
		this.applicant = applicant;
		this.btoId = btoId;
		this.status = status;
		this.type = type;
		this.applicationId = idCounter++;
	}
	
	// bto application
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
	
	public String getType() {
		return this.type;
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
		} else if (type == "bto" && role == "applicant" && (status.equals("withdraw") || status.equals("booking"))) {
			this.status = status;
		} else if (type == "officer" && role == "manager") {
			this.status = status;
		}
	}
	
	// functions
	public void printApplication() {
		System.out.println("Application Id: " + this.applicationId 
							+ "\nStatus: " + this.status
							+ (this.type.equals("bto") ? "\nRoom Type: "  + this.roomType : ""));
	}
	
	@Override
	public boolean getById(Integer id) {
		return this.applicationId == id;
	}
	
	@Override
	public boolean getByBTO(Integer id) {
		return this.btoId == id;
	}
	
	@Override
	public boolean getByUser(Integer id) {
		return this.applicant.getId() == id;
	}
}
