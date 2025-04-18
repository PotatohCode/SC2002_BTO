package BTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BTO implements Searchable {
	
	private int btoId;
	private static int idCounter = 0;
	private String name;
	private String neighbourhood;
	private int num2Rooms;
	private int num3Rooms;
	private int maxOfficer = 10;
	private Date applicationStart;
	private Date applicationEnd;
	private Manager manager;
	private boolean visible;
	private List<Application> applicationList = new ArrayList<>();
	private List<Integer> enquiriesList = new ArrayList<>();
	private List<Integer> officerList = new ArrayList<>();

	public BTO(String name, String neighbourhood, int num2Rooms, int num3Rooms, Date applicationStart, Date applicationEnd, Manager manager, int maxOfficer, boolean visible) {
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
	public int getId() {
		return this.btoId;
	}
	
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
	
	public Date getApplicationStart() {
		return this.applicationStart;
	}
	
	public Date getApplicationEnd() {
		return this.applicationEnd;
	}
	
	public Manager getManager() {
		return this.manager;
	}
	
	public boolean getVisible() {
		return this.visible;
	}
	
	// setter
	public void setName(String name, int id) {
		if (this.authManager(id)) {
			this.name = name;
		}
	}
	
	public void setNeighbourhood(String neighbourhood, int id) {
		if (this.authManager(id)) {
			this.neighbourhood = neighbourhood;
		}
	}
	
	public void setNum2Rooms(int num, int id) {
		if (this.authManager(id)) {
			this.num2Rooms = num;
		}
	}
	
	public void setNum3Rooms(int num, int id) {
		if (this.authManager(id)) {
			this.num3Rooms = num;
		}
	}
	
	public void setApplicationStart(Date date, int id) {
		if (this.authManager(id)) {
			this.applicationStart = date;
		}
	}
	
	public void setApplicationEnd(Date date, int id) {
		if (this.authManager(id)) {
			this.applicationEnd = date;
		}
	}
	
	public void setManager(Manager m, int id) {
		if (this.authManager(id)) {
			this.manager = m;
		}
	}
	
	public void setVisible(boolean visible, int id) {
		if (this.authManager(id)) {
			this.visible = visible;
		}
	}
	
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
	
	public void reduceNum2Rooms(int id) {
		if (this.authManager(id) || this.authOfficer(id)) {
			this.num2Rooms--;
		}
	}
	
	public void reduceNum3Rooms(int id) {
		if (this.authManager(id) || this.authOfficer(id)) {
			this.num3Rooms--;
		}
	}
	
	// search by id
	@Override
	public boolean searchId(int id) {
		return this.btoId == id;
	}

	@Override
	public boolean searchOtherId(int id) {
		// blank
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
