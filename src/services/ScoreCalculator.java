package services;

public class ScoreCalculator {
    
    public double calculateScore(int passedTestCases, double marksPerTestCase) {
        return passedTestCases*marksPerTestCase;
    }

    public double calculatePercentage(int passedTestCases, int totalTestCases) {
        if(totalTestCases==0){
            return 0.0;
        }
        return ((double) passedTestCases / totalTestCases)*100;
    }
}
