import models.TestCase;
import models.EvaluationResult;
import services.EvaluationService;
import java.util.ArrayList;

import student.ContestService;
import student.StudentService;


public class Main {
    
    public static void main(String[] args) {
        
        ArrayList<TestCase> testCases = new ArrayList<>();

        testCases.add(new TestCase(1, "5 5", "10", 10));
        testCases.add(new TestCase(2, "10 10", "20", 10));
        testCases.add(new TestCase(3, "15 15", "30", 10));

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

        // Member 1: Student & Contest Management
        StudentService studentService = new StudentService();
        ContestService contestService = new ContestService();

        // Test Student Management
        studentService.registerStudent(101, "Aarav", "aarav@coderank.edu");
        studentService.registerStudent(102, "Priya", "priya@coderank.edu");
        System.out.println("All Students: " + studentService.getAllStudents());
        System.out.println("Find 101: " + studentService.getStudentById(101));

        studentService.updateStudent(101, "Aarav Sharma", "aarav.s@coderank.edu");
        studentService.deleteStudent(102);
        System.out.println("Updated Students: " + studentService.getAllStudents());

        // Test Contest Management
        contestService.createContest(1, "Weekly Contest", "10:00 AM", "12:00 PM");
        contestService.createContest(2, "Code Sprint", "2:00 PM", "4:00 PM");
        System.out.println("All Contests: " + contestService.getAllContests());
        System.out.println("Find Contest 1: " + contestService.getContestDetails(1));

        contestService.updateContest(1, "Weekly Contest #1", "10:00 AM", "1:00 PM");
        contestService.deleteContest(2);
        System.out.println("Updated Contests: " + contestService.getAllContests());






        // StudentService studentService = new StudentService();
        // ContestService contestService = new ContestService();

        // // Test Student Management
        // studentService.registerStudent(101, "Aarav", "aarav@coderank.edu");
        // studentService.registerStudent(102, "Priya", "priya@coderank.edu");
        // System.out.println("All Students: " + studentService.getAllStudents());
        // System.out.println("Find 101: " + studentService.getStudentById(101));

        // studentService.updateStudent(101, "Aarav Sharma", "aarav.s@coderank.edu");
        // studentService.deleteStudent(102);
        // System.out.println("Updated Students: " + studentService.getAllStudents());

        // // Test Contest Management
        // contestService.createContest(1, "Weekly Contest", "10:00 AM", "12:00 PM");
        // contestService.createContest(2, "Code Sprint", "2:00 PM", "4:00 PM");
        // System.out.println("All Contests: " + contestService.getAllContests());
        // System.out.println("Find Contest 1: " + contestService.getContestDetails(1));

        // contestService.updateContest(1, "Weekly Contest #1", "10:00 AM", "1:00 PM");
        // contestService.deleteContest(2);
        // System.out.println("Updated Contests: " + contestService.getAllContests());
    }

}




// Harsimran checks 
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;

// import leaderboard.LeaderboardService;
// import models.LeaderboardEntry;
// import models.PlagiarismReport;
// import models.SimilarityMatch;
// import models.Student;
// import plagiarism.PlagiarismService;

// public class Main {

//     public static void main(String[] args) {

//         testLeaderboard();

//         System.out.println();

//         testPlagiarism();
//     }

//     private static void testLeaderboard() {
//         LeaderboardService leaderboard = new LeaderboardService();

//         leaderboard.addEntry(new LeaderboardEntry(
//                 new Student(101, "Asha", "asha@gmail.com"),
//                 30,
//                 120,
//                 LocalDateTime.now().minusMinutes(10)
//         ));

//         leaderboard.addEntry(new LeaderboardEntry(
//                 new Student(102, "Ravi", "ravi@gmail.com"),
//                 30,
//                 95,
//                 LocalDateTime.now().minusMinutes(8)
//         ));

//         leaderboard.addEntry(new LeaderboardEntry(
//                 new Student(103, "Meera", "meera@gmail.com"),
//                 20,
//                 80,
//                 LocalDateTime.now().minusMinutes(5)
//         ));

//         leaderboard.displayLeaderboard();
//     }

//     private static void testPlagiarism() {
//         String codeA =
//                 "int sum = a + b;\n" +
//                 "return sum;";

//         String codeB =
//                 "int sum=a+b;\n" +
//                 "return sum;";

//         String codeC =
//                 "int largest = a;\n" +
//                 "if (b > largest) largest = b;\n" +
//                 "return largest;";

//         PlagiarismService plagiarism = new PlagiarismService();
//         double threshold = 80.0;

//         SimilarityMatch match1 = plagiarism.compareSubmissions(
//                 1, codeA,
//                 2, codeB,
//                 threshold
//         );

//         SimilarityMatch match2 = plagiarism.compareSubmissions(
//                 1, codeA,
//                 3, codeC,
//                 threshold
//         );

//         List<SimilarityMatch> matches = new ArrayList<>();
//         matches.add(match1);
//         matches.add(match2);

//         PlagiarismReport report = plagiarism.generateReport(matches);
//         report.displayReport();
//     }
// }