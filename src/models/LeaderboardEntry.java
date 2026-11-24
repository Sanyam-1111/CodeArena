package models;

import java.time.LocalDateTime;

public class LeaderboardEntry {

    private final int contestId;
    private final Student student;
    private final double score;
    private final double executionTimeMs;
    private final LocalDateTime submissionTime;

    public LeaderboardEntry(Student student, double score) {
        this(0, student, score, 0.0, LocalDateTime.now());
    }

    public LeaderboardEntry(
            Student student,
            double score,
            double executionTimeMs,
            LocalDateTime submissionTime) {
        this(0, student, score, executionTimeMs, submissionTime);
    }

    public LeaderboardEntry(
            int contestId,
            Student student,
            double score,
            double evaluationTimeMs,
            LocalDateTime submissionTime) {

        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        if (contestId < 0) {
            throw new IllegalArgumentException("Contest ID cannot be negative");
        }

        if (evaluationTimeMs < 0) {
            throw new IllegalArgumentException(
                    "Evaluation time cannot be negative");
        }

        if (submissionTime == null) {
            throw new IllegalArgumentException(
                    "Submission time cannot be null");
        }

        this.contestId = contestId;
        this.student = student;
        this.score = score;
        this.executionTimeMs = evaluationTimeMs;
        this.submissionTime = submissionTime;
    }

    public int getContestId() {
        return contestId;
    }

    public Student getStudent() {
        return student;
    }

    public double getScore() {
        return score;
    }

    public double getExecutionTimeMs() {
        return executionTimeMs;
    }

    public double getEvaluationTimeMs() {
        return executionTimeMs;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    @Override
    public String toString() {
        return "Student: " + student.getName()
                + " | Score: " + score
                + " | Evaluation Time: " + executionTimeMs + " ms"
                + " | Submission Time: " + submissionTime;
    }
}
