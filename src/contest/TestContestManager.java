package contest;

import java.time.LocalDateTime;

public class TestContestManager {

    public static void main(String[] args) {

        ContestManager manager = new ContestManager();

        // 1. ADD CONTESTS
        System.out.println("\n--- ADDING CONTESTS ---");

        manager.addContest(
            "Java Coding Challenge",
            "Contest for Java programmers",
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(3)
        );

        manager.addContest(
            "DSA Championship",
            "Data Structures and Algorithms contest",
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2)
        );

        // 2. VIEW ALL CONTESTS
        System.out.println("\n--- VIEW ALL CONTESTS ---");
        manager.viewAllContests();

        // 3. SEARCH CONTEST
        System.out.println("\n--- SEARCH CONTEST ---");

        Contest c = manager.searchContestById(1);

        if (c != null) {
            c.displayContest();
        } else {
            System.out.println("Contest not found!");
        }

        // 4. UPDATE CONTEST
        System.out.println("\n--- UPDATE CONTEST ---");

        manager.updateContest(
            1,
            "Advanced Java Challenge",
            "Advanced Java programming competition",
            LocalDateTime.now().plusHours(2),
            LocalDateTime.now().plusHours(5)
        );

        // 5. VIEW UPDATED CONTEST
        System.out.println("\n--- UPDATED CONTEST ---");

        Contest updated = manager.getContestById(1);

        if (updated != null) {
            updated.displayContest();
        }

        // 6. DELETE CONTEST
        System.out.println("\n--- DELETE CONTEST ---");

        manager.deleteContest(2);

        // 7. VIEW FINAL CONTEST LIST
        System.out.println("\n--- FINAL CONTEST LIST ---");

        manager.viewAllContests();

        // 8. TEST INVALID DATE VALIDATION
        System.out.println("\n--- TEST INVALID DATES ---");

        try {
            manager.addContest(
                "Invalid Contest",
                "Testing date validation",
                LocalDateTime.now().plusHours(5),
                LocalDateTime.now().plusHours(2)
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Validation successful: "
                               + e.getMessage());
        }
    }
}
