package BTO;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Represents an applicant in the BTO system. 
 * This class extends {@link Users} and implements {@link Search}, providing logic for applying to BTO projects,
 * handling application workflows, and managing personal enquiries.
 * 
 * An applicant can:
 * <ul>
 *     <li>Apply for a BTO project (based on eligibility)</li>
 *     <li>Withdraw or book BTO units</li>
 *     <li>Create, edit, or delete enquiries</li>
 *     <li>Interact with the system through a user menu</li>
 * </ul>
 * 
 * Fields include application ID, linked BTO ID, room type, and a list of submitted enquiries.
 * 
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 * @version 16/4/25
 */

public class Applicant extends Users {
	
	private int applicationId = -1;
	private List<Integer> enquiriesId = new ArrayList<>();
	protected int btoId = -1;
	private int roomType = -1;
	
	private Scanner sc = new Scanner(System.in);
	
	/**
	 * Constructs an Applicant object with default role set to "applicant".
	 *
	 * @param nric the NRIC of the applicant
	 * @param name the name of the applicant
	 * @param age the age of the applicant
	 * @param password the login password
	 * @param married the marital status of the applicant
	 */
	public Applicant(String nric, String name, int age, String password, boolean married) {
		super(nric, name, age, password, married, "applicant");
	}
	
	/**
	 * Constructs an Applicant object with a specified user role (e.g., officer).
	 *
	 * @param nric the NRIC of the user
	 * @param name the name of the user
	 * @param age the age of the user
	 * @param password the login password
	 * @param married the marital status
	 * @param role the role to assign to this user (e.g., "applicant", "officer")
	 */
	public Applicant(String nric, String name, int age, String password, boolean married, String role) {
		super(nric, name, age, password, married, role);
	}
	
	// getters
	
	/**
	 * Returns the application ID associated with this applicant.
	 * @return the application ID, or -1 if none exists
	 */
	public int getApplicationId() {
		return this.applicationId;
	}
	
	/**
	 * Returns a list of enquiry IDs submitted by the applicant.
	 * @return list of enquiry IDs
	 */
	public List<Integer> getEnquiriesId() {
		return this.enquiriesId;
	}
	
	/**
	 * Returns the BTO project ID associated with the applicant.
	 * @return the BTO project ID, or -1 if none exists
	 */
	public int getBTOId() {
		return this.btoId;
	}
	

	/**
	 * Returns the room type selected in the application.
	 * @return the room type (2 or 3), or -1 if not set
	 */
	public int getRoomType() {
		return this.roomType;
	}
	
	// setter
	
	/**
	 * Sets the room type for the applicant, if validated by the role and NRIC.
	 * Used by officers to update applicant profile after successful booking.
	 *
	 * @param roomType the new room type to assign
	 * @param nric the NRIC used for validation
	 * @param role the role of the user attempting to make the change
	 */
	public void setRoomType(int roomType, String nric, String role) {
		if (role.equals("officer") && nric.equals(this.nric)) {
			this.roomType = roomType;
		}
	}
	
	// functions
	/**
	 * Returns a list of BTO projects this applicant is eligible to apply for.
	 * Eligibility depends on marital status, age, and room availability.
	 *
	 * @param btoList the list of all BTO projects
	 * @return a filtered list of applicable BTOs
	 */
	public List<BTO> getApplicableBTOs(List<BTO> btoList) {
		return btoList.stream().filter(b -> canApplyBTO(b, (this.getMarried() ? -1 : 2))).toList();
	}
	
	/**
	 * Checks if the applicant meets criteria to apply for a specific BTO and room type.
	 *
	 * @param bto the BTO project to check
	 * @param roomType the room type requested (2 or 3)
	 * @return true if eligible, false otherwise
	 */
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
	
	/**
	 * Creates a new BTO application for this applicant, if eligible.
	 * Updates internal state with application ID and BTO ID.
	 *
	 * @param bto the BTO project to apply for
	 * @param roomType the room type requested
	 * @return the Application object created, or null if ineligible
	 */
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
	
	/**
	 * Requests to withdraw a successful or booked application.
	 * Only allowed if the current application ID matches and role is correct.
	 *
	 * @param application the application to withdraw
	 */
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
	
	/**
	 * Requests to proceed with booking a unit for a successful application.
	 * Only allowed if current application is in "successful" state.
	 *
	 * @param application the application to confirm booking for
	 */
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
	
	/**
	 * Creates a new enquiry related to a BTO project.
	 * The enquiry ID is stored internally for future reference.
	 *
	 * @param inpEnquiry the enquiry text
	 * @param btoId the BTO project this enquiry is related to
	 * @return the Enquiries object created
	 */
	public Enquiries createEnquiries(String inpEnquiry, int btoId) {
		Enquiries enquiry = new Enquiries(inpEnquiry, this.getId(), btoId);
		this.enquiriesId.add(enquiry.getId());
		System.out.println(ANSI_GREEN + "Enquiry submitted!\n" + ANSI_RESET);
		return enquiry;
	}
	
	/**
	 * Edits an existing enquiry submitted by the applicant.
	 * Only editable if the enquiry belongs to the applicant.
	 *
	 * @param enquiry the enquiry object to edit
	 * @param inpEnquiry the new enquiry content
	 */
	public void editEnquiries(Enquiries enquiry, String inpEnquiry) {
		if (this.enquiriesId.contains(enquiry.getId()) && enquiry.getEnquirierId() == this.getId()) {
			enquiry.setEnquiry(inpEnquiry);
			System.out.println(ANSI_GREEN + "Enquiry updated!\n" + ANSI_RESET);
		}
	}
	
	/**
	 * Deletes an enquiry by ID if it belongs to the applicant.
	 * Removes the ID from internal tracking.
	 *
	 * @param enquiryId the ID of the enquiry to delete
	 */
	public void deleteEnquiries(int enquiryId) {
		this.enquiriesId.remove(enquiryId);
		System.out.println(ANSI_GREEN + "Enquiry deleted!\n" + ANSI_RESET);
	}
	
	/**
	 * Prints applicant information including name, NRIC, age,
	 * marital status, and application details (if available).
	 */
	public void printApplicant() {
		System.out.println("Applicant: " + this.getName() + "\n"
				+ "NRIC: " + this.getNric() + "\n"
				+ "Age: " + this.getAge() + "\n"
				+ "Martial Status: " + (this.getMarried() ? "Married" : "Single")
				+ (this.applicationId > -1 ? "\nApplication Id: " + this.applicationId : "")
				+ (this.roomType > -1 ? "\nRoom Type: " + this.roomType + "-Room" : ""));
	}
	
	/**
	 * Displays the applicant menu and handles user interaction for:
	 * viewing BTOs, applying, checking applications, and managing enquiries.
	 *
	 * @param btoProj the project containing all BTOs
	 * @param appProj the project containing all applications
	 * @param enquiryProj the project containing all enquiries
	 */
	public void showMenu(Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj) {
		boolean run = true;
		while (run) {
			try {
				System.out.println(ANSI_CYAN + "Menu:" + ANSI_RESET);
				System.out.println("1. View available BTOs");
				System.out.println((this.getApplicationId() > -1 ? "2. Access application" : "2. Apply for BTO"));
				System.out.println("3. Submit enquiry");
				System.out.println("4. Access enquiries");
				System.out.println("5. Change Search Filters");
				System.out.println("6. Change Password");
				System.out.println("7. Logout");
			
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int menuOption = sc.nextInt();
				sc.nextLine(); // capture extra \n
				System.out.println();
				
				switch (menuOption) {
					case 1: 
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> availBTO = FilterUtil.applyUserFilters(this.getApplicableBTOs(btoProj.getItems()), this);

						
						if (availBTO.size() == 0) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						btoProj.printBTOs(availBTO);
						break;
						
					case 2: 
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						if (this.getApplicationId() == -1) { // create application
							List<BTO> applyList = this.getApplicableBTOs(btoProj.getItems());
							applyList = FilterUtil.applyUserFilters(applyList, this);
							if (applyList.size() == 0) {
								System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
								break;
							}
							btoProj.printBTOs(applyList);
							
							System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
							int inpBTOId = sc.nextInt();
							sc.nextLine();
							BTO selectBTO = btoProj.getBTOById(inpBTOId);
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
							BTO appliedBTO = btoProj.getBTOById(this.getBTOId());
							appliedBTO.printBTO();
							Application application = appProj.getAppById(this.getApplicationId());
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
						if (this.getBTOId() > -1) btoProj.getBTOById(this.getBTOId()).printBTO();
						if (btoList.size() == 0 && this.getBTOId() != -1) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						btoProj.printBTOs(btoList);
						System.out.println();
						System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
						int inpBTOId = sc.nextInt();
						sc.nextLine();
						BTO applyBTO = btoProj.getBTOById(inpBTOId);
						if (applyBTO == null) throw new InvalidInput("BTO"); // invalid capture
						
						System.out.print(ANSI_YELLOW + "Enter enquiry: " + ANSI_RESET);
						String inpEnq = sc.nextLine();
						if (inpEnq.length() <= 0) throw new InvalidInput("enquiry"); // invalid capture
						enquiryProj.addItem(this.createEnquiries(inpEnq, inpBTOId));
						break;
						
					case 4:
						System.out.println(ANSI_CYAN + "===== Enquiries =====" + ANSI_RESET);
						List<Enquiries> userEnquiry = enquiryProj.getEnquiryByUser(this.getId());
						if (userEnquiry.size() == 0) {
							System.out.println(ANSI_RED + "No exisiting enquiry\n" + ANSI_RESET);
							break;
						}
						enquiryProj.printEnquiries(userEnquiry, btoProj.getItems());
						
						System.out.print(ANSI_YELLOW + "Enter enquiry id or -1 to return: " + ANSI_RESET);
						int enquiryId = sc.nextInt();
						sc.nextLine();
						if (enquiryId == -1) break; 
						Enquiries enquiry = enquiryProj.getEnquiryById(enquiryId);
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
					case 5:
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
					case 6:
					    System.out.print(ANSI_YELLOW + "Enter new password: " + ANSI_RESET);
					    String newPwd = sc.nextLine();

					    if (newPwd.trim().isEmpty()) {
					        System.out.println(ANSI_RED + "Password cannot be empty.\n" + ANSI_RESET);
					        break;
					    }

					    this.changePassword(newPwd);
					    System.out.println(ANSI_GREEN + "Password changed successfully!\n" + ANSI_RESET);
					    break;
					case 7: return;
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