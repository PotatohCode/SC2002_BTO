package BTO;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * 
 *  @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 *  @version 16/4/25
 *  
 *  Applicant.java: generic class for all users
 * 
 */
public class Applicant extends Users implements Search {
	
	private int applicationId = -1;
	private List<Integer> enquiriesId = new ArrayList<>();
	protected int btoId = -1;
	private int roomType = -1;
	
	private Scanner sc = new Scanner(System.in);

	public Applicant(String nric, String name, int age, String password, boolean married) {
		super(nric, name, age, password, married, "applicant");
	}
	
	public Applicant(String nric, String name, int age, String password, boolean married, String role) {
		super(nric, name, age, password, married, role);
	}
	
	// getters
	public int getApplicationId() {
		return this.applicationId;
	}
	
	public List<Integer> getEnquiriesId() {
		return this.enquiriesId;
	}
	
	public int getBTOId() {
		return this.btoId;
	}
	
	public int getRoomType() {
		return this.roomType;
	}
	
	// setter
	public void setRoomType(int roomType, String nric, String role) {
		if (role.equals("officer") && nric.equals(this.nric)) {
			this.roomType = roomType;
		}
	}
	
	// functions
	// viewing bto
	public List<BTO> getApplicableBTOs(List<BTO> btoList) {
		return btoList.stream().filter(b -> canApplyBTO(b, (this.getMarried() ? -1 : 2))).toList();
	}
	
	// check qualification
	public boolean canApplyBTO(BTO bto, int roomType) {
		boolean can = false;
		// check user qualification
		if (bto.getVisible()
				&& (this.btoId != bto.getId() || this.btoId == -1)
				&& ((this.getMarried() && this.getAge() >= 21) 
				|| (!this.getMarried() && this.getAge() >= 35 && roomType == 2))) {
			// check bto qualification
			if (this.getRole().equals("officer")) {
				Officer officer = (Officer) this;
				if (officer.getManaging().contains(bto.getId()) || officer.getOfficerApp().contains(bto.getId())) return false;
			}
			switch (roomType) {
				case -1: can = bto.getNum2Rooms() > 0 || bto.getNum3Rooms() > 0; // for married to see both
				break;
				case 2: can = bto.getNum2Rooms() > 0;
				break;
				case 3: can = bto.getNum3Rooms() > 0;
				break;
			}
		}
		return can;
	}
	
	// application functions
	public Application createApplication(BTO bto, int roomType) {
		if (canApplyBTO(bto, roomType)) {
			Application apply = new Application(this, bto.getId(), roomType);
			this.btoId = bto.getId();
			this.applicationId = apply.getId();
			System.out.println(ANSI_GREEN + "Application submitted!\n" + ANSI_RESET);
			return apply;
		} else {
			System.out.println(ANSI_RED + "You are not applicable to apply for this BTO project\n" + ANSI_RESET);
			return null;
		}
	}
	
	public void withdrawApplication(Application application) {
		if (application.getId() == this.applicationId 
				&& application.getApplicant().getId() == this.getId() 
				&& (application.getStatus() == "successful" || application.getStatus() == "booked")) {
			application.setStatus("withdraw", this.getRole());
			System.out.println(ANSI_GREEN + "Withdrawal submitted!\n" + ANSI_RESET);
		} else {
			System.out.println(ANSI_RED + "Unable to withdraw\n" + ANSI_RESET);
		}
	}
	
	public void bookBTO(Application application) {
		if (application.getId() == this.applicationId 
				&& application.getApplicant().getId() == this.getId() 
				&& application.getStatus() == "successful") {
			application.setStatus("booking", this.getRole());
			System.out.println(ANSI_GREEN + "Booking requested!\n" + ANSI_RESET);
		} else {
			System.out.println(ANSI_RED + "Unable to book\n" + ANSI_RESET);
		}
	}
	
	// enquiry function
	public Enquiries createEnquiries(String inpEnquiry, int btoId) {
		Enquiries enquiry = new Enquiries(inpEnquiry, this.getId(), btoId);
		this.enquiriesId.add(enquiry.getId());
		System.out.println(ANSI_GREEN + "Enquiry submitted!\n" + ANSI_RESET);
		return enquiry;
	}
	
	public void editEnquiries(Enquiries enquiry, String inpEnquiry) {
		if (this.enquiriesId.contains(enquiry.getId()) && enquiry.getEnquirierId() == this.getId()) {
			enquiry.setEnquiry(inpEnquiry);
			System.out.println(ANSI_GREEN + "Enquiry updated!\n" + ANSI_RESET);
		}
	}
	
	public void deleteEnquiries(int enquiryId) {
		this.enquiriesId.remove(enquiryId);
		System.out.println(ANSI_GREEN + "Enquiry deleted!\n" + ANSI_RESET);
	}

	public void printApplicant() {
		System.out.println("Applicant: " + this.getName() + "\n"
				+ "NRIC: " + this.getNric() + "\n"
				+ "Age: " + this.getAge() + "\n"
				+ "Martial Status: " + (this.getMarried() ? "Married" : "Single")
				+ (this.applicationId > -1 ? "\nApplication Id: " + this.applicationId : "")
				+ (this.roomType > -1 ? "\nRoom Type: " + this.roomType + "-Room" : ""));
	}
	
	public void showMenu(Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj) {
		boolean run = true;
		while (run) {
			try {
				System.out.println(ANSI_CYAN + "Menu:" + ANSI_RESET);
				System.out.println("1. View available BTOs");
				System.out.println((this.getApplicationId() > -1 ? "2. Access application" : "2. Apply for BTO"));
				System.out.println("3. Submit enquiry");
				System.out.println("4. Access enquiries");
				System.out.println("5. Logout");
			
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int menuOption = sc.nextInt();
				sc.nextLine(); // capture extra \n
				System.out.println();
				
				switch (menuOption) {
					case 1: 
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> availBTO = this.getApplicableBTOs(btoProj.getItems());
						if (availBTO.size() == 0) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						printBTOs(availBTO);
						break;
						
					case 2: 
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						if (this.getApplicationId() == -1) { // create application
							List<BTO> applyList = this.getApplicableBTOs(btoProj.getItems());
							if (applyList.size() == 0) {
								System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
								break;
							}
							printBTOs(applyList);
							
							System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
							int inpBTOId = sc.nextInt();
							sc.nextLine();
							BTO selectBTO = getBTOById(btoProj.getItems(),inpBTOId);
							if (selectBTO == null) throw new InvalidInput("BTO"); // invalid capture
							
							int inpRT = 2;
							if (this.getMarried()) {
								System.out.print(ANSI_YELLOW + "Enter room type: " + ANSI_RESET);
								inpRT = sc.nextInt();
								sc.nextLine();
								if (inpRT != 2 && inpRT != 3) throw new InvalidInput("room type"); // invalid capture
							}
							
							Application application = this.createApplication(selectBTO, inpRT);
							if (application != null) appProj.addItem(application);
						} else { // access application
							BTO appliedBTO = getBTOById(btoProj.getItems(), this.getBTOId());
							appliedBTO.printBTO();
							Application application = getAppById(appProj.getItems(), this.getApplicationId());
							application.printApplication();
							System.out.println();
							if (application.getStatus().equals("successful") || application.getStatus().equals("booking")) {
								System.out.println(ANSI_CYAN + "BTO Menu:" + ANSI_RESET);
								System.out.println("1. Withdraw application");
								if (application.getStatus().equals("successful")) {
									System.out.println("2. Book flat with an officer");
									System.out.println("3. Return to menu");
								} else {
									System.out.println("2. Return to menu");
								}
								System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
								int applicationMenu = sc.nextInt();
								sc.nextLine();
								
								switch (applicationMenu) {
									case 1:
										this.withdrawApplication(application);
										break;
									case 2: 
										if (!application.getStatus().equals("successful")) break;
										this.bookBTO(application);
										break;
									case 3: if (!application.getStatus().equals("successful")) throw new InvalidInput("option");
										break;
									default: throw new InvalidInput("option");
								}
							}
						}
						break;
						
					case 3:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> btoList = this.getApplicableBTOs(btoProj.getItems());
						if (this.getBTOId() > -1) getBTOById(btoProj.getItems(), this.getBTOId()).printBTO();
						printBTOs(btoList);
						System.out.println();
						System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
						int inpBTOId = sc.nextInt();
						sc.nextLine();
						BTO applyBTO = getBTOById(btoProj.getItems(), inpBTOId);
						if (applyBTO == null) throw new InvalidInput("BTO"); // invalid capture
						
						System.out.print(ANSI_YELLOW + "Enter enquiry: " + ANSI_RESET);
						String inpEnq = sc.nextLine();
						if (inpEnq.length() <= 0) throw new InvalidInput("enquiry"); // invalid capture
						enquiryProj.addItem(this.createEnquiries(inpEnq, inpBTOId));
						break;
						
					case 4:
						System.out.println(ANSI_CYAN + "===== Enquiries =====" + ANSI_RESET);
						List<Enquiries> userEnquiry = getEnquiryByUser(enquiryProj.getItems(), this.getId());
						if (userEnquiry.size() == 0) {
							System.out.println(ANSI_RED + "No exisiting enquiry\n" + ANSI_RESET);
							break;
						}
						printEnquiries(userEnquiry, btoProj.getItems());
						
						System.out.print(ANSI_YELLOW + "Enter enquiry id or -1 to return: " + ANSI_RESET);
						int enquiryId = sc.nextInt();
						sc.nextLine();
						if (enquiryId == -1) break; 
						Enquiries enquiry = getEnquiryById(enquiryProj.getItems(), enquiryId);
						if (enquiry == null) throw new InvalidInput("enquiry"); // invalid capture
						
						System.out.println(ANSI_CYAN + "Enquiry Menu:" + ANSI_RESET);
						System.out.println("1. Edit\n2. Delete\n3. Return to menu");
						System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
						int enquiryMenu = sc.nextInt();
						sc.nextLine();
						switch(enquiryMenu) {
							case 1:
								System.out.print(ANSI_YELLOW + "Enter enquiry: " + ANSI_RESET);
								String editEnq = sc.nextLine();
								if (editEnq.length() <= 0) throw new InvalidInput("enquiry"); // invalid capture
								this.editEnquiries(enquiry, editEnq);
								break;
							case 2:
								this.deleteEnquiries(enquiryId);
								enquiryProj.removeItem(enquiry);
								break;
							case 3:
								break;
							default: throw new InvalidInput("option");
						}
						break;
					case 5: return;
					default: throw new InvalidInput("option");
				}
			} catch (InputMismatchException ime) {
				System.out.println("Invalid input!");
			} catch (InvalidInput ii) {
				System.out.println(ANSI_RED + ii.getMessage() + "\n" + ANSI_RESET);
			}
		}
	}
	
}