package BTO;

import java.util.ArrayList;
import java.util.List;

public class Project<T> {
	// declare
	private List<T> list;
	
	// constructor
	public Project() {
		this.list = new ArrayList<>();
	}
	
	// get
	public List<T> getItems() {
		return this.list;
	}
	
	public int getCount() {
		return this.list.size();
	}
	
	// add
	public void addItem(T item) {
		this.list.add(item);
	}
	
	// remove
	public void removeItem(T item) {
		this.list.remove(item);
	}
	
	// get BTO by id
	public BTO getBTOById(int id) {
		List<BTO> btoList = (List<BTO>) this.list;
		return btoList.stream()
				.filter(b -> b.getById(id))
				.findFirst().orElse(null);
	}
	
	// get enquiries by id
	public Enquiries getEnquiryById(int id) {
		List<Enquiries> enqList = (List<Enquiries>) this.list;
		return enqList.stream()
				.filter(e -> e.getById(id))
				.findFirst().orElse(null);
	}
	
	// get enquiries by bto projects
	public List<Enquiries> getEnquiryByBTO(int btoId) {
		List<Enquiries> enqList = (List<Enquiries>) this.list;
		return enqList.stream()
				.filter(e -> e.getBTOId() == btoId).toList();
	}
	
	// get enquiries by users
	public List<Enquiries> getEnquiryByUser(int userId) {
		List<Enquiries> enqList = (List<Enquiries>) this.list;
		return enqList.stream()
				.filter(e -> e.getEnquirierId() == userId).toList();
	}
	
	// get application by id
	public Application getAppById(int id) {
		List<Application> appList = (List<Application>) this.list;
		return appList.stream()
				.filter(a -> a.getId() == id)
				.findFirst().orElse(null);
	}
	
	// get application by bto projects
	public List<Application> getAppByBTO(int btoId, String type, String status) {
		List<Application> appList = (List<Application>) this.list;
		return appList.stream()
				.filter(a -> a.getBTOId() == btoId && a.getType().equals(type) && (status == null || (a.getStatus() == status))).toList();
	}
	
	// get application by users
	public List<Application> getAppByUser(int userId, String type) {
		List<Application> appList = (List<Application>) this.list;
		return appList.stream()
				.filter(a -> a.getApplicant().getId() == userId && a.getType().equals(type)).toList();
	}
	
	// get applicant by nric
	public Applicant getApplicantByNric(String nric) {
		List<Users> userList = (List<Users>) this.list;
		return (Applicant) userList.stream()
				.filter(a -> a.getNric().equals(nric))
				.findFirst().orElse(null);
	}
	
	// print given btoList
	public void printBTOs(List<BTO> btoList) {
		for (BTO b : btoList) {
			b.printBTO();
			System.out.println();
		}
	}
	
	// print given enquiryList with bto name
	public void printEnquiries(List<Enquiries> enquiryList, List<BTO> btoList) {
		for (Enquiries e : enquiryList) {
			System.out.println("Project Name: " + getBTOById(e.getBTOId()).getName());
			e.printEnquiry();
			System.out.println();
		}
	}
	
	// print given application list with bto name
	public void printBTOApp(List<Application> appList, List<BTO> btoList) {
		for (Application a : appList) {
			System.out.println("Project Name: " + getBTOById(a.getBTOId()).getName());
			a.printApplication();
			System.out.println();
		}
	}

}
