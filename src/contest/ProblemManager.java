package contest;

import models.TestCase;

import java.util.ArrayList;

public class ProblemManager {

    // ArrayList to store problems
    private ArrayList<Problem> problems;

    // Automatically generate unique problem IDs
    private int nextProblemId;
    private int nextTestCaseId;
    private ContestManager contestManager;

    // Constructor
    public ProblemManager(ContestManager contestManager) {
        problems = new ArrayList<>();
        nextProblemId = 1;
        nextTestCaseId = 1;
        this.contestManager = contestManager;
    }

    // 1. Add a new problem
    public Problem addProblem(int contestId, String title,
                              String description, String difficulty,
                              int maxScore, String sampleInput,
                              String sampleOutput) {

        if (contestManager.getContestById(contestId) == null) {
            throw new IllegalArgumentException("Contest not found.");
        }

        Problem problem = new Problem(
            nextProblemId,
            contestId,
            title,
            description,
            difficulty,
            maxScore,
            sampleInput,
            sampleOutput
        );

        problems.add(problem);
        nextProblemId++;

        System.out.println("Problem added successfully!");
        System.out.println("Generated Problem ID: "
                           + problem.getProblemId());

        return problem;
    }

    public TestCase addTestCase(int problemId, String input,
                                String expectedOutput, double marks) {
        Problem problem = getProblemById(problemId);
        if (problem == null) {
            throw new IllegalArgumentException("Problem not found.");
        }
        if (input == null || expectedOutput == null || marks <= 0) {
            throw new IllegalArgumentException("Test case input, expected output, and positive marks are required.");
        }

        TestCase testCase = new TestCase(nextTestCaseId, input, expectedOutput, marks);
        problem.addTestCase(testCase);
        nextTestCaseId++;
        return testCase;
    }

    public ArrayList<TestCase> getTestCasesForProblem(int problemId) {
        Problem problem = getProblemById(problemId);
        return problem == null ? new ArrayList<>() : problem.getTestCases();
    }

    // 2. View all problems
    public void viewAllProblems() {

        if (problems.isEmpty()) {
            System.out.println("No problems available.");
            return;
        }

        System.out.println("\n========== ALL PROBLEMS ==========");

        for (Problem problem : problems) {
            problem.displayProblem();
        }
    }

    // 3. Search problem by ID
    public Problem searchProblemById(int problemId) {

        for (Problem problem : problems) {

            if (problem.getProblemId() == problemId) {
                return problem;
            }
        }

        return null;
    }

    // 4. Update problem details
    public boolean updateProblem(int problemId, String title,
                                 String description, String difficulty,
                                 int maxScore, String sampleInput,
                                 String sampleOutput) {

        Problem problem = searchProblemById(problemId);

        if (problem == null) {
            System.out.println("Problem not found!");
            return false;
        }

        // Update using setter methods
        problem.setProblemTitle(title);
        problem.setDescription(description);
        problem.setDifficulty(difficulty);
        problem.setMaxScore(maxScore);
        problem.setSampleInput(sampleInput);
        problem.setSampleOutput(sampleOutput);

        System.out.println("Problem updated successfully!");

        return true;
    }

    // 5. Delete problem by ID
    public boolean deleteProblem(int problemId) {

        Problem problem = searchProblemById(problemId);

        if (problem == null) {
            System.out.println("Problem not found!");
            return false;
        }

        problems.remove(problem);

        System.out.println("Problem deleted successfully!");

        return true;
    }

    // 6. View problems belonging to a particular contest
    public void viewProblemsByContestId(int contestId) {

        boolean found = false;

        System.out.println("\nProblems for Contest ID: " + contestId);

        for (Problem problem : problems) {

            if (problem.getContestId() == contestId) {

                problem.displayProblem();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No problems found for this contest.");
        }
    }

    // 7. Get problem by ID
    public Problem getProblemById(int problemId) {
        return searchProblemById(problemId);
    }

    // 8. Get all problems
    public ArrayList<Problem> getAllProblems() {
        return new ArrayList<>(problems);
    }

}

