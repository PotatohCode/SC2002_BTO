package BTO;

public class Application implements Searchable {
	
	private int applicationId;
	private static int idCounter = 0;
	private int applicantId;
	private int btoId;
	private int roomType;
	private String status = "pending";
	private String type = "bto";
	
	public Application(int applicantId, int btoId, String type) {
		this.applicantId = applicantId;
		this.btoId = btoId;
		this.applicationId = idCounter++;
		this.type = type;
	}
	
	public Application(int applicantId, int btoId, int roomType) {
		this.applicantId = applicantId;
		this.btoId = btoId;
		this.roomType = roomType;
		this.applicationId = idCounter++;
	}
	
	// getter
	public int getId() {
		return this.applicationId; 
	}
	
	public int getApplicantId() {
		return this.applicantId;
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
