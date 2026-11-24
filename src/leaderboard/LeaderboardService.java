package leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.LeaderboardEntry;

public class LeaderboardService {

    private final List<LeaderboardEntry> entries;

    public LeaderboardService() {
        entries = new ArrayList<>();
    }

    public void addEntry(LeaderboardEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Entry cannot be null.");
        }
        entries.add(entry);
    }

    public List<LeaderboardEntry> generateLeaderboard() {
        List<LeaderboardEntry> sortedEntries = new ArrayList<>(entries);
        Collections.sort(sortedEntries, new LeaderboardComparator());
        return sortedEntries;
    }

    public List<LeaderboardEntry> getTopStudents(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than zero.");
        }

        List<LeaderboardEntry> sortedEntries = generateLeaderboard();
        int count = Math.min(limit, sortedEntries.size());
        return new ArrayList<>(sortedEntries.subList(0, count));
    }

    public void displayLeaderboard() {

        LeaderboardDisplay display = new LeaderboardDisplay();

        display.displayLeaderboard(generateLeaderboard());
    }

    public void displayTopStudents(int limit) {

        LeaderboardDisplay display = new LeaderboardDisplay();

        display.displayTopStudents(
                getTopStudents(limit),
                limit
        );
    }

    public int getTotalEntries() {
        return entries.size();
    }

    public void clearLeaderboard() {
        entries.clear();
    }
}