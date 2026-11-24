package services;

import contest.ContestManager;
import contest.Problem;
import contest.ProblemManager;
import models.EvaluationResult;
import models.Submission;
import models.TestCase;
import student.StudentService;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SubmissionService {

    private final StudentService studentService;
    private final ContestManager contestManager;
    private final ProblemManager problemManager;
    private final EvaluationService evaluationService;
    private final ArrayList<Submission> submissions;
    private int nextSubmissionId;

    public SubmissionService(StudentService studentService,
                             ContestManager contestManager,
                             ProblemManager problemManager,
                             EvaluationService evaluationService) {
        if (studentService == null || contestManager == null || problemManager == null
                || evaluationService == null) {
            throw new IllegalArgumentException("Submission service dependencies cannot be null.");
        }
        this.studentService = studentService;
        this.contestManager = contestManager;
        this.problemManager = problemManager;
        this.evaluationService = evaluationService;
        this.submissions = new ArrayList<>();
        this.nextSubmissionId = 1;
    }

    public Submission createSubmission(int studentId, int contestId,
                                       int problemId, String sourceCode) {
        if (studentService.getStudentById(studentId) == null) {
            throw new IllegalArgumentException("Student not found.");
        }
        if (contestManager.getContestById(contestId) == null) {
            throw new IllegalArgumentException("Contest not found.");
        }
        Problem problem = problemManager.getProblemById(problemId);
        if (problem == null) {
            throw new IllegalArgumentException("Problem not found.");
        }
        if (problem.getContestId() != contestId) {
            throw new IllegalArgumentException("Problem does not belong to the specified contest.");
        }

        Submission submission = new Submission(
                nextSubmissionId,
                studentId,
                contestId,
                problemId,
                sourceCode,
                LocalDateTime.now()
        );
        submissions.add(submission);
        nextSubmissionId++;
        return submission;
    }

    public boolean attachEvaluationResult(int submissionId, EvaluationResult result) {
        Submission submission = getSubmissionById(submissionId);
        if (submission == null || result == null) {
            return false;
        }
        submission.setEvaluationResult(result);
        return true;
    }

    public EvaluationResult evaluateSubmission(int submissionId,
                                               ArrayList<String> actualOutputs) {
        Submission submission = getSubmissionById(submissionId);
        if (submission == null) {
            throw new IllegalArgumentException("Submission not found.");
        }
        Problem problem = problemManager.getProblemById(submission.getProblemId());
        ArrayList<TestCase> testCases = problemManager.getTestCasesForProblem(problem.getProblemId());
        if (testCases.isEmpty()) {
            throw new IllegalArgumentException("Problem has no test cases.");
        }

        EvaluationResult result = evaluationService.evaluateSubmission(testCases, actualOutputs);
        submission.setEvaluationResult(result);
        return result;
    }

    public Submission getSubmissionById(int submissionId) {
        for (Submission submission : submissions) {
            if (submission.getSubmissionId() == submissionId) {
                return submission;
            }
        }
        return null;
    }

    public ArrayList<Submission> getAllSubmissions() {
        return new ArrayList<>(submissions);
    }

    public ArrayList<Submission> getSubmissionsForProblem(int contestId, int problemId) {
        ArrayList<Submission> matchingSubmissions = new ArrayList<>();
        for (Submission submission : submissions) {
            if (submission.getContestId() == contestId
                    && submission.getProblemId() == problemId) {
                matchingSubmissions.add(submission);
            }
        }
        return matchingSubmissions;
    }
}
