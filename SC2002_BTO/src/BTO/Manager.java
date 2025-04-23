package BTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a Manager in the BTO system.
 * 
 * A Manager is responsible for overseeing BTO project creation and maintenance.
 * They are authorised to create, edit, and delete BTO projects, view applicant and officer applications,
 * manage application statuses, and respond to user enquiries.
 * 
 * This class extends {@link Users} and implements the {@link Admin} interface, inheriting user authentication,
 * search filters, and BTO administrative functionalities.
 * 
 * Key Responsibilities:
 * <ul>
 *   <li>Create and edit BTO project details (room counts, dates, visibility, etc.)</li>
 *   <li>Manage applicant and officer applications tied to their BTOs</li>
 *   <li>Respond to user enquiries</li>
 *   <li>Ensure no application date overlaps between BTOs they manage</li>
 * </ul>
 * 
 * The list of BTO projects managed by a Manager is tracked via their {@code managingId} list.
 * Each Manager is identified uniquely and automatically assigned a role of "manager".
 * 
 * @see Users
 * @see Admin
 * @see BTO
 * @see Application
 * @see Enquiries
 * 
 * @version 23/04/2025
 * @author Kah Teck,
 *         Keanan,
 *         Javier,
 *         Junnoske,
 *         Kevin
 */
public class Manager extends Users implements Admin {
	
	private List<Integer> managingId = new ArrayList<>();
	private Scanner sc;
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * Constructs a new Manager user in the BTO system.
	 * Initialises with the "manager" role and sets up internal state.
	 *
	 * @param nric     the NRIC of the manager
	 * @param name     the name of the manager
	 * @param age      the age of the manager
	 * @param password the login password
	 * @param married  marital status
	 */
	public Manager(String nric, String name, int age, String password, boolean married) {
		super(nric, name, age, password, married, "manager");
		sc = new Scanner(System.in);
	}
	
	// getter

	/**
	 * Returns the list of BTO project IDs that the manager is managing.
	 *
	 * @return list of managed BTO IDs
	 */
	public List<Integer> getManaging() {
		return this.managingId;
	}
	
	// setter
	/**
	 * Adds a BTO project to the manager's managed list.
	 *
	 * @param btoId the BTO project ID to add
	 */
	public void addManagingBTO(int btoId) {
		this.managingId.add(btoId);
	}
	
	// functions
	// view all BTO
	
	/**
	 * Displays a list of BTOs, filtered to show either only those managed by this user or all.
	 *
	 * @param btoList the full list of BTOs
	 * @param own     true to show only managed BTOs, false for all
	 */
	public void viewBTOs(List<BTO> btoList, boolean own) {
		int count = 1;
		for (BTO bto : btoList) {
			if (own) {
				if (bto.getManager().getId() == this.getId()) {
					System.out.println(count++ + ". " + bto.getName() + " at " + bto.getNeighbourhood() + " managed by " + bto.getManager().getName());
				}
			} else {
				System.out.println(count++ + ". " + bto.getName() + " at " + bto.getNeighbourhood() + " managed by " + bto.getManager().getName());
			}
		}
	}
	
	// create BTO project
	/**
	 * Creates a new BTO project via manager input.
	 * Validates date range and optional reassignment to another manager.
	 *
	 * @param userProj     the project containing all users
	 * @param managingBTO  current list of BTOs managed by this user
	 * @return the new {@link BTO} project object
	 * @throws ParseException if the input date format is invalid
	 * @throws InputMismatchException for scanner input mismatches
	 * @throws InvalidInput if any field is invalid (e.g. manager not found, invalid dates)
	 */
	public BTO createBTO(Project<Users> userProj, List<BTO> managingBTO) throws ParseException, InputMismatchException, InvalidInput  {
			Date applicationStart, applicationEnd, now = new Date();
			System.out.print("Enter BTO name: ");
			String name = sc.nextLine();
			System.out.print("Enter BTO neighbourhood: ");
			String neighbourhood = sc.nextLine();
			System.out.print("Enter selling price: ");
			double price = sc.nextDouble();
			sc.nextLine();
			System.out.print("Enter number of 2 rooms: ");
			int num2Rooms = sc.nextInt();
			sc.nextLine();
			System.out.print("Enter number of 3 rooms: ");
			int num3Rooms = sc.nextInt();
			sc.nextLine();
			
			System.out.print("Enter application start date (dd/MM/yyyy): ");
			String start = sc.nextLine();
			applicationStart = df.parse(start);
			
			System.out.print("Enter application end date (dd/MM/yyyy): ");
			String end = sc.nextLine();
			applicationEnd = df.parse(end);
			
			if (applicationEnd.compareTo(applicationStart) < 0) throw new InvalidInput("dates");
				
			System.out.print("Enter manager NRIC (leave blank if you are manager): ");
			String managerNric = sc.nextLine();
			if (managerNric.length() > 0) {
				Users u = userProj.getApplicantByNric(managerNric);
				Manager manager = null;
				if (u.getRole() == "manager") manager = (Manager) u;
				if (manager == null) throw new InvalidInput("manager");
			}
			
			System.out.print("Enter max officers: ");
			int maxOfficer = sc.nextInt();
			sc.nextLine();
			
			BTO newBTO = new BTO(name, neighbourhood, price, num2Rooms, num3Rooms, applicationStart, applicationEnd, this, maxOfficer, (now.compareTo(applicationStart) > 0 && now.compareTo(applicationStart) <= 0));
			return newBTO;
		
	}
	
	// edit BTO project
	/**
	 * Allows manager to edit fields of an existing BTO project.
	 * Includes validations for ownership and input constraints.
	 *
	 * @param bto the BTO project to edit
	 * @throws InputMismatchException for scanner mismatches
	 * @throws InvalidInput if invalid fields or access violations occur
	 */
	public void editBTO(BTO bto) throws InputMismatchException, InvalidInput {
		if (managingId.contains(bto.getId()) && bto.getManager().getId() == this.getId()) {
			System.out.println("Which field would you like to edit?\n"
								+ "1. Name: " + bto.getName()
								+ "\n2. Neighbourhood: " + bto.getNeighbourhood()
								+ "\n3. Selling price: $" + bto.getPrice()
								+ "\n4. No. of 2 Rooms: " + bto.getNum2Rooms()
								+ "\n5. No. of 3 Rooms: " + bto.getNum3Rooms()
								+ "\n6. Application Start Date: " + df.format(bto.getApplicationStart())
								+ "\n7. Application End Date: " + df.format(bto.getApplicationEnd())
								+ "\n8. No. of Officers: " + bto.getMaxOfficer()
								+ "\n9. Visibility: " + bto.getVisible()
								+ "\n10. Return to menu");
			System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
			int option = sc.nextInt();
		
			try {
				switch (option) {
					case 1: 
						System.out.print("Enter new name: ");
						String name = sc.nextLine();
						bto.setName(name, this.getId());
						break;
					case 2:
						System.out.print("Enter new neighbourhood: ");
						String neighbourhood = sc.nextLine();
						bto.setNeighbourhood(neighbourhood, this.getId());
						break;
					case 3:
						System.out.print("Enter selling price: ");
						double price = sc.nextDouble();
						bto.setPrice(price, this.getId());
						break;
					case 4:
						System.out.print("Enter new no of 2 rooms: ");
						int num2 = sc.nextInt();
						bto.setNum2Rooms(num2, this.getId());
						break;
					case 5:
						System.out.print("Enter new no of 3 rooms: ");
						int num3 = sc.nextInt();
						bto.setNum3Rooms(num3, this.getId());
						break;
					case 6:
						System.out.print("Enter new start date (dd/MM/yyyy): ");
						String startDate = sc.nextLine();
						Date applicationStart = df.parse(startDate);
						if (bto.getApplicationEnd().compareTo(applicationStart) < 0) throw new InvalidInput("dates");
						bto.setApplicationStart(df.parse(startDate), this.getId());
						break;
					case 7:
						System.out.print("Enter new end date (dd/MM/yyyy): ");
						String endDate = sc.nextLine();
						Date applicationEnd = df.parse(endDate);
						if (applicationEnd.compareTo(bto.getApplicationStart()) < 0) throw new InvalidInput("dates");
						bto.setApplicationEnd(df.parse(endDate), this.getId());
						break;
					case 8:
						System.out.print("Enter new max no. of officers: ");
						int num = sc.nextInt();
						bto.setMaxOfficer(num, this.getId());
						break;
					case 9:
						bto.toggleVisible(this.getId());
						break;
					case 10:
						break;
					default: throw new InvalidInput("options");
				}
				System.out.println(ANSI_GREEN + "BTO edited!\n" + ANSI_RESET);
			} catch (ParseException pe) {
				System.out.println(ANSI_RED + "Date format is wrong\n" + ANSI_RESET);
			}
		}
	}
	
	// delete BTO
	/**
	 * Removes a BTO from the manager’s list of managed projects.
	 * Removes BTO from application list, enquiry list and removes application from users and remove bto from officer manageing
	 *
	 * @param btoId the ID of the BTO to remove
	 * @param btoProj     the project containing all BTOs
	 * @param appProj     the project containing all applications
	 * @param enquiryProj the project containing all enquiries
	 * @param userProj    the project containing all users
	 */
	public void deleteBTO(BTO bto, Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj, Project<Users> userProj) {
		List<Application> appList = new ArrayList<>();
		appList.addAll(appProj.getAppByBTO(bto.getId(), "bto", null));
		appList.addAll(appProj.getAppByBTO(bto.getId(), "officer", null));
		for (Application a : appList) {
			appProj.removeItem(a);
		}
		
		List<Enquiries> enqList = enquiryProj.getEnquiryByBTO(bto.getId());
		for (Enquiries e : enqList) {
			enquiryProj.removeItem(e);
		}
		
		List<Applicant> aList = userProj.getApplicantByBTO(bto.getId());
		for (Applicant u : aList) {
			u.resetApplication();
		}
		
		List<Officer> oList = userProj.getOfficerByBTO(bto.getId());
		for (Officer u : oList) {
			u.removeManagingBTO(bto.getId());
		}
		
		btoProj.removeItem(bto);
		System.out.println(ANSI_GREEN + "BTO deleted!\n" + ANSI_RESET);
	}
	
	/**
	 * Displays the main menu for the manager and handles all role-specific workflows.
	 * Includes creating, editing, viewing BTOs, replying to enquiries, and managing applications.
	 *
	 * @param btoProj     the project containing all BTOs
	 * @param appProj     the project containing all applications
	 * @param enquiryProj the project containing all enquiries
	 * @param userProj    the project containing all users
	 */
	public void showMenu(Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj, Project<Users> userProj) {
		boolean run = true;
		while (run) {
			try {
				System.out.println(ANSI_CYAN + "Menu:" + ANSI_RESET);
				System.out.println("1. View all BTOs");
				System.out.println("2. Access your BTOs");
				System.out.println("3. Create BTO");
				System.out.println("4. View all applicant bookings");
				System.out.println("5. View all enquiries");
				System.out.println("6. View officers");
				System.out.println("7. Logout");
				
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int menuOption = sc.nextInt();
				sc.nextLine(); // capture extra \n
				System.out.println();

				switch(menuOption) {
					case 1:
						System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
						List<BTO> allBTOs = btoProj.getItems();
						if (allBTOs.size() <= 0) {
							System.out.println(ANSI_RED + "No BTO available\n" + ANSI_RESET);
							break;
						}
						btoProj.printBTOs(allBTOs, true);
						break;
					case 2:
						managingBTO(this.getManaging(), btoProj, appProj, enquiryProj, userProj);
						break;
						
					case 3:
						List<BTO> managingBTO = btoProj.getItems().stream().filter(b -> this.managingId.contains(b.getId())).toList();
						BTO newBTO = createBTO(userProj, managingBTO);
						if (!clashApplication(newBTO, managingBTO)) {
							this.managingId.add(newBTO.getId());
							btoProj.addItem(newBTO);
							System.out.println(ANSI_GREEN + "BTO created!\n" + ANSI_RESET);
						} else {
							System.out.println(ANSI_RED + "This project timeline clashes with another project\n" + ANSI_RESET);
						}
						break;
						
					case 4:
						List<Application> bookApp = new ArrayList<>();
						for (int btoId : this.getManaging()) {
							bookApp.addAll(appProj.getAppByBTO(btoId, "bto", "booked"));
						}
						if (bookApp.size() <= 0) {
							System.out.println(ANSI_RED + "No bookings\n" + ANSI_RESET);
							break;
						}
						for (Application app : bookApp) {
							System.out.println("Project Name: " + appProj.getBTOById(app.getBTOId()).getName());
							app.printApplication();
							Applicant applicant = (Applicant) app.getApplicant();
							applicant.printApplicant();
							System.out.println();
						}
						break;
					
					case 5: 
						if (enquiryProj.getCount() > 0) {
							enquiryProj.printEnquiries(enquiryProj.getItems(), btoProj.getItems());
						} else {
							System.out.println(ANSI_RED + "No enquiries\n" + ANSI_RESET);
						}
						break;
					
					case 6:
						List<BTO> managingBTO2 = btoProj.getItems().stream().filter(b -> this.managingId.contains(b.getId())).toList();
						if (managingBTO2.size() <= 0) {
							System.out.println(ANSI_RED + "No BTOs\n" + ANSI_RESET);
						}
						for (BTO bto : managingBTO2) {
							if (bto.getOfficers().size() > 0) {
								System.out.print("BTO Name: " + bto.getName() + " Officers: ");
								for (Officer o : bto.getOfficers()) {
									System.out.print(o.getName() + " ");
								}
								System.out.println("\n");
							} else {
								System.out.println("BTO Name: " + bto.getName() + " Officers: No Officers");
							}
						}
						break;
					case 7:
						return;
					default: throw new InvalidInput("option");
				}
			} catch (ParseException pe) {
				System.out.println(ANSI_RED + "Date format is wrong!\n" + ANSI_RESET);
				sc.nextLine();
			} catch (InputMismatchException ime) {
				System.out.println(ANSI_RED + "Invalid input!\n" + ANSI_RESET);
				sc.nextLine();
			} catch (InvalidInput ii) {
				System.out.println(ANSI_RED + ii.getMessage() + "\n" + ANSI_RESET);
			}
		}
	}
	
	/**
	 * Handles the "Manage BTO" menu for a manager.
	 * Enables access to applications, officer requests, enquiry replies, and BTO settings.
	 *
	 * @param managingId  list of BTO project IDs managed by this user
	 * @param btoProj     the BTO data project
	 * @param appProj     the application data project
	 * @param enquiryProj the enquiry data project
	 * @param userProj	  the user data project
	 * @throws InputMismatchException for scanner issues
	 * @throws InvalidInput for invalid option selection or access
	 */
	@Override
	public void managingBTO(List<Integer> managingId, Project<BTO> btoProj, Project<Application> appProj, Project<Enquiries> enquiryProj, Project<Users> userProj) throws InputMismatchException, InvalidInput {
		List<BTO> managingBTO = btoProj.getItems().stream().filter(b -> managingId.contains(b.getId())).toList();
		System.out.println(ANSI_CYAN + "===== BTOs =====" + ANSI_RESET);
		if (managingBTO.size() <= 0) {
			System.out.println(ANSI_RED + "No managing BTO available\n" + ANSI_RESET);
			return;
		}
		btoProj.printBTOs(managingBTO);
		
		System.out.println("What would you like to do?\n"
							+ "1. Access enquries\n"
							+ "2. Access applicant applications\n"
							+ "3. Access officer applications\n"
							+ "4. Toggle visibility\n"
							+ "5. Edit BTO details\n"
							+ "6. Delete BTO\n"
							+ "7. Return to menu");
		
		System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
		int manageMenu = sc.nextInt();
		sc.nextLine();
		
		if (manageMenu == 7) return;
		if (manageMenu < 1 && manageMenu > 3) throw new InvalidInput("option");
		
		System.out.print(ANSI_YELLOW + "Enter BTO id: " + ANSI_RESET);
		int inpBTOId = sc.nextInt();
		sc.nextLine();
		if (!managingId.contains(inpBTOId)) throw new InvalidInput("BTO");
		BTO bto = btoProj.getBTOById(inpBTOId);
		if (bto == null) throw new InvalidInput("BTO");
		System.out.println();
		
		switch (manageMenu) {
			case 1:
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
				int enqMenu = sc.nextInt();
				sc.nextLine();
				if (enqMenu == 2) return;
				if (enqMenu < 1 && enqMenu > 2) throw new InvalidInput("option");
				replyEnquiry(this.getId(), this.getRole(), enquiryProj, sc);
				break;
				
			case 2:
				System.out.println(ANSI_CYAN + "===== Applicant Applications =====" + ANSI_RESET);
				List<Application> appApp = appProj.getAppByBTO(inpBTOId, "bto", null);
				if (appApp.size() <= 0) {
					System.out.println(ANSI_RED + "No applications\n" + ANSI_RESET);
					return;
				}
				appProj.printBTOApp(appApp, btoProj.getItems());
				
				System.out.println("What would you like to do?\n"
						+ "1. Accept application\n"
						+ "2. Reject application\n"
						+ "3. Return to menu");
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int appMenu = sc.nextInt();
				sc.nextLine();
				if (appMenu == 3) return;
				if (appMenu < 1 && appMenu > 3) throw new InvalidInput("option");
				System.out.print("Enter application id: ");
				int selAppId = sc.nextInt();
				sc.nextLine();
				Application app = appProj.getAppById(selAppId);
				if (app == null || !appApp.contains(app)) throw new InvalidInput("application");
				
				updateAppStatus(app, appMenu, bto);
				break;
			
			case 3:
				System.out.println(ANSI_CYAN + "===== Officer Applications =====" + ANSI_RESET);
				List<Application> offApps = appProj.getAppByBTO(inpBTOId, "officer", null);
				if (offApps.size() <= 0) {
					System.out.println(ANSI_RED + "No applications\n" + ANSI_RESET);
					return;
				}
				appProj.printBTOApp(offApps, btoProj.getItems());
				
				System.out.println("What would you like to do?\n"
						+ "1. Accept application\n"
						+ "2. Reject application\n"
						+ "3. Return to menu");
				System.out.print(ANSI_YELLOW + "Enter option: " + ANSI_RESET);
				int offMenu = sc.nextInt();
				sc.nextLine();
				if (offMenu == 3) return;
				if (offMenu < 1 && offMenu > 3) throw new InvalidInput("option");
				System.out.print(ANSI_YELLOW + "Enter application id: " + ANSI_RESET);
				int selAppId2 = sc.nextInt();
				sc.nextLine();
				Application offApp = appProj.getAppById(selAppId2);
				if (offApp == null || !offApps.contains(offApp)) throw new InvalidInput("application");
				
				updateAppStatus(offApp, offMenu, bto);
				break;
			
			case 4:
				bto.toggleVisible(this.getId());
				System.out.println(ANSI_GREEN + "BTO is now " + (bto.getVisible() ? "Visible" : "Not Visible" + ANSI_RESET));
				break;
				
			case 5: editBTO(bto);
				break;
			
			case 6: deleteBTO(bto, btoProj, appProj, enquiryProj, userProj);
				break;
			
			case 7: break;
			
			default: throw new InvalidInput("option");
		}
		
	}
	
	/**
	 * Updates the status of an application (officer or applicant) based on manager decisions.
	 * Handles authentication, availability of rooms, and success conditions.
	 *
	 * @param app the application to update
	 * @param status 1 to accept, 2 to reject
	 * @param bto the BTO project the application is tied to
	 */
	public void updateAppStatus(Application app, int status, BTO bto) {
		if (app.getType().equals("officer")) {
			if (bto.getNumOfficer() >= bto.getMaxOfficer()) {
				System.out.println(ANSI_RED + "Insufficient slots available\n" + ANSI_RESET);
				return;
			}
			if (app.getApplicant() instanceof Officer) {
				Officer o = (Officer) app.getApplicant();
				bto.addOfficer(o);
			} else {
				System.out.println(ANSI_RED + "Something went wrong\n" + ANSI_RESET);
				return;
			}
		} else {
			Applicant applicant = (Applicant) app.getApplicant();
			if (app.getStatus().equals("booking") || app.getStatus().equals("booked")) {
				System.out.println(ANSI_RED + "Flat has already been booked\n" + ANSI_RESET);
				return;
			}
			if (app.getStatus().equals("withdrawing")) {
				app.setStatus(status == 1 ? "withdrawn" : "pending", this.getRole());
				if (status == 1) applicant.resetApplication();
				System.out.println(ANSI_GREEN + "Application updated!\n" + ANSI_RESET);
				return;
			}
			if (app.getRoomType() == 2 && bto.getNum2Rooms() <= 0) {
				System.out.println(ANSI_RED + "Insufficient rooms available\n" + ANSI_RESET);
				return;
			} else if (app.getRoomType() == 3 && bto.getNum3Rooms() <= 0) {
				System.out.println(ANSI_RED + "Insufficient rooms available\n" + ANSI_RESET);
				return;
			}
			if (app.getStatus().equals("pending")) {
				if (status == 1) {
					applicant.setApplicationId(app.getId());
					applicant.setBTOId(app.getBTOId());
				} else {
					applicant.resetApplication();
				}
			}
		}
		app.setStatus(status == 1 ? "successful" : "unsuccessful", this.getRole());
		System.out.println(ANSI_GREEN + "Application updated!\n" + ANSI_RESET);
	}
	
}
