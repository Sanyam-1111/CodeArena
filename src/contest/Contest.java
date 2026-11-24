package contest;

import java.time.LocalDateTime;

public class Contest {

    private int contestId;
    private String contestName;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Constructor
    public Contest(int contestId, String contestName,
                   String description,
                   LocalDateTime startTime,
                   LocalDateTime endTime) {

        this.contestId = contestId;
        this.contestName = contestName;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public int getContestId() {
        return contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Setters
    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    // Automatically calculate contest status
    public String getStatus() {

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(startTime)) {
            return "Upcoming";
        } 
        else if (now.isBefore(endTime)) {
            return "Ongoing";
        } 
        else {
            return "Completed";
        }
    }

    // Display contest details
    public void displayContest() {

        System.out.println("\n========== CONTEST DETAILS ==========");
        System.out.println("Contest ID   : " + contestId);
        System.out.println("Contest Name : " + contestName);
        System.out.println("Description  : " + description);
        System.out.println("Start Time   : " + startTime);
        System.out.println("End Time     : " + endTime);
        System.out.println("Status       : " + getStatus());
        System.out.println("=====================================");
    }
}
