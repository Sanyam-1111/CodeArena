import models.TestCase;
import models.EvaluationResult;
import services.EvaluationService;
import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) {
        
        ArrayList<TestCase> testCases = new ArrayList<>();

        testCases.add( new TestCase(1, "5 5", "10", 10));
        testCases.add( new TestCase(2, "10 10", "20", 10));
        testCases.add( new TestCase(3, "15 15", "30", 10));

        ArrayList<String> actualOutputs = new ArrayList<>();

        actualOutputs.add("10");
        actualOutputs.add("20");
        actualOutputs.add("25");

        EvaluationService evaluationService = new EvaluationService();

        EvaluationResult result = evaluationService.evaluateSubmission(testCases, actualOutputs, 10);

        System.out.println("Total Test Cases: " + result.getTotalTestCases());
        System.out.println("Passed: " + result.getPassedTestCases());
        System.out.println("Failed: " + result.getFailedTestCases());
        System.out.println("Score: " + result.getScore());
        System.out.println("Status: " + result.getStatus());
        System.out.println("Evaluation Time: " + result.getEvaluationTime() + " ns");
    }

}

