package BTO;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class for reading and parsing CSV data into in-memory project structures.
 * 
 * Provides static methods to populate user lists and BTO project lists from corresponding CSV files.
 * 
 * Supports parsing for:
 * <ul>
 *   <li>Applicants, Officers, and Managers</li>
 *   <li>BTO projects with linked officers and manager assignments</li>
 * </ul>
 * 
 * skips headers and validates referenced users.
 * 
 * This class does not store any internal state.
 * 
 * @author Kah Teck, Keanan, Javier, Junnoske, Kevin
 */
public class CsvParser {
	//user csv header positions
	private static final int nameIdx = 0, nricIdx = 1, ageIdx = 2, marriedIdx = 3, passwordIdx = 4;
	//bto csv header positions
	private static final int projNameIdx = 0, neighborIdx = 1, t1Idx = 2, t1NumIdx = 3, t1PriceIdx = 4, t2Idx = 5, t2NumIdx = 6, t2PriceIdx = 7, appOpenIdx = 8, appCloseIdx = 9, managerIdx = 10, officerSlotsIdx = 11, officersIdx = 12;
	
	/**
	 * Parses a CSV file containing user records and adds them to the provided user project list.
	 * 
	 * Supports parsing users with roles: "applicant", "officer", and "manager".
	 * Each line (excluding header) should include user details in the format:
	 * name, nric, age, marital status, password
	 *
	 * @param filename the path to the CSV file
	 * @param userList the {@link Project} container to store parsed {@link Users}
	 * @param role the user role to assign for all parsed entries (e.g., "applicant")
	 */
    public static void parseUsersCsv(String filename, Project<Users> userList, String role) {
        File file = new File(filename);
        try (Scanner sc = new Scanner(file)) {
            int loopCount = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (loopCount++ == 0) continue; 
                String[] value = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                boolean married = !value[marriedIdx].equals("Single");

                Users newUser = switch (role.toLowerCase()) {
                    case "applicant" -> new Applicant(value[nricIdx], value[nameIdx],
                            Integer.parseInt(value[ageIdx]), value[passwordIdx], married);
                    case "officer" -> new Officer(value[nricIdx], value[nameIdx],
                            Integer.parseInt(value[ageIdx]), value[passwordIdx], married);
                    case "manager" -> new Manager(value[nricIdx], value[nameIdx],
                            Integer.parseInt(value[ageIdx]), value[passwordIdx], married);
                    default -> {
                        System.out.printf("Error: Unknown role %s\n", role);
                        yield null;
                    }
                };

                if (newUser != null) userList.addItem(newUser);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a CSV file containing BTO project details and adds them to the provided project list.
     * 
     * This method extracts BTO project attributes such as room count, dates, and manager assignment.
     * It also attempts to associate officers with the project if listed.
     * 
     * Officer names are matched by string against the user list (case-sensitive).
     * Projects with unresolved managers are skipped and reported in console.
     *
     * CSV Format (simplified, header assumed):
     * name, neighbourhood, room types, open date, close date, manager, officer limit, [officer names]
     *
     * @param filename the path to the CSV file containing project data
     * @param btoList the {@link Project} container to store parsed {@link BTO} objects
     * @param userList the {@link Project} containing all users, used for linking managers and officers
     */
    public static void parseProjectsCsv(String filename, Project<BTO> btoList, Project<Users> userList) {
        File file = new File(filename);
        try (Scanner sc = new Scanner(file)) {
            int lineNum = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (lineNum++ == 0) continue;
                String[] value = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                int num2Rooms, num3Rooms, price2Rooms, price3Rooms;
                if (value[t1Idx].equals("2")) {
                    num2Rooms = Integer.parseInt(value[t1NumIdx]);
                    num3Rooms = Integer.parseInt(value[t2NumIdx]);
                    price2Rooms = Integer.parseInt(value[t1PriceIdx]);
                    price3Rooms = Integer.parseInt(value[t2PriceIdx]);
                } else {
                    num2Rooms = Integer.parseInt(value[t2NumIdx]);
                    num3Rooms = Integer.parseInt(value[t1NumIdx]);
                    price2Rooms = Integer.parseInt(value[t2PriceIdx]);
                    price3Rooms = Integer.parseInt(value[t1PriceIdx]);
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                Date dateOpen = formatter.parse(value[appOpenIdx]);
                Date dateClose = formatter.parse(value[appCloseIdx]);
                Date now = new Date();

                Manager manager = null;
                for (Users user : userList.getItems()) {
                    if (user instanceof Manager && user.getName().equals(value[managerIdx])) {
                        manager = (Manager) user;
                        break;
                    }
                }

                if (manager == null) {
                    System.out.printf("Manager %s not found.\n", value[managerIdx]);
                    continue;
                }

                List<Officer> officers = new ArrayList<>();
                if (value.length >= 13) {
                    String[] nameArray = value[officersIdx].replaceAll("\"", "").split(",");
                    for (String name : nameArray) {
                        for (Users user : userList.getItems()) {
                            if (user instanceof Officer && user.getName().equals(name)) {
                                officers.add((Officer) user);
                            }
                        }
                    }
                }

                BTO bto = new BTO(
                        value[projNameIdx],
                        value[neighborIdx],
                        num2Rooms,
                        num3Rooms,
                        dateOpen,
                        dateClose,
                        manager,
                        Integer.parseInt(value[officerSlotsIdx]),
                        (now.compareTo(dateOpen) > 0 && now.compareTo(dateClose) <= 0)
                );
                btoList.addItem(bto);
                manager.addManagingBTO(bto.getId());
                for (Officer o : officers) {
                    o.addManagingBTO(bto.getId());
                }
                bto.setOfficers(officers, manager.getId());
            }
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
    }
}

