package services;

import models.EvaluationResult;
import models.TestCase;
import java.util.ArrayList;

public class EvaluationService {
    
    private ScoreCalculator scoreCalculator;
    private RuntimeEvaluator runtimeEvaluator;

    public EvaluationService() {
        scoreCalculator = new ScoreCalculator();
        runtimeEvaluator = new RuntimeEvaluator();
    }

    public EvaluationResult evaluateSubmission(ArrayList<TestCase> testCases, ArrayList<String> actualOutputs, double marksPerTestCase) {
        return evaluate(testCases, actualOutputs, marksPerTestCase, false);
    }

    public EvaluationResult evaluateSubmission(ArrayList<TestCase> testCases, ArrayList<String> actualOutputs) {
        return evaluate(testCases, actualOutputs, 0.0, true);
    }

    private EvaluationResult evaluate(ArrayList<TestCase> testCases,
                                      ArrayList<String> actualOutputs,
                                      double marksPerTestCase,
                                      boolean usePerCaseMarks) {
        if (testCases == null || actualOutputs == null) {
            throw new IllegalArgumentException("Test cases and outputs cannot be null.");
        }
        if(testCases.size() != actualOutputs.size()){
            throw new IllegalArgumentException("Test cases and outputs must have equal sizes");
        }

        int[] passed = {0};
        int[] failed = {0};
        double[] score = {0.0};
        String[] status = {""};

        int totalTestCases = testCases.size();

        long evaluationTime = runtimeEvaluator.measureExecutionTime( () -> {

            for(int i=0;i<testCases.size();i++) {
                String expected = testCases.get(i).getExpectedOutput();
                String actual = actualOutputs.get(i);

                if((expected.trim()).equals(actual.trim())) {
                    passed[0]++;
                    if (usePerCaseMarks) {
                        score[0] += testCases.get(i).getMarks();
                    }
                }
                else {
                    failed[0]++;
                }
            }

            if (!usePerCaseMarks) {
                score[0] = scoreCalculator.calculateScore(passed[0], marksPerTestCase);
            }

            if(totalTestCases==0){
                status[0] = "Failed";
            }
            else if(passed[0]==totalTestCases){
                status[0] = "Accepted";
            }
            else if(passed[0]==0){
                status[0] = "Failed";
            }
            else{
                status[0] = "Partial";
            }

        });

        return new EvaluationResult(totalTestCases, passed[0], failed[0], score[0], status[0], evaluationTime);
    }
}
