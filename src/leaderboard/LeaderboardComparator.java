package leaderboard;

import java.util.Comparator;
import models.LeaderboardEntry;

public class LeaderboardComparator implements Comparator<LeaderboardEntry> {

    @Override
    public int compare(LeaderboardEntry a, LeaderboardEntry b) {

        // 1. Higher score comes first
        int scoreComparison = Double.compare(b.getScore(), a.getScore());

        if (scoreComparison != 0) {
            return scoreComparison;
        }

        // 2. Lower execution time comes first
        int timeComparison = Double.compare(
                a.getExecutionTimeMs(),
                b.getExecutionTimeMs()
        );

        if (timeComparison != 0) {
            return timeComparison;
        }

        // 3. Earlier submission comes first
        return a.getSubmissionTime().compareTo(b.getSubmissionTime());
    }
}