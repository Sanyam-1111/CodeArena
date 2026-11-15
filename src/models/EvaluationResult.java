package models;

public class EvaluationResult {

    private int totalTestCases;
    private int passedTestCases;
    private int failedTestCases;
    private double score;
    private String status;
    private long evaluationTime;

    public EvaluationResult(int totalTestCases, int passedTestCases, int failedTestCases, double score, String status, long evaluationTime) {
        
        this.totalTestCases = totalTestCases;
        this.passedTestCases = passedTestCases;
        this.failedTestCases = failedTestCases;
        this.score = score;
        this.status = status;
        this.evaluationTime = evaluationTime;

    }

    public int getTotalTestCases() {
        return totalTestCases;
    }
    public void setTotalTestCases(int totalTestCases) {
        this.totalTestCases = totalTestCases;
    }

    public int getPassedTestCases() {
        return passedTestCases;
    }
    public void setPassedTestCases(int passedTestCases) {
        this.passedTestCases = passedTestCases;
    }

    public int getFailedTestCases() {
        return failedTestCases;
    }
    public void setFailedTestCases(int failedTestCases) {
        this.failedTestCases = failedTestCases;
    }

    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public long getEvaluationTime() {
        return evaluationTime;
    }
    public void setEvaluationTime(long evaluationTime) {
        this.evaluationTime = evaluationTime;
    }
    
}
