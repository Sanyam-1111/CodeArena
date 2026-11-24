package leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.LeaderboardEntry;
import models.Student;
import models.Submission;
import student.StudentService;

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

    public void refreshContestLeaderboard(int contestId,
                                          List<Submission> submissions,
                                          StudentService studentService) {
        if (contestId <= 0 || submissions == null || studentService == null) {
            throw new IllegalArgumentException("Contest ID, submissions, and student service are required.");
        }

        entries.removeIf(entry -> entry.getContestId() == contestId);

        Map<Integer, Map<Integer, Submission>> bestByStudentAndProblem = new HashMap<>();
        for (Submission submission : submissions) {
            if (submission.getContestId() != contestId || submission.getEvaluationResult() == null) {
                continue;
            }

            Map<Integer, Submission> bestByProblem = bestByStudentAndProblem.computeIfAbsent(
                    submission.getStudentId(), key -> new HashMap<>());
            Submission currentBest = bestByProblem.get(submission.getProblemId());
            if (currentBest == null
                    || submission.getEvaluationResult().getScore() > currentBest.getEvaluationResult().getScore()
                    || (submission.getEvaluationResult().getScore() == currentBest.getEvaluationResult().getScore()
                        && submission.getSubmissionTime().isBefore(currentBest.getSubmissionTime()))) {
                bestByProblem.put(submission.getProblemId(), submission);
            }
        }

        for (Map.Entry<Integer, Map<Integer, Submission>> studentScores : bestByStudentAndProblem.entrySet()) {
            Student student = studentService.getStudentById(studentScores.getKey());
            if (student == null) {
                continue;
            }

            double totalScore = 0.0;
            double evaluationTimeMs = 0.0;
            java.time.LocalDateTime completionTime = null;
            for (Submission bestSubmission : studentScores.getValue().values()) {
                totalScore += bestSubmission.getEvaluationResult().getScore();
                evaluationTimeMs += bestSubmission.getEvaluationResult().getEvaluationTime() / 1_000_000.0;
                if (completionTime == null || bestSubmission.getSubmissionTime().isAfter(completionTime)) {
                    completionTime = bestSubmission.getSubmissionTime();
                }
            }

            entries.add(new LeaderboardEntry(
                    contestId,
                    student,
                    totalScore,
                    evaluationTimeMs,
                    completionTime
            ));
        }
    }

    public List<LeaderboardEntry> generateLeaderboard(int contestId) {
        List<LeaderboardEntry> contestEntries = new ArrayList<>();
        for (LeaderboardEntry entry : entries) {
            if (entry.getContestId() == contestId) {
                contestEntries.add(entry);
            }
        }
        Collections.sort(contestEntries, new LeaderboardComparator());
        return contestEntries;
    }

    public void displayContestLeaderboard(int contestId) {
        System.out.println("Contest ID: " + contestId);
        new LeaderboardDisplay().displayLeaderboard(generateLeaderboard(contestId));
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
