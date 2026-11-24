package leaderboard;

import java.util.Comparator;
import models.LeaderboardEntry;

public class LeaderboardComparator implements Comparator<LeaderboardEntry> {

    @Override
    public int compare(LeaderboardEntry a, LeaderboardEntry b) {
        int scoreComparison = Double.compare(b.getScore(), a.getScore());
        if (scoreComparison != 0) {
            return scoreComparison;
        }

        int timeComparison = Double.compare(
                a.getExecutionTimeMs(),
                b.getExecutionTimeMs()
        );
        if (timeComparison != 0) {
            return timeComparison;
        }

        return a.getSubmissionTime().compareTo(b.getSubmissionTime());
    }
}