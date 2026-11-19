// package student;

// import models.Contest;
// import java.util.ArrayList;

// public class ContestService {
//     private ArrayList<Contest> contests = new ArrayList<>();

//     // Create a new contest
//     public void createContest(int contestId, String contestName, String startTime, String endTime) {
//         if (getContestDetails(contestId) != null) {
//             System.out.println("Contest ID " + contestId + " already exists.");
//             return;
//         }
//         contests.add(new Contest(contestId, contestName, startTime, endTime));
//         System.out.println("Created contest: " + contestName);
//     }

//     // Get all contests
//     public ArrayList<Contest> getAllContests() {
//         return contests;
//     }

//     // Find one contest by ID
//     public Contest getContestDetails(int contestId) {
//         for (Contest c : contests) {
//             if (c.getContestId() == contestId) {
//                 return c;
//             }
//         }
//         return null;
//     }

//     // Update contest details
//     public boolean updateContest(int contestId, String newName, String newStart, String newEnd) {
//         Contest c = getContestDetails(contestId);
//         if (c == null) {
//             System.out.println("Contest not found.");
//             return false;
//         }
//         c.setContestName(newName);
//         c.setStartTime(newStart);
//         c.setEndTime(newEnd);
//         return true;
//     }

//     // Delete contest by ID
//     public boolean deleteContest(int contestId) {
//         Contest c = getContestDetails(contestId);
//         if (c == null) {
//             System.out.println("Contest not found.");
//             return false;
//         }
//         contests.remove(c);
//         return true;
//     }
// }