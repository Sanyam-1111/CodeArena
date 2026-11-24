package leaderboard;

import java.util.List;
import models.LeaderboardEntry;

public class LeaderboardDisplay {

    public void displayLeaderboard(List<LeaderboardEntry> entries) {

        if (entries == null || entries.isEmpty()) {
            System.out.println("Leaderboard is currently empty.");
            return;
        }

        System.out.println("\n==============================================================");
        System.out.println("                    CODERANK LEADERBOARD");
        System.out.println("==============================================================");

        System.out.printf(
                "%-8s %-22s %-12s %-18s %-22s%n",
                "RANK", "STUDENT", "SCORE", "EVALUATION TIME", "SUBMISSION TIME"
        );

        System.out.println("--------------------------------------------------------------");

        int rank = 1;

        for (LeaderboardEntry entry : entries) {

            System.out.printf(
                    "%-8d %-22s %-12.2f %-18.2f %-22s%n",
                    rank,
                    entry.getStudent().getName(),
                    entry.getScore(),
                    entry.getEvaluationTimeMs(),
                    entry.getSubmissionTime()
            );

            rank++;
        }

        System.out.println("==============================================================\n");
    }

    public void displayTopStudents(List<LeaderboardEntry> entries, int limit) {

        if (entries == null || entries.isEmpty()) {
            System.out.println("No leaderboard records available.");
            return;
        }

        if (limit <= 0) {
            throw new IllegalArgumentException(
                    "Limit must be greater than zero."
            );
        }

        System.out.println("\n========== TOP " + limit + " STUDENTS ==========");

        int count = Math.min(limit, entries.size());

        for (int i = 0; i < count; i++) {

            LeaderboardEntry entry = entries.get(i);

            System.out.println(
                    (i + 1) + ". "
                    + entry.getStudent().getName()
                    + " | Score: " + entry.getScore()
            );
        }

        System.out.println("====================================\n");
    }
}
