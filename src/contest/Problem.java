package contest;

import models.TestCase;

import java.util.ArrayList;
public class Problem {

    // Private data members
    private int problemId;
    private int contestId;
    private String problemTitle;
    private String description;
    private String difficulty;
    private int maxScore;
    private String sampleInput;
    private String sampleOutput;
    private ArrayList<TestCase> testCases;

    // Parameterized Constructor
    public Problem(int problemId, int contestId,
                   String problemTitle, String description,
                   String difficulty, int maxScore,
                   String sampleInput, String sampleOutput) {

        this.problemId = problemId;
        this.contestId = contestId;
        setProblemTitle(problemTitle);
        setDescription(description);
        setDifficulty(difficulty);
        setMaxScore(maxScore);
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
        this.testCases = new ArrayList<>();
    }

    // Getters
    public int getProblemId() {
        return problemId;
    }

    public int getContestId() {
        return contestId;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public String getSampleInput() {
        return sampleInput;
    }

    public String getSampleOutput() {
        return sampleOutput;
    }

    public ArrayList<TestCase> getTestCases() {
        return new ArrayList<>(testCases);
    }

    public void addTestCase(TestCase testCase) {
        if (testCase == null) {
            throw new IllegalArgumentException("Test case cannot be null.");
        }
        testCases.add(testCase);
    }

    // Setters
    public void setProblemTitle(String problemTitle) {
        if (problemTitle == null || problemTitle.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Problem title cannot be empty."
            );
        }

        this.problemTitle = problemTitle;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Description cannot be empty."
            );
        }

        this.description = description;
    }

    public void setDifficulty(String difficulty) {

        if (difficulty == null) {
            throw new IllegalArgumentException(
                "Difficulty cannot be empty."
            );
        }

        difficulty = difficulty.trim();

        if (difficulty.equalsIgnoreCase("Easy") ||
            difficulty.equalsIgnoreCase("Medium") ||
            difficulty.equalsIgnoreCase("Hard")) {

            this.difficulty = difficulty.substring(0, 1).toUpperCase()
                    + difficulty.substring(1).toLowerCase();

        } else {
            throw new IllegalArgumentException(
                "Difficulty must be Easy, Medium or Hard."
            );
        }
    }

    public void setMaxScore(int maxScore) {
        if (maxScore <= 0) {
            throw new IllegalArgumentException(
                "Maximum score must be greater than zero."
            );
        }

        this.maxScore = maxScore;
    }

    public void setSampleInput(String sampleInput) {
        this.sampleInput = sampleInput;
    }

    public void setSampleOutput(String sampleOutput) {
        this.sampleOutput = sampleOutput;
    }

    // Display Problem Details
    public void displayProblem() {

        System.out.println("\n========== PROBLEM DETAILS ==========");
        System.out.println("Problem ID   : " + problemId);
        System.out.println("Contest ID   : " + contestId);
        System.out.println("Title        : " + problemTitle);
        System.out.println("Description  : " + description);
        System.out.println("Difficulty   : " + difficulty);
        System.out.println("Maximum Score: " + maxScore);
        System.out.println("Sample Input : " + sampleInput);
        System.out.println("Sample Output: " + sampleOutput);
        System.out.println("=====================================");
    }
}
