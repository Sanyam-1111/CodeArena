package contest;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ContestManager {

    
    private ArrayList<Contest> contests;

    
    private int nextContestId;

    
    public ContestManager() {
        contests = new ArrayList<>();
        nextContestId = 1;
    }

   
    public Contest addContest(String contestName,
                              String description,
                              LocalDateTime startTime,
                              LocalDateTime endTime) {

        
        if (contestName == null || contestName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Contest name cannot be empty."
            );
        }

      
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Contest description cannot be empty."
            );
        }

        
        validateContestDates(startTime, endTime);

        
        Contest contest = new Contest(
            nextContestId,
            contestName.trim(),
            description.trim(),
            startTime,
            endTime
        );

        
        contests.add(contest);

        nextContestId++;

        System.out.println("Contest added successfully!");
        System.out.println("Generated Contest ID: "
                           + contest.getContestId());

        return contest;
    }

    
    public void viewAllContests() {

        if (contests.isEmpty()) {
            System.out.println("No contests available.");
            return;
        }

        System.out.println("\n========== ALL CONTESTS ==========");

        for (Contest contest : contests) {
            contest.displayContest();
        }
    }

    
    public Contest searchContestById(int contestId) {

        for (Contest contest : contests) {

            if (contest.getContestId() == contestId) {
                return contest;
            }
        }

        return null;
    }

    
    public boolean updateContest(int contestId,
                                 String contestName,
                                 String description,
                                 LocalDateTime startTime,
                                 LocalDateTime endTime) {

        Contest contest = searchContestById(contestId);

        if (contest == null) {
            System.out.println("Contest not found!");
            return false;
        }

        
        if (contestName == null || contestName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Contest name cannot be empty."
            );
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Contest description cannot be empty."
            );
        }

        validateContestDates(startTime, endTime);

        
        contest.setContestName(contestName.trim());
        contest.setDescription(description.trim());
        contest.setStartTime(startTime);
        contest.setEndTime(endTime);

        System.out.println("Contest updated successfully!");

        return true;
    }

    
    public boolean deleteContest(int contestId) {

        Contest contest = searchContestById(contestId);

        if (contest == null) {
            System.out.println("Contest not found!");
            return false;
        }

        contests.remove(contest);

        System.out.println("Contest deleted successfully!");

        return true;
    }

    
    public Contest getContestById(int contestId) {
        return searchContestById(contestId);
    }

    
    public ArrayList<Contest> getAllContests() {
        return new ArrayList<>(contests);
    }

    
    private void validateContestDates(LocalDateTime startTime,
                                      LocalDateTime endTime) {

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException(
                "Contest dates cannot be null."
            );
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                "End time must be after start time."
            );
        }
    }
}

