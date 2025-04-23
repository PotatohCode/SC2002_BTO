package BTO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


/**
 * Represents a user with officer-level permissions in the BTO system.
 * 
 * Officers are specialised users who inherit from {@link Applicant} and
 * are authorised to manage BTO applications, reply to enquiries,
 * book flats on behalf of applicants, and access booking and enquiry workflows.
 * 
 * Officers can:
 * <ul>
 *   <li>Apply to become BTO officers</li>
 *   <li>Be appointed as managing officers</li>
 *   <li>Facilitate the booking process</li>
 *   <li>View and respond to user enquiries</li>
 * </ul>
 * 
 * Officers are assigned to projects dynamically, and also interact with
 * other users through the shared BTO interface.
 * 
 * Implements {@link Search} and {@link Admin} interfaces.
 * 
 * Authorised operations depend on role and project assignment.
 * 
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 */

public class Officer extends Applicant implements Admin {
	
	private List<Integer> managingId = new ArrayList<>();
	private List<Integer> officerAppId = new ArrayList<>();
	private Scanner sc = new Scanner(System.in);
	
	/**
	 * Constructs a new officer user.
	 * Officer inherits applicant-level permissions and is initialised with officer-specific role.
	 *
	 * @param nric     the NRIC of the officer
	 * @param name     the name of the officer
	 * @param age      the age of the officer
	 * @param password the officer’s login password
	 * @param married  the marital status of the officer
	 */
	public Officer (String nric, String name, int age, String password, boolean married) {
		super(nric, name, age, password, married, "officer");
	}
	
	// getters
	/**
	 * Returns the list of BTO IDs the officer is currently managing.
	 *
	 * @return a list of BTO project IDs
	 */
	public List<Integer> getManaging() {
		return this.managingId;
	}
	
	/**
	 * Returns the list of BTO project IDs the officer has applied to manage.
	 *
	 * @return a list of BTO project IDs for officer applications
	 */
	public List<Integer> getOfficerApp() {
		return this.officerAppId;
	}
	
	// setter
	/**
	 * Adds a BTO project ID to the officer's list of managing projects.
	 *
	 * @param btoId the BTO project ID
	 */
	public void addManagingBTO(int btoId) {
		this.managingId.add(btoId);
	}
	
	/**
	 * Remove a BTO project ID from the officer's list of managing projects.
	 *
	 * @param btoId the BTO project ID
	 */
	public void removeManagingBTO(int btoId) {
		this.managingId.remove(btoId);
	}
	
	// functions
	// check criteria to apply officer
	/**
	 * Checks whether the officer is eligible to apply to manage a BTO project.
	 *
	 * @param bto the BTO project to evaluate
	 * @param clash whether the application period clashes with an existing assignment
	 * @return true if officer is eligible, false otherwise
	 */
	public boolean canApplyOfficer(BTO bto, boolean clash) {
		return !clash && bto.getId() != this.btoId && !this.managingId.contains(bto.getId()) && !this.officerAppId.contains(bto.getId()) && bto.getMaxOfficer() > 0;
	}
	
	// application functions
	/**
	 * Creates an officer application for the given BTO project if eligible.
	 *
	 * @param bto the BTO project to apply for
	 * @param clash whether the project clashes with other assigned periods
	 * @return a new {@link Application} object if valid; null otherwise
	 */
	public Application createOfficerApplication(BTO bto, boolean clash) {
		if (this.canApplyOfficer(bto, clash)) {
			Application apply = new Application(this, bto.getId(), "pending", "officer");
			this.btoId = bto.getId();
			this.officerAppId.add(bto.getId());
			System.out.println(ANSI_GREEN + "Application submitted!\n"+ ANSI_RESET);
			return apply;
		} else {
			System.out.println(ANSI_RED + "You are not applicable to apply for this BTO project\n"+ ANSI_RESET);
			return null;
		}
	}
	
	/**
	 * Checks for any date overlaps between the given application dates and
	 * the officer's current managing projects.
	 *
	 * @param start the start date of the new project
	 * @param end the end date of the new project
	 * @param btoIdList the list of BTO IDs the officer is already assigned to
	 * @param btoList the list of all BTO projects
	 * @return true if any date overlaps are found, false otherwise
	 */
	public boolean clashApplication(Date start, Date end, List<Integer> btoIdList, List<BTO> btoList) {
		for (BTO bto : btoList) {
			if (btoIdList.contains(bto.getId())) {
				if (start.after(bto.getApplicationStart()) && end.before(bto.getApplicationEnd())) {
					return true;
				}
			}
		}
		return false;
	}
	
	// book room
	/**
	 * Books a room on behalf of an applicant if the application is valid and the officer is authorised.
	 * This method updates application status, reduces available units, and updates applicant profile.
	 *
	 * @param bto the BTO project associated with the booking
	 * @param application the applicant’s application to be booked
	 * @return the NRIC of the applicant if booking succeeds, otherwise {@code null}
	 */
	public String bookBTO(BTO bto, Application application) {
		if (application.getStatus() == "booking" && this.managingId.contains(bto.getId())) {
			switch (application.getRoomType()) {
				case 2: 
					if (bto.getNum2Rooms() > 0) {
						bto.reduceNum2Rooms(this.getId());
					} else {
						System.out.println("Unable to book");
						return null;
					}
				break;
				case 3: 
					if (bto.getNum3Rooms() > 0) {
						bto.reduceNum3Rooms(this.getId());
					} else {
						System.out.println("Unable to book");
						return null;
					}
				break;
			}
			Applicant applicant = (Applicant) application.getApplicant();
			System.out.println(ANSI_GREEN + "Room numbers updated!"+ ANSI_RESET);
			application.setStatus("booked", "officer");
			System.out.println(ANSI_GREEN + "Application status updated!"+ ANSI_RESET);
			applicant.setRoomType(application.getRoomType(), applicant.getNric(), this.getRole());
			System.out.println(ANSI_GREEN + "Applicant profile updated!"+ ANSI_RESET);
			System.out.println(ANSI_GREEN + "Booked successfully!"+ ANSI_RESET);
			System.out.println(ANSI_GREEN + "Applicant NRIC: " + ANSI_RESET + applicant.getNric());
			System.out.println();
			return applicant.getNric();
		} else {
			System.out.println(ANSI_RED + "Unable to book"+ ANSI_RESET);
			return null;
		}
	}
	
	// generate receipt
	/**
	 * Generates and prints a booking receipt for a given applicant and their assigned BTO project.
	 *
	 * @param bto the booked BTO project
	 * @param applicant the applicant whose booking details are printed
	 */
	public void applicantReceipt(BTO bto, Applicant applicant) {
		applicant.printApplicant();
		bto.printBTO();
		System.out.println();
	}
	
//	public void replyEnquiry(Project<Enquiries> enquiryProj) throws InvalidInput {
//		System.out.print(ANSI_YELLOW + "Enter enquiry id: " + ANSI_RESET);
//		int inpEnqId = sc.nextInt();
//		sc.nextLine();
//		Enquiries enquiry = enquiryProj.getEnquiryById(inpEnqId);
//		enquiry.printEnquiry();
//		if (enquiry.getReplierId() > -1) {
//			System.out.println(ANSI_RED + "Enquiry already has a reply\n" + ANSI_RESET);
//			return;
//		}
//		System.out.print(ANSI_YELLOW + "Enter reply: " + ANSI_RESET);
//		String reply = sc.nextLine();
//		if (reply.length() <= 0) throw new InvalidInput("reply");
//		enquiry.setReply(reply, this.getId(), this.getRole());
//	}
	
	
	/**
	 * Menu handler for officers managing BTO projects.
	 * Officers can respond to enquiries and approve booking requests.
	 *
	 * @param managingId the list of BTO project IDs this officer manages
	 * @param btoProj the full list of BTO projects
	 * @param appProj the application records
	 * @param enquiryProj the enquiry records
	 * @param userProj the user records
	 * @throws InputMismatchException for invalid inputs
	 * @throws InvalidInput for any domain-specific invalid selections
	 */
	@Override
	public void managingBTO(List<Integer> managingId, Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj, Project<Users> userProj) throws InputMismatchException, InvalidInput {
		System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
		List<BTO> managingBTO = btoProj.getItems().stream().filter(b -> managingId.contains(b.getId())).toList();
		if (managingBTO.size() <= 0) {
			System.out.println(ANSI_RED + "No managing BTOs\n" + ANSI_RESET);
			return;
		}
		btoProj.printBTOs(managingBTO);
		
		System.out.println("What would you like to do?\n"
						+ "1. Access enquries\n"
						+ "2. Accept booking requests\n"
						+ "3. Return to menu");
		System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
		int manageMenu = sc.nextInt();
		sc.nextLine();
		if (manageMenu == 3) return;
		if (manageMenu < 1 && manageMenu > 3) throw new InvalidInput("option");
		
		System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
		int inpBTOId = sc.nextInt();
		sc.nextLine();
		if (!managingId.contains(inpBTOId)) throw new InvalidInput("BTO");
		BTO bto = btoProj.getBTOById(inpBTOId);
		if (bto == null) throw new InvalidInput("BTO");
		System.out.println();
		
		if (manageMenu == 1) {
			System.out.println(ANSI_CYAN + "===== Enquiries =====" + ANSI_RESET);
			List<Enquiries> btoEnquiries = enquiryProj.getEnquiryByBTO(inpBTOId);
			if (btoEnquiries.size() <= 0) {
				System.out.println(ANSI_RED + "No enquiries for this BTO\n" + ANSI_RESET);
				return;
			}
			enquiryProj.printEnquiries(btoEnquiries, btoProj.getItems());
			
			System.out.println("What would you like to do?\n"
						+ "1. Reply enquiry\n"
						+ "2. Return to menu");
			System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
			int manageMenu2 = sc.nextInt();
			sc.nextLine();
			if (manageMenu2 == 2) return;
			if (manageMenu2 < 1 && manageMenu > 2) throw new InvalidInput("option");
			replyEnquiry(this.getId(), this.getRole(), enquiryProj, sc);
		} else if(manageMenu == 2 && this.getRole().equals("officer")) {
			System.out.println(ANSI_CYAN + "===== Booking Applications =====" + ANSI_RESET);
			List<Application> bookingApps = appProj.getAppByBTO(inpBTOId, "bto", "booking");
			if (bookingApps.size() <= 0) {
				System.out.println(ANSI_RED + "No booking applications\n" + ANSI_RESET);
				return;
			}
			btoProj.printBTOApp(bookingApps, btoProj.getItems());
			
			System.out.print(ANSI_YELLOW + "Enter application id or -1 to return: " + ANSI_RESET);
			int inpAppId = sc.nextInt();
			sc.nextLine();
			if (inpAppId == -1) return;
			Application bookingApp = appProj.getAppById(inpAppId);
			if (bookingApp == null) throw new InvalidInput("application");
			this.bookBTO(bto, bookingApp);
		}
	}
	

	/**
	 * Displays the officer's main menu and handles officer-specific workflows.
	 * Includes actions like officer application, booking confirmation, enquiry response, and password updates.
	 *
	 * @param btoProj the BTO project container
	 * @param appProj the application container
	 * @param enquiryProj the enquiry container
	 * @param userList the full list of users
	 */
	public void showMenu(Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj, List<Users> userList) {
		boolean run = true;
		while (run) {
			try {			
				System.out.println(ANSI_CYAN + "Menu:" + ANSI_RESET);
				System.out.println("1. View BTOs availble for officer application");
				System.out.println("2. Register as officer");
				System.out.println("3. Access officer applications");
				System.out.println("4. Access officiating BTOs");
				System.out.println("5. Generate booking receipt");
				System.out.println("6. Change Search Filters");
				System.out.println("7. Change password");
				System.out.println("8. Logout");
			
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int menuOption = sc.nextInt();
				sc.nextLine(); // capture extra \n
				System.out.println();
				
				List<BTO> managingBTO = btoProj.getItems().stream().filter(b -> managingId.contains(b.getId())).toList();
				
				switch(menuOption) {
					case 1:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> availBTO = btoProj.getItems().stream()
												.filter(b -> this.canApplyOfficer(b, clashApplication(b, managingBTO))).toList();
						availBTO = FilterUtil.applyUserFilters(availBTO, this);
						if (availBTO.size() <= 0) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						btoProj.printBTOs(availBTO);
						break;
						
					case 2:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> applyBTO = btoProj.getItems().stream().filter(b -> this.canApplyOfficer(b, clashApplication(b, managingBTO))).toList();
						applyBTO = FilterUtil.applyUserFilters(applyBTO, this);
						if (applyBTO.size() == 0) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						btoProj.printBTOs(applyBTO);
						
						System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
						int inpBTOId = sc.nextInt();
						sc.nextLine();
						BTO selectBTO = btoProj.getBTOById(inpBTOId);
						if (selectBTO == null) throw new InvalidInput("BTO"); // invalid capture
						
						Application application = this.createOfficerApplication(selectBTO, clashApplication(selectBTO, managingBTO));
						if (application != null) appProj.addItem(application);
						break;
					
					case 3:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<Application> officerAppList = appProj.getAppByUser(this.getId(), "officer");
						if (officerAppList.size() <= 0) {
							System.out.println(ANSI_RED + "No applications\n" + ANSI_RESET);
						}
						appProj.printBTOApp(officerAppList, btoProj.getItems());
						break;
						
					case 4:
						managingBTO(this.getManaging(), btoProj, appProj, enquiryProj, null);
						break;
					case 5:
						System.out.print(ANSI_YELLOW + "Enter applicant nric: " + ANSI_RESET);
						String appNric = sc.nextLine();
						Users u = appProj.getApplicantByNric(appNric);
						Applicant applicant = null;
						if (u.getRole() == "applicant") applicant = (Applicant) u;
						if (applicant == null) throw new InvalidInput("applicant");
						System.out.println(ANSI_CYAN + "===== Applicant Booking Receipt =====" + ANSI_RESET);
						this.applicantReceipt(btoProj.getBTOById(applicant.getBTOId()), applicant);
						break;
					case 6:
						System.out.print("Enter neighbourhood keyword (leave blank to skip): ");
					    this.setFilterNeighbourhood(sc.nextLine());

					    System.out.print("Filter by room type? (2/3/-1 for any): ");
					    this.setFilterRoomType(sc.nextInt());
					    sc.nextLine();

					    System.out.print("Only show open applications? (true/false): ");
					    this.setFilterOpenOnly(sc.nextBoolean());
					    sc.nextLine();

					    System.out.print("Project name contains (leave blank to skip): ");
					    this.setFilterProjectName(sc.nextLine());

					    System.out.println(ANSI_GREEN + "Filters updated!" + ANSI_RESET);
					    break;
					case 7:
					    System.out.print(ANSI_YELLOW + "Enter new password: " + ANSI_RESET);
					    String newPwd = sc.nextLine();

					    if (newPwd.trim().isEmpty()) {
					        System.out.println(ANSI_RED + "Password cannot be empty.\n" + ANSI_RESET);
					        break;
					    }

					    this.changePassword(newPwd);
					    System.out.println(ANSI_GREEN + "Password changed successfully!\n" + ANSI_RESET);
					    break;
					case 8: return;
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
