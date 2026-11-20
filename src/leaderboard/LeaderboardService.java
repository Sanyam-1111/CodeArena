package leaderboard;

import java.util.ArrayList;
import java.util.List;
import models.LeaderboardEntry;

public class LeaderboardService {
    private final List<LeaderboardEntry> entries = new ArrayList<>();

    public void addEntry(LeaderboardEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Entry cannot be null");
        }
        entries.add(entry);
    }

    public List<LeaderboardEntry> generateLeaderboard() {
        List<LeaderboardEntry> sortedEntries = new ArrayList<>(entries);
        sortedEntries.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        return sortedEntries;
    }
}
