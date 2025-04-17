package BTO;

public class Application {
	
	private int applicationId;
	private static int idCounter = 0;
	private int applicantId;
	private int btoId;
	private int roomType; 
	private String status = "pending";
	
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
	
	public void setStatus(String status) {
		this.status = status; 
	}
}
