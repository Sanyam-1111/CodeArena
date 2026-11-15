package models;

public class TestCase {
    
    private int testCaseId;
    private String input;
    private String expectedOutput;
    private double marks;

    public TestCase(int testCaseId, String input, String expectedOutput, double marks) {
        this.testCaseId = testCaseId;
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.marks = marks;
    }

    public int getTestCaseId() {
        return testCaseId;
    }
    public void setTestCaseId(int testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getInput() {
        return input;
    }
    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }
    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public double getMarks() {
        return marks;
    }
    public void setMarks(double marks) {
        this.marks = marks;
    }

    
}
