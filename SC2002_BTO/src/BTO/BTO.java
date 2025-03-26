package BTO;
import java.util.HashMap;
import java.util.ArrayList;

public class BTO {
	private String name;
	private String neighbourhood;
	private int num2Rooms;
	private int num3Rooms;
	private int maxOfficer = 10;
	private int applicationStart; //DDMMYY
	private int applicationEnd; //DDMMYY
	private Manager manager;
	private boolean visible;
	private HashMap<Officer, String>  officerList; // key: officer, value: status of approval
	private Application[] applicationList;

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
	
	public int remaining2Room() {
		int count = this.num2Rooms;
		for (Application a : this.applicationList) {
			if (a.roomType == 2 && a.status == "approved") {
				count--;
			}
		}
		return count;
	}
	
	public int remaining3Room() {
		int count = this.num3Rooms;
		for (Application a : this.applicationList) {
			if (a.roomType == 3 && a.status == "approved") {
				count--;
			}
		}
		return count;
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
	
	public boolean authManager(Users u) {
		return this.manager.getNric() == u.getNric();
	}
	
	public boolean authOfficer(Users u) {
		for (Officer o : this.officerList.keySet()) {
			if (o.getNric() == u.getNric()) {
				return true;
			}
		}
		return false;
	}
	
	public int countOfficer() {
		int count = 0;
		for (String status : this.officerList.values()) {
			if (status == "approved") {
				count++;
			}
		}
		return count;
	}
	
	public void setVisible(boolean v) {
		this.visible = v;
	}
	
	public void listOfficers() {
		// need to add in sorting and filter feature
		for (Officer o : this.officerList.keySet()) {
			System.out.println("Officer: " + o.name + ", Status: " + this.officerList.get(o));
		}
	}
	
	public void printBTO() {
		System.out.println("Project Name: " + this.name + "\n" +
							"Managed By: " + this.manager.name + "\n" +
							"No. of Officers: " + this.countOfficer() + "\n" +
							"Visibility: " + (this.visible ? "Visible" : "Not Visible") + "\n" +
							"Application Start: " + this.applicationStart + "\t Application End: " + this.applicationEnd + "\n" +
							"No. of 2 Rooms: " + this.num2Rooms + "\t No. Left: " + this.remaining2Room() + "\n" +
							"No. of 3 Rooms: " + this.num3Rooms + "\t No. Left: " + this.remaining3Room());
	}
	
	public void updateOfficerStatus(Officer o, String status, Users u) {
		// need to add error handling for throwing failed authentication
		if (authManager(u)) {
			this.officerList.put(o, status);
		}
	}
	
	public void updateApplicationStatus(int index, String status, Users u) {
		// need to add error handling for throwing failed authentication
		if (authManager(u) || authOfficer(u)) {
			this.applicationList[index].status = status;
		}
	}

}
