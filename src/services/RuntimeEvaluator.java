package services;

public class RuntimeEvaluator {
    
    public long measureExecutionTime(Runnable task) {
        
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();

        return endTime-startTime;

    }

    public double convertToMilliSeconds(long executionTime) {
        return executionTime / 1_000_000.0;
    }

}
