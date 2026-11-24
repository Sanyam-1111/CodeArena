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

        return a.getSubmissionTime().compareTo(b.getSubmissionTime());
    }
}
