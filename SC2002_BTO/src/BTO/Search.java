package BTO;

import java.util.List;

public interface Search<T> {
	boolean getById(T id);
	default boolean getByBTO(T id) { return false; };
	default boolean getByUser(T id) { return false; };
//	// get BTO by id
//	default BTO getBTOById(List<BTO> btoList, int id) {
//		return btoList.stream()
//				.filter(b -> b.getId() == id)
//				.findFirst().orElse(null);
//	}
//	
//	// get applicant by nric
//	default Applicant getApplicantByNric(List<Users> userList, String nric) {
//		return (Applicant) userList.stream()
//				.filter(a -> a.getNric().equals(nric))
//				.findFirst().orElse(null);
//	}
//	
//	// get enquiries by id
//	default Enquiries getEnquiryById(List<Enquiries> enqList, int id) {
//		return enqList.stream()
//				.filter(e -> e.getId() == id)
//				.findFirst().orElse(null);
//	}
//	
//	// get enquiries by bto projects
//	default List<Enquiries> getEnquiryByBTO(List<Enquiries> enqList, int btoId) {
//		return enqList.stream()
//				.filter(e -> e.getBTOId() == btoId).toList();
//	}
//	
//	// get enquiries by users
//	default List<Enquiries> getEnquiryByUser(List<Enquiries> enqList, int userId) {
//		return enqList.stream()
//				.filter(e -> e.getEnquirierId() == userId).toList();
//	}
//	
//	// get application by id
//	default Application getAppById(List<Application> appList, int id) {
//		return appList.stream()
//				.filter(a -> a.getId() == id)
//				.findFirst().orElse(null);
//	}
//	
//	// get application by bto projects
//	default List<Application> getAppByBTO(List<Application> appList, int btoId, String type, String status) {
//		return appList.stream()
//				.filter(a -> a.getBTOId() == btoId && a.getType().equals(type) && (status == null || (a.getStatus() == status))).toList();
//	}
//	
//	// get application by users
//	default List<Application> getAppByUser(List<Application> appList, int userId, String type) {
//		return appList.stream()
//				.filter(a -> a.getApplicant().getId() == userId && a.getType().equals(type)).toList();
//	}
//	
//	// print given btoList
//	default void printBTOs(List<BTO> btoList) {
//		for (BTO b : btoList) {
//			b.printBTO();
//			System.out.println();
//		}
//	}
//	
//	// print given enquiryList with bto name
//	default void printEnquiries(List<Enquiries> enquiryList, List<BTO> btoList) {
//		for (Enquiries e : enquiryList) {
//			System.out.println("Project Name: " + getBTOById(btoList, e.getBTOId()).getName());
//			e.printEnquiry();
//			System.out.println();
//		}
//	}
//	
//	// print given application list with bto name
//	default void printBTOApp(List<Application> appList, List<BTO> btoList) {
//		for (Application a : appList) {
//			System.out.println("Project Name: " + getBTOById(btoList, a.getBTOId()).getName());
//			a.printApplication();
//			System.out.println();
//		}
//	}
	
}
