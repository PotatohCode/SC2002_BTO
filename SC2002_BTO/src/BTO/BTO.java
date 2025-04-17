package BTO;

public class BTO {
	
	private int btoId;
	private static int idCounter = 0;
	private String name;
	private String neighbourhood;
	private int num2Rooms;
	private int num3Rooms;
	private int maxOfficer = 10;
	private int applicationStart; //DDMMYY
	private int applicationEnd; //DDMMYY
	private Manager manager;
	private boolean visible;
	private int[] applicationList;
	private int[] enquiriesList;
	private int[] officerList;

	public BTO(String name, String neighbourhood, int num2Rooms, int num3Rooms, int applicationStart, int applicationEnd, Manager manager, int maxOfficer, boolean visible) {
		this.name = name;
		this.neighbourhood = neighbourhood;
		this.num2Rooms = num2Rooms;
		this.num3Rooms = num3Rooms;
		this.applicationStart = applicationStart;
		this.applicationEnd = applicationEnd;
		this.manager = manager;
		this.maxOfficer = maxOfficer;
		this.visible = visible;
		this.btoId = idCounter++;
	}
	
	// getter
	public String getName() {
		return this.name;
	}
	
	public String getNeighbourhood() {
		return this.neighbourhood;
	}
	
	public int getNum2Rooms() {
		return this.num2Rooms;
	}
	
	public int getNum3Rooms() {
		return this.num3Rooms;
	}
	
	public int getApplicationStart() {
		return this.applicationStart;
	}
	
	public int getApplicationEnd() {
		return this.applicationEnd;
	}
	
	public Manager getManager() {
		return this.manager;
	}
	
	public boolean getVisible() {
		return this.visible;
	}
	
	// setter
	
	//functions
	public boolean authManager(int id) {
		return this.manager.getId() == id;
	}
	
	public boolean authOfficer(int id) {
		for (int officerId : this.officerList) {
			if (id == officerId) {
				return true;
			}
		}
		return false;
	}
	
//	public void replyEnquiries(int id, String reply) {
//		for (int enquiriesId : this.enquiriesList) { 
//			if (id == enquiriesId) {
//				
//			}
//		}
//	}
	
//	public void printBTO() {
//		System.out.println("Project Name: " + this.name + "\n" +
//							"Managed By: " + this.manager.name + "\n" +
//							"No. of Officers: " + this.countOfficer() + "\n" +
//							"Visibility: " + (this.visible ? "Visible" : "Not Visible") + "\n" +
//							"Application Start: " + this.applicationStart + "\t Application End: " + this.applicationEnd + "\n" +
//							"No. of 2 Rooms: " + this.num2Rooms + "\t No. Left: " + this.remaining2Room() + "\n" +
//							"No. of 3 Rooms: " + this.num3Rooms + "\t No. Left: " + this.remaining3Room());
//	}
	
//	public void updateOfficerStatus(Officer o, String status, Users u) {
//		// need to add error handling for throwing failed authentication
//		if (authManager(u)) {
//			this.officerList.put(o, status);
//		}
//	}
//	
//	public void updateApplicationStatus(int index, String status, Users u) {
//		// need to add error handling for throwing failed authentication
//		if (authManager(u) || authOfficer(u)) {
//			this.applicationList[index].status = status;
//		}
//	}

}
