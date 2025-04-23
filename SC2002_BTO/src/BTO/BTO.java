package BTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a Build-To-Order (BTO) housing project within the BTO system.
 * 
 * Each BTO project contains information such as the project name, location,
 * available room types (2-room and 3-room), application period, assigned manager,
 * and a list of appointed officers. The project also supports access control,
 * allowing only authorised managers and officers to perform updates.
 * 
 * BTOs are used as the main entities that applicants can apply to for housing,
 * and officers can apply to manage.
 * 
 * Each project is assigned a unique auto-incremented ID.
 * 
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 */
public class BTO implements Search<Integer> {
	
	private int btoId;
	private static int idCounter = 0;
	private String name;
	private String neighbourhood;
	private double price;
	private int num2Rooms;
	private int num3Rooms;
	private int maxOfficer = 10;
	private Date applicationStart;
	private Date applicationEnd;
	private Manager manager;
	private boolean visible;
	private List<Officer> officerList = new ArrayList<>();
	
	/**
	 * Constructs a new BTO (Build-To-Order) project with specified details.
	 *
	 * @param name             the name of the BTO project
	 * @param neighbourhood    the neighbourhood where the project is located
	 * @param num2Rooms        number of 2-room units available
	 * @param num3Rooms        number of 3-room units available
	 * @param applicationStart the start date of the application period
	 * @param applicationEnd   the end date of the application period
	 * @param manager          the manager responsible for the project
	 * @param maxOfficer       the maximum number of officers allowed
	 * @param visible          whether the project is currently visible to users
	 */
	public BTO(String name, String neighbourhood, double price, int num2Rooms, int num3Rooms, Date applicationStart, Date applicationEnd, Manager manager, int maxOfficer, boolean visible) {
		this.name = name;
		this.neighbourhood = neighbourhood;
		this.price = price;
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
	/**
	 * Returns the unique ID of this BTO project.
	 *
	 * @return the BTO ID
	 */
	public int getId() {
		return this.btoId;
	}
	
	/**
	 * Returns the name of the project.
	 *
	 * @return project name
	 */
	public String getName() {
		return this.name;
	}
	
	
	/**
	 * Returns the neighbourhood where the project is located.
	 *
	 * @return neighbourhood name
	 */
	public String getNeighbourhood() {
		return this.neighbourhood;
	}
	
	/**
	 * Returns the price in 2dp
	 * @return price
	 */
	public String getPrice() {
		return String.format( "%.2f", this.price);
	}
	
	/**
	 * Returns the number of available 2-room units.
	 *
	 * @return number of 2-room flats
	 */
	public int getNum2Rooms() {
		return this.num2Rooms;
	}
	
	/**
	 * Returns the number of available 3-room units.
	 *
	 * @return number of 3-room flats
	 */
	public int getNum3Rooms() {
		return this.num3Rooms;
	}
	
	/**
	 * Returns the maximum number of officers allowed for this project.
	 *
	 * @return maximum officer count
	 */
	public int getMaxOfficer() {
		return this.maxOfficer;
	}
	
	/**
	 * Returns the number of officers currently assigned to this project.
	 *
	 * @return current officer count
	 */
	public int getNumOfficer() {
		return this.officerList.size();
	}
	
	/**
	 * Returns the application start date.
	 *
	 * @return start date of application period
	 */
	public Date getApplicationStart() {
		return this.applicationStart;
	}
	
	/**
	 * Returns the application end date.
	 *
	 * @return end date of application period
	 */
	public Date getApplicationEnd() {
		return this.applicationEnd;
	}
	
	/**
	 * Returns the manager assigned to this BTO project.
	 *
	 * @return {@link Manager} object
	 */
	public Manager getManager() {
		return this.manager;
	}
	
	/**
	 * Returns whether this BTO project is visible to users.
	 *
	 * @return true if visible, false otherwise
	 */
	public boolean getVisible() {
		return this.visible;
	}
	
	/**
	 * Returns the list of officers assigned to this project.
	 *
	 * @return list of {@link Officer} objects
	 */
	public List<Officer> getOfficers() {
		return this.officerList;
	}
	
	// ==== Setters with Manager Authorization ====
	
	/**
	 * Updates the name of the BTO project if called by the manager.
	 *
	 * @param name the new project name
	 * @param id the ID of the user attempting the update
	 */
	public void setName(String name, int id) {
		if (this.authManager(id)) {
			this.name = name;
		}
	}
	
	/**
	 * Updates the neighbourhood name if called by the manager.
	 *
	 * @param neighbourhood the new neighbourhood name
	 * @param id the ID of the user attempting the update
	 */
	public void setNeighbourhood(String neighbourhood, int id) {
		if (this.authManager(id)) {
			this.neighbourhood = neighbourhood;
		}
	}
	
	/**
	 * Updates the price if called by the manager.
	 * @param price
	 * @param id
	 */
	public void setPrice(double price, int id) {
		if (this.authManager(id)) {
			this.price = price;
		}
	}
	
	/**
	 * Sets the number of 2-room flats available.
	 *
	 * @param num the new number of 2-room flats
	 * @param id the ID of the manager
	 */
	public void setNum2Rooms(int num, int id) {
		if (this.authManager(id)) {
			this.num2Rooms = num;
		}
	}
	
	/**
	 * Sets the number of 3-room flats available.
	 *
	 * @param num the new number of 3-room flats
	 * @param id the ID of the manager
	 */
	public void setNum3Rooms(int num, int id) {
		if (this.authManager(id)) {
			this.num3Rooms = num;
		}
	}
	
	
	/**
	 * Sets the maximum number of officers for this project.
	 *
	 * @param num the new max number of officers
	 * @param id the ID of the manager
	 */
	public void setMaxOfficer(int num, int id) {
		if (this.authManager(id) ) {
			this.maxOfficer = num;
		}
	}
	
	/**
	 * Sets the start date of the application period.
	 *
	 * @param date the new start date
	 * @param id the ID of the manager
	 */
	public void setApplicationStart(Date date, int id) {
		if (this.authManager(id)) {
			this.applicationStart = date;
		}
	}
	
	/**
	 * Sets the end date of the application period.
	 *
	 * @param date the new end date
	 * @param id the ID of the manager
	 */
	public void setApplicationEnd(Date date, int id) {
		if (this.authManager(id)) {
			this.applicationEnd = date;
		}
	}
	
	/**
	 * Changes the manager of this BTO project.
	 *
	 * @param m  the new manager
	 * @param id the ID of the current manager
	 */
	public void setManager(Manager m, int id) {
		if (this.authManager(id)) {
			this.manager = m;
		}
	}
	

	/**
	 * Assigns officers to the project if called by the manager.
	 * Reduces available officer slots accordingly.
	 *
	 * @param o  list of officers to assign
	 * @param id the manager's ID
	 */
	public void setOfficers(List<Officer> o, int id) {
		if (this.authManager(id)) {
			this.officerList.addAll(o);
			this.maxOfficer -= o.size(); // did not capture if csv file has more officer than max
		}
	}
	

	/**
	 * Sets the visibility of the project.
	 *
	 * @param visible true to make visible, false to hide
	 * @param id the ID of the manager
	 */
	public void setVisible(boolean visible, int id) {
		if (this.authManager(id)) {
			this.visible = visible;
		}
	}
	
	// ==== Role Check Functions ====

	/**
	 * Checks whether the user is the assigned manager.
	 *
	 * @param id the user ID
	 * @return true if authorised manager
	 */
	public boolean authManager(int id) {
		return this.manager.getId() == id;
	}
	
	/**
	 * Checks whether the user is one of the assigned officers.
	 *
	 * @param id the user ID
	 * @return true if the user is an authorised officer
	 */
	public boolean authOfficer(int id) {
		for (Officer officer : this.officerList) {
			if (id == officer.getId()) {
				return true;
			}
		}
		return false;
	}
	

	// ==== Room Booking and Modification ====

	/**
	 * Decreases the number of 2-room units available, if authorised.
	 *
	 * @param id ID of the acting officer or manager
	 */
	public void reduceNum2Rooms(int id) {
		if (this.authManager(id) || this.authOfficer(id)) {
			this.num2Rooms--;
		}
	}
	
	/**
	 * Decreases the number of 3-room units available, if authorised.
	 *
	 * @param id ID of the acting officer or manager
	 */
	public void reduceNum3Rooms(int id) {
		if (this.authManager(id) || this.authOfficer(id)) {
			this.num3Rooms--;
		}
	}
	
	/**
	 * Adds a new officer to the officer list for this BTO.
	 *
	 * @param o the officer to add
	 */
	public void addOfficer(Officer o) {
		this.officerList.add(o);
	}
	
	/**
	 * Toggles the visibility of the project.
	 *
	 * @param id the ID of the manager performing the toggle
	 */
	public void toggleVisible(int id) {
		if (this.authManager(id)) {
			this.visible = !this.visible;
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
	
	/**
	 * Converts a {@link Date} to a readable string format "dd/MM/yyyy".
	 *
	 * @param date the date to convert
	 * @return formatted date string
	 */
	public String stringDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
	
	/**
	 * Prints the BTO project details in basic format.
	 */
	public void printBTO() {
		System.out.println("Project Id: " + this.btoId + "\n" + 
							"Project Name: " + this.name + "\n" +
							"Application Start: " + stringDate(this.applicationStart) + "\n" +
							"Application End: " + stringDate(this.applicationEnd) + "\n" +
							"No. of 2 Rooms: " + this.num2Rooms + "\n" +
							"No. of 3 Rooms: " + this.num3Rooms);
	}
	
	/**
	 * Prints the BTO project details with optional visibility status.
	 *
	 * @param manager true if manager view is enabled
	 */
	public void printBTO(boolean manager) {
		System.out.println("Project Id: " + this.btoId + "\n" + 
							"Project Name: " + this.name + "\n" +
							"Neighbourhood: " + this.neighbourhood + "\n" + 
							"Selling price: $" + this.getPrice() + "\n" +
							"Application Start: " + stringDate(this.applicationStart) + "\n" +
							"Application End: " + stringDate(this.applicationEnd) + "\n" +
							"No. of 2 Rooms: " + this.num2Rooms + "\n" +
							"No. of 3 Rooms: " + this.num3Rooms + "\n" +
							(manager ? "Visibility: " + (this.visible ? "Visible" : "Not Visible") : ""));
	}

	/**
	 * Checks if the BTO project matches the specified ID.
	 *
	 * @param id the BTO ID to match
	 * @return true if matched
	 */
	@Override
	public boolean getById(Integer id) {
		return this.btoId == id;
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
