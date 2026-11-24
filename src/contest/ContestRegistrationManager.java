package contest;

import java.util.ArrayList;

public class ContestRegistrationManager {

    private ArrayList<ContestRegistration> registrations;
    private ContestManager contestManager;
    private int nextRegistrationId;

    public ContestRegistrationManager(ContestManager contestManager) {
        this.registrations = new ArrayList<>();
        this.contestManager = contestManager;
        this.nextRegistrationId = 1;
    }

    public void registerStudent(int contestId, int studentId) {

        if (studentId <= 0) {
            System.out.println("Invalid Student ID.");
            return;
        }

        Contest contest = contestManager.getContestById(contestId);

        if (contest == null) {
            System.out.println("Contest not found.");
            return;
        }

        if (contest.getStatus().equalsIgnoreCase("Completed")) {
            System.out.println("Registration is not allowed for completed contests.");
            return;
        }

        for (ContestRegistration registration : registrations) {

            if (registration.getContestId() == contestId &&
                registration.getStudentId() == studentId &&
                registration.getRegistrationStatus().equalsIgnoreCase("Registered")) {

                System.out.println("Student is already registered for this contest.");
                return;
            }
        }

        ContestRegistration registration = new ContestRegistration(
            nextRegistrationId,
            contestId,
            studentId
        );

        registrations.add(registration);
        nextRegistrationId++;

        System.out.println("Student registered successfully!");
        System.out.println("Registration ID: " + registration.getRegistrationId());
    }

    public void viewAllRegistrations() {

        if (registrations.isEmpty()) {
            System.out.println("No registrations found.");
            return;
        }

        System.out.println("\n========== ALL REGISTRATIONS ==========");

        for (ContestRegistration registration : registrations) {
            registration.displayRegistration();
        }
    }

    public void viewStudentsByContestId(int contestId) {

        boolean found = false;

        System.out.println("\nStudents registered for Contest ID: " + contestId);

        for (ContestRegistration registration : registrations) {

            if (registration.getContestId() == contestId &&
                registration.getRegistrationStatus().equalsIgnoreCase("Registered")) {

                System.out.println("Student ID: " + registration.getStudentId());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No registered students found for this contest.");
        }
    }

    public void cancelRegistration(int contestId, int studentId) {

        for (ContestRegistration registration : registrations) {

            if (registration.getContestId() == contestId &&
                registration.getStudentId() == studentId &&
                registration.getRegistrationStatus().equalsIgnoreCase("Registered")) {

                registration.setRegistrationStatus("Cancelled");

                System.out.println("Registration cancelled successfully!");
                return;
            }
        }

        System.out.println("Active registration not found.");
    }

    public ContestRegistration getRegistrationById(int registrationId) {

        for (ContestRegistration registration : registrations) {

            if (registration.getRegistrationId() == registrationId) {
                return registration;
            }
        }

        return null;
    }

    public ArrayList<ContestRegistration> getAllRegistrations() {
        return registrations;
    }
}
