package BTO;

public class Application {
	private Applicant applicant; 
	private int roomType; 
	private String status; 
	
	public Application() {
		this.applicant = new Applicant();
	}
	
	public Applicant getApplicant() {
		return this.applicant; 
	}
	
	public int getRoomType() {
		return this.roomType;
	}
	
	public int setRoomType(int roomType) {
		if (roomType == 2 || roomType == 3) {
			this.roomType = roomType;
			return 0;
		}
		return -1; 
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status; 
	}
}
