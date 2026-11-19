package models;

public class Contest {
    private int contestId;
    private String contestName;
    private String startTime;
    private String endTime;

    public Contest(int contestId, String contestName, String startTime, String endTime) {
        this.contestId = contestId;
        this.contestName = contestName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Contest [ID=" + contestId + ", Name=" + contestName
                + ", Start=" + startTime + ", End=" + endTime + "]";
    }
}