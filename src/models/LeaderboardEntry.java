package models;

import java.time.LocalDateTime;

public class LeaderboardEntry {

    private final Student student;
    private final double score;
    private final double executionTimeMs;
    private final LocalDateTime submissionTime;

    public LeaderboardEntry(Student student, double score) {
        this(student, score, 0.0, LocalDateTime.now());
    }

    public LeaderboardEntry(
            Student student,
            double score,
            double executionTimeMs,
            LocalDateTime submissionTime) {

        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        if (executionTimeMs < 0) {
            throw new IllegalArgumentException(
                    "Execution time cannot be negative");
        }

        if (submissionTime == null) {
            throw new IllegalArgumentException(
                    "Submission time cannot be null");
        }

        this.student = student;
        this.score = score;
        this.executionTimeMs = executionTimeMs;
        this.submissionTime = submissionTime;
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

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    @Override
    public String toString() {
        return "Student: " + student.getName()
                + " | Score: " + score
                + " | Execution Time: " + executionTimeMs + " ms"
                + " | Submission Time: " + submissionTime;
    }
}