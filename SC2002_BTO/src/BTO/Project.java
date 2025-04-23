package BTO;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic container class that manages collections of entities within the BTO system.
 * 
 * This class acts as a lightweight project-level storage for any type of item, 
 * such as Users, Applications, Enquiries, or BTOs. It supports basic operations including:
 * adding items, removing items, and retrieving the full list.
 * 
 * It is used throughout the system to hold user lists, project data, and other core records.
 * 
 * @param <T> the type of item this project manages
 * 
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 */
public class Project<T> {
	// declare
	private List<T> list;
	
	// constructor
	/**
	 * Project()
	 * Constructs a new Project instance with an empty list.
	 */
	public Project() {
		this.list = new ArrayList<>();
	}
	
	// get
	/**
	 * Returns the list of all items in this project.
	 * @return list of items
	 */
	public List<T> getItems() {
		return this.list;
	}
	
	/**
	 * int getCount()
	 * Returns the number of items in the project.
	 * @return item count
	 */
	public int getCount() {
		return this.list.size();
	}
	
	// add
	/**
	 * void addItem(T item)
	 * Adds a new item to the project.
	 * @param item the item to add
	 */
	public void addItem(T item) {
		this.list.add(item);
	}
	
	// remove
	/**
	 * void removeItem(T item)
	 * Removes an item from the project.
	 * @param item the item to remove
	 */
	public void removeItem(T item) {
		this.list.remove(item);
	}
	
	// get BTO by id
	/**
	 * Retrieves a BTO project by its ID.
	 *
	 * @param id the BTO ID to search for
	 * @return the {@link BTO} object if found, otherwise {@code null}
	 */
	public BTO getBTOById(int id) {
		List<BTO> btoList = (List<BTO>) this.list;
		return btoList.stream()
				.filter(b -> b.getById(id))
				.findFirst().orElse(null);
	}
	
	
	/**
	 * Retrieves a BTO project by ID from a given list.
	 *
	 * @param btoList the list of BTOs to search in
	 * @param id the BTO ID to search for
	 * @return the {@link BTO} object if found, otherwise {@code null}
	 */
	public BTO getBTOById(List<BTO> btoList, int id) {
		return btoList.stream()
				.filter(b -> b.getById(id))
				.findFirst().orElse(null);
	}
	

	/**
	 * Retrieves an enquiry by its ID.
	 *
	 * @param id the enquiry ID
	 * @return the {@link Enquiries} object if found, otherwise {@code null}
	 */
	public Enquiries getEnquiryById(int id) {
		List<Enquiries> enqList = (List<Enquiries>) this.list;
		return enqList.stream()
				.filter(e -> e.getById(id))
				.findFirst().orElse(null);
	}
	
	/**
	 * Retrieves all enquiries associated with a specific BTO ID.
	 *
	 * @param btoId the BTO project ID
	 * @return a list of matching {@link Enquiries}
	 */
	public List<Enquiries> getEnquiryByBTO(int btoId) {
		List<Enquiries> enqList = (List<Enquiries>) this.list;
		return enqList.stream()
				.filter(e -> e.getBTOId() == btoId).toList();
	}
	
	/**
	 * Retrieves all enquiries submitted by a specific user.
	 *
	 * @param userId the user ID
	 * @return a list of enquiries from the specified user
	 */
	public List<Enquiries> getEnquiryByUser(int userId) {
		List<Enquiries> enqList = (List<Enquiries>) this.list;
		return enqList.stream()
				.filter(e -> e.getEnquirierId() == userId).toList();
	}
	
	/**
	 * Retrieves an application by its ID.
	 *
	 * @param id the application ID
	 * @return the {@link Application} object if found, otherwise {@code null}
	 */
	public Application getAppById(int id) {
		List<Application> appList = (List<Application>) this.list;
		return appList.stream()
				.filter(a -> a.getId() == id)
				.findFirst().orElse(null);
	}
	
	/**
	 * Retrieves applications filtered by BTO ID, type, and optional status.
	 *
	 * @param btoId the BTO project ID
	 * @param type the application type ("bto" or "officer")
	 * @param status optional status filter (e.g., "pending", "booked"); pass {@code null} for no filter
	 * @return a list of matching {@link Application}s
	 */
	public List<Application> getAppByBTO(int btoId, String type, String status) {
		List<Application> appList = (List<Application>) this.list;
		return appList.stream()
				.filter(a -> a.getBTOId() == btoId && a.getType().equals(type) && (status == null || (a.getStatus() == status))).toList();
	}
	
	/**
	 * Retrieves applications submitted by a specific user.
	 *
	 * @param userId the applicant user ID
	 * @param type the application type
	 * @return a list of applications submitted by the user
	 */
	public List<Application> getAppByUser(int userId, String type) {
		List<Application> appList = (List<Application>) this.list;
		return appList.stream()
				.filter(a -> a.getApplicant().getId() == userId && a.getType().equals(type)).toList();
	}
	

	/**
	 * Finds a user by NRIC from the stored list.
	 *
	 * @param nric the user's NRIC
	 * @return the {@link Users} object if found, otherwise {@code null}
	 */
	public Users getApplicantByNric(String nric) {
		List<Users> userList = (List<Users>) this.list;
		return userList.stream()
				.filter(a -> a.getNric().equals(nric))
				.findFirst().orElse(null);
	}
	
	/**
	 * Retrieve applicant by btoid
	 * @param btoId the bto id of the applicant
	 * @return a list of applicants part of the bto
	 */
	public List<Applicant> getApplicantByBTO(int btoId) {
		List<Users> userList = (List<Users>) this.list;
		userList =	userList.stream()
				.filter(a -> a.getRole().equals("applicant") || a.getRole().equals("officer")).toList();
		List<Applicant> appList = new ArrayList<>();
		for (Users u : userList) {
			appList.add((Applicant) u);
		}
		return appList.stream().filter(a -> a.getBTOId() == btoId).toList();
	}
	
	/**
	 * Retrieve officer by btoid
	 * @param btoId the bto id of the officer
	 * @return a list of officer part of the bto
	 */
	public List<Officer> getOfficerByBTO(int btoId) {
		List<Users> userList = (List<Users>) this.list;
		userList =	userList.stream()
				.filter(a -> a.getRole().equals("officer")).toList();
		List<Officer> oList = new ArrayList<>();
		for (Users u : userList) {
			oList.add((Officer) u);
		}
		return oList.stream()
				.filter(a -> a.getManaging().contains(btoId)).toList();
	}
	
	/**
	 * Prints details for all BTO projects in the provided list.
	 *
	 * @param btoList the list of BTOs to print
	 */
	public void printBTOs(List<BTO> btoList) {
		for (BTO b : btoList) {
			b.printBTO();
			System.out.println();
		}
	}
	

	/**
	 * Prints details for all BTO projects with visibility toggle included.
	 *
	 * @param btoList the list of BTOs to print
	 * @param manager whether the manager view is enabled
	 */
	public void printBTOs(List<BTO> btoList, boolean manager) {
		for (BTO b : btoList) {
			b.printBTO(manager);
			System.out.println();
		}
	}
	

	/**
	 * Prints enquiries along with their associated BTO project names.
	 *
	 * @param enquiryList the list of enquiries
	 * @param btoList the list of BTO projects for name lookup
	 */
	public void printEnquiries(List<Enquiries> enquiryList, List<BTO> btoList) {
		for (Enquiries e : enquiryList) {
			System.out.println("Project Name: " + getBTOById(e.getBTOId()).getName());
			e.printEnquiry();
			System.out.println();
		}
	}
	
	// print given application list with bto name
	/**
	 * Prints applications along with their associated BTO project names.
	 *
	 * @param appList the list of applications
	 * @param btoList the list of BTO projects for name lookup
	 */
	public void printBTOApp(List<Application> appList, List<BTO> btoList) {
		for (Application a : appList) {
			System.out.println("Project Name: " + getBTOById(btoList, a.getBTOId()).getName());
			a.printApplication();
			System.out.println();
		}
	}

}
