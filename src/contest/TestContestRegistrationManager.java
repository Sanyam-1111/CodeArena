
package contest;

import java.time.LocalDateTime;

public class TestContestRegistrationManager {

    public static void main(String[] args) {

        ContestManager contestManager = new ContestManager();

        contestManager.addContest(
            "Java Coding Challenge",
            "Contest for Java programmers",
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(3)
        );

        ContestRegistrationManager registrationManager =
            new ContestRegistrationManager(contestManager);

        System.out.println("\n--- REGISTER STUDENTS ---");

        registrationManager.registerStudent(1, 101);
        registrationManager.registerStudent(1, 102);

        System.out.println("\n--- TEST DUPLICATE REGISTRATION ---");

        registrationManager.registerStudent(1, 101);

        System.out.println("\n--- VIEW ALL REGISTRATIONS ---");

        registrationManager.viewAllRegistrations();

        System.out.println("\n--- VIEW STUDENTS BY CONTEST ---");

        registrationManager.viewStudentsByContestId(1);

        System.out.println("\n--- CANCEL REGISTRATION ---");

        registrationManager.cancelRegistration(1, 101);

        System.out.println("\n--- VIEW REGISTRATIONS AFTER CANCELLATION ---");

        registrationManager.viewAllRegistrations();

        System.out.println("\n--- REGISTER AGAIN AFTER CANCELLATION ---");

        registrationManager.registerStudent(1, 101);
    }
}
