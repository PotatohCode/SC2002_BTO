package BTO;

import java.util.List;

public class Officer extends Applicant {

    public Officer(String nric, String password, int age, boolean married) {
        super(nric, password, age, married);
    }

    // Register a project (submit for manager approval)
    public void registerProject(BTO bto, Manager manager) {
        if (bto.getManager().equals(manager)) {
            System.out.println("Project is already managed by this manager.");
        } else {
            System.out.println("You must contact manager to assign the project.");
        }
    }

    // View all applications for a project
    public void viewApplications(BTO bto) {
        if (!bto.authOfficer(this)) {
            System.out.println("You are not assigned to this project.");
            return;
        }

        List<Application> apps = bto.getApplicationList();
        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            System.out.println("[" + i + "] Applicant NRIC: " + app.getApplicant().getNric() +
                               ", Room Type: " + app.getRoomType() +
                               ", Status: " + app.getStatus());
        }
    }

    // Approve an application
    public void approveApplication(BTO bto, int index) {
        if (!bto.authOfficer(this)) {
            System.out.println("You are not authorized for this project.");
            return;
        }

        List<Application> apps = bto.getApplicationList();
        if (index < 0 || index >= apps.size()) {
            System.out.println("Invalid application index.");
            return;
        }

        Application app = apps.get(index);
        int roomType = app.getRoomType();

        if (roomType == 2 && bto.remaining2Room() > 0 ||
            roomType == 3 && bto.remaining3Room() > 0) {
            app.setStatus("approved");
            System.out.println("Application approved.");
        } else {
            System.out.println("No available flats of this type.");
        }
    }

    // Reject an application
    public void rejectApplication(BTO bto, int index) {
        if (!bto.authOfficer(this)) {
            System.out.println("You are not authorized for this project.");
            return;
        }

        List<Application> apps = bto.getApplicationList();
        if (index < 0 || index >= apps.size()) {
            System.out.println("Invalid application index.");
            return;
        }

        apps.get(index).setStatus("rejected");
        System.out.println("Application rejected.");
    }

    // Book flat for applicant (upon approval)
    public void bookFlatForApplicant(BTO bto, int index) {
        if (!bto.authOfficer(this)) {
            System.out.println("Unauthorized officer.");
            return;
        }

        List<Application> apps = bto.getApplicationList();
        if (index < 0 || index >= apps.size()) {
            System.out.println("Invalid application index.");
            return;
        }

        Application app = apps.get(index);
        if (!app.getStatus().equals("approved")) {
            System.out.println("Application is not approved yet.");
            return;
        }

        int roomType = app.getRoomType();
        if (roomType == 2 && bto.remaining2Room() <= 0 ||
            roomType == 3 && bto.remaining3Room() <= 0) {
            System.out.println("No flats available to book.");
            return;
        }

        app.setStatus("booked");
        app.getApplicant().setHasBooked(true);

        System.out.println("Flat booked for applicant " + app.getApplicant().getNric());
    }

    // View officer's assigned BTOs
    public void viewAssignedBTOs(List<BTO> allBTOs) {
        for (BTO bto : allBTOs) {
            if (bto.authOfficer(this)) {
                System.out.println("Assigned to: " + bto.getName());
            }
        }
    }
}
