package models;

import java.time.LocalDateTime;

public class Submission {

    private final int submissionId;
    private final int studentId;
    private final int contestId;
    private final int problemId;
    private final String sourceCode;
    private final LocalDateTime submissionTime;
    private EvaluationResult evaluationResult;

    public Submission(int submissionId, int studentId, int contestId,
                      int problemId, String sourceCode,
                      LocalDateTime submissionTime) {
        if (submissionId <= 0 || studentId <= 0 || contestId <= 0 || problemId <= 0) {
            throw new IllegalArgumentException("Submission and related IDs must be positive.");
        }
        if (sourceCode == null || sourceCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Source code cannot be empty.");
        }
        if (submissionTime == null) {
            throw new IllegalArgumentException("Submission time cannot be null.");
        }

        this.submissionId = submissionId;
        this.studentId = studentId;
        this.contestId = contestId;
        this.problemId = problemId;
        this.sourceCode = sourceCode;
        this.submissionTime = submissionTime;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getContestId() {
        return contestId;
    }

    public int getProblemId() {
        return problemId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public EvaluationResult getEvaluationResult() {
        return evaluationResult;
    }

    public void setEvaluationResult(EvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }
}
