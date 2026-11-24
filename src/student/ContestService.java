package student;

import java.util.ArrayList;
import models.Contest;

public class ContestService {
    private final ArrayList<Contest> contests = new ArrayList<>();

    // Create a new contest
    public boolean createContest(int contestId, String contestName, String startTime, String endTime) {
        if (contestId <= 0) {
            System.out.println("Invalid contest ID. Must be a positive number.");
            return false;
        }
        if (contestName == null || contestName.trim().isEmpty()) {
            System.out.println("Contest name cannot be empty.");
            return false;
        }
        if (startTime == null || startTime.trim().isEmpty() || endTime == null || endTime.trim().isEmpty()) {
            System.out.println("Start time and end time cannot be empty.");
            return false;
        }
        if (getContestDetails(contestId) != null) {
            System.out.println("Contest ID " + contestId + " already exists.");
            return false;
        }

        contests.add(new Contest(contestId, contestName.trim(), startTime.trim(), endTime.trim()));
        System.out.println("Created contest: " + contestName.trim());
        return true;
    }

    // Get all contests
    public ArrayList<Contest> getAllContests() {
        return new ArrayList<>(contests);
    }

    // Find one contest by ID
    public Contest getContestDetails(int contestId) {
        for (Contest c : contests) {
            if (c.getContestId() == contestId) {
                return c;
            }
        }
        return null;
    }

    // Search contests by name keyword (case-insensitive)
    public ArrayList<Contest> searchContestsByName(String keyword) {
        ArrayList<Contest> matches = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return matches;
        }
        String query = keyword.trim().toLowerCase();
        for (Contest c : contests) {
            if (c.getContestName().toLowerCase().contains(query)) {
                matches.add(c);
            }
        }
        return matches;
    }

    // Get total count of contests
    public int getTotalContests() {
        return contests.size();
    }

    // Update contest details
    public boolean updateContest(int contestId, String newName, String newStart, String newEnd) {
        Contest c = getContestDetails(contestId);
        if (c == null) {
            System.out.println("Contest with ID " + contestId + " not found.");
            return false;
        }
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println("Contest name cannot be empty.");
            return false;
        }
        if (newStart == null || newStart.trim().isEmpty() || newEnd == null || newEnd.trim().isEmpty()) {
            System.out.println("Start time and end time cannot be empty.");
            return false;
        }

        c.setContestName(newName.trim());
        c.setStartTime(newStart.trim());
        c.setEndTime(newEnd.trim());
        System.out.println("Updated contest ID " + contestId);
        return true;
    }

    // Delete contest by ID
    public boolean deleteContest(int contestId) {
        Contest c = getContestDetails(contestId);
        if (c == null) {
            System.out.println("Contest with ID " + contestId + " not found.");
            return false;
        }
        contests.remove(c);
        System.out.println("Deleted contest ID " + contestId);
        return true;
    }
}