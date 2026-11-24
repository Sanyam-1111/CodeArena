package contest;

import java.time.LocalDateTime;

public class TestContest {

    public static void main(String[] args) {

        Contest c1 = new Contest(
            101,
            "Java Coding Challenge",
            "A contest for Java programmers",
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(3)
        );

        c1.displayContest();

        // Updating contest name
        c1.setContestName("Advanced Java Challenge");

        System.out.println("\nAfter Updating Contest Name:");

        c1.displayContest();
    }
}

