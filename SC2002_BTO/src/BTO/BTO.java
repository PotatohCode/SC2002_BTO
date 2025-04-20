package BTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BTO {
	
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
	private List<Officer> officerList = new ArrayList<>();

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
	
	public int getMaxOfficer() {
		return this.maxOfficer;
	}
	
	public int getNumOfficer() {
		return this.officerList.size();
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
	
	public void setMaxOfficer(int num, int id) {
		if (this.authManager(id) ) {
			this.maxOfficer = num;
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
	
	public void setOfficers(List<Officer> o, int id) {
		if (this.authManager(id)) {
			this.officerList.addAll(o);
			this.maxOfficer -= o.size(); // did not capture if csv file has more officer than max
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
		for (Officer officer : this.officerList) {
			if (id == officer.getId()) {
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
	
//	public void addApplication(Application application) {
//		this.applicationList.add(application);
//	}
//	
//	public void removeApplication(Application application) {
//		this.applicationList.remove(application);
//	}
//	
//	public void addOfficerApp(Application application) {
//		this.officerAppList.add(application);
//	}
	
	public String stringDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
	
	public void printBTO() {
		System.out.println("Project Id: " + this.btoId + "\n" + 
							"Project Name: " + this.name + "\n" +
							"Application Start: " + stringDate(this.applicationStart) + "\n" +
							"Application End: " + stringDate(this.applicationEnd) + "\n" +
							"No. of 2 Rooms: " + this.num2Rooms + "\n" +
							"No. of 3 Rooms: " + this.num3Rooms);
	}
	
//	public void replyEnquiries(int id, String reply) {
//		for (int enquiriesId : this.enquiriesList) { 
//			if (id == enquiriesId) {
//				
//			}
//		}
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
