package app;

import contest.Contest;
import contest.ContestManager;
import contest.ContestRegistrationManager;
import contest.Problem;
import contest.ProblemManager;
import leaderboard.LeaderboardService;
import models.EvaluationResult;
import models.Submission;
import models.TestCase;
import plagiarism.PlagiarismService;
import services.EvaluationService;
import services.SubmissionService;
import student.StudentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class CodeArenaApplication {

    private final StudentService studentService = new StudentService();
    private final ContestManager contestManager = new ContestManager();
    private final ProblemManager problemManager = new ProblemManager(contestManager);
    private final ContestRegistrationManager registrationManager =
            new ContestRegistrationManager(contestManager, studentService);
    private final EvaluationService evaluationService = new EvaluationService();
    private final SubmissionService submissionService = new SubmissionService(
            studentService, contestManager, problemManager, evaluationService);
    private final LeaderboardService leaderboardService = new LeaderboardService();
    private final PlagiarismService plagiarismService = new PlagiarismService();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void run() {
        new CodeArenaApplication().runMenu();
    }

    private void runMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(scanner, "Choose an option: ");
            try {
                switch (choice) {
                    case 1:
                        registerStudent(scanner);
                        break;
                    case 2:
                        displayStudents();
                        break;
                    case 3:
                        updateStudent(scanner);
                        break;
                    case 4:
                        studentService.deleteStudent(readInt(scanner, "Student ID: "));
                        break;
                    case 5:
                        addContest(scanner);
                        break;
                    case 6:
                        contestManager.viewAllContests();
                        break;
                    case 7:
                        searchContest(scanner);
                        break;
                    case 8:
                        updateContest(scanner);
                        break;
                    case 9:
                        contestManager.deleteContest(readInt(scanner, "Contest ID: "));
                        break;
                    case 10:
                        addProblem(scanner);
                        break;
                    case 11:
                        problemManager.viewProblemsByContestId(readInt(scanner, "Contest ID: "));
                        break;
                    case 12:
                        addTestCase(scanner);
                        break;
                    case 13:
                        viewTestCases(scanner);
                        break;
                    case 14:
                        registrationManager.registerStudent(
                                readInt(scanner, "Contest ID: "),
                                readInt(scanner, "Student ID: "));
                        break;
                    case 15:
                        registrationManager.viewAllRegistrations();
                        break;
                    case 16:
                        registrationManager.viewStudentsByContestId(readInt(scanner, "Contest ID: "));
                        break;
                    case 17:
                        registrationManager.cancelRegistration(
                                readInt(scanner, "Contest ID: "),
                                readInt(scanner, "Student ID: "));
                        break;
                    case 18:
                        createSubmission(scanner);
                        break;
                    case 19:
                        evaluateSubmission(scanner);
                        break;
                    case 20:
                        viewSubmissions();
                        break;
                    case 21:
                        showLeaderboard(scanner);
                        break;
                    case 22:
                        showPlagiarismReport(scanner);
                        break;
                    case 23:
                        updateProblem(scanner);
                        break;
                    case 24:
                        problemManager.deleteProblem(readInt(scanner, "Problem ID: "));
                        break;
                    case 25:
                        searchProblem(scanner);
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Exiting CodeArena.");
    }

    private void printMenu() {
        System.out.println("\n========== CODEARENA ==========");
        System.out.println("1. Register student");
        System.out.println("2. View students");
        System.out.println("3. Update student");
        System.out.println("4. Delete student");
        System.out.println("5. Create contest");
        System.out.println("6. View contests");
        System.out.println("7. Search contest by name");
        System.out.println("8. Update contest");
        System.out.println("9. Delete contest");
        System.out.println("10. Add problem");
        System.out.println("11. View problems in contest");
        System.out.println("12. Add test case to problem");
        System.out.println("13. View problem test cases");
        System.out.println("14. Register student for contest");
        System.out.println("15. View registrations");
        System.out.println("16. View students in contest");
        System.out.println("17. Cancel contest registration");
        System.out.println("18. Submit source code");
        System.out.println("19. Evaluate supplied outputs");
        System.out.println("20. View submissions");
        System.out.println("21. Show contest leaderboard");
        System.out.println("22. Analyze plagiarism for contest problem");
        System.out.println("23. Update problem");
        System.out.println("24. Delete problem");
        System.out.println("25. Search problem by ID");
        System.out.println("0. Exit");
    }

    private void registerStudent(Scanner scanner) {
        int id = readInt(scanner, "Student ID: ");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        studentService.registerStudent(id, name, email);
    }

    private void displayStudents() {
        if (studentService.getAllStudents().isEmpty()) {
            System.out.println("No students registered.");
            return;
        }
        studentService.getAllStudents().forEach(System.out::println);
    }

    private void updateStudent(Scanner scanner) {
        int id = readInt(scanner, "Student ID: ");
        System.out.print("New name: ");
        String name = scanner.nextLine();
        System.out.print("New email: ");
        String email = scanner.nextLine();
        studentService.updateStudent(id, name, email);
    }

    private void addContest(Scanner scanner) {
        System.out.print("Contest name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        LocalDateTime start = readDateTime(scanner, "Start time (yyyy-MM-dd HH:mm): ");
        LocalDateTime end = readDateTime(scanner, "End time (yyyy-MM-dd HH:mm): ");
        contestManager.addContest(name, description, start, end);
    }

    private void searchContest(Scanner scanner) {
        System.out.print("Contest name keyword: ");
        String keyword = scanner.nextLine().trim().toLowerCase();
        boolean found = false;
        for (Contest contest : contestManager.getAllContests()) {
            if (contest.getContestName().toLowerCase().contains(keyword)) {
                contest.displayContest();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching contests found.");
        }
    }

    private void updateContest(Scanner scanner) {
        int id = readInt(scanner, "Contest ID: ");
        if (contestManager.getContestById(id) == null) {
            System.out.println("Contest not found.");
            return;
        }
        System.out.print("New name: ");
        String name = scanner.nextLine();
        System.out.print("New description: ");
        String description = scanner.nextLine();
        LocalDateTime start = readDateTime(scanner, "New start time (yyyy-MM-dd HH:mm): ");
        LocalDateTime end = readDateTime(scanner, "New end time (yyyy-MM-dd HH:mm): ");
        contestManager.updateContest(id, name, description, start, end);
    }

    private void addProblem(Scanner scanner) {
        int contestId = readInt(scanner, "Contest ID: ");
        System.out.print("Problem title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Difficulty (Easy/Medium/Hard): ");
        String difficulty = scanner.nextLine();
        int maxScore = readInt(scanner, "Maximum score: ");
        System.out.print("Sample input: ");
        String sampleInput = scanner.nextLine();
        System.out.print("Sample output: ");
        String sampleOutput = scanner.nextLine();
        problemManager.addProblem(contestId, title, description, difficulty, maxScore, sampleInput, sampleOutput);
    }

    private void updateProblem(Scanner scanner) {
        int problemId = readInt(scanner, "Problem ID: ");
        System.out.print("New title: ");
        String title = scanner.nextLine();
        System.out.print("New description: ");
        String description = scanner.nextLine();
        System.out.print("New difficulty (Easy/Medium/Hard): ");
        String difficulty = scanner.nextLine();
        int maxScore = readInt(scanner, "New maximum score: ");
        System.out.print("New sample input: ");
        String sampleInput = scanner.nextLine();
        System.out.print("New sample output: ");
        String sampleOutput = scanner.nextLine();
        problemManager.updateProblem(problemId, title, description, difficulty, maxScore, sampleInput, sampleOutput);
    }

    private void searchProblem(Scanner scanner) {
        Problem problem = problemManager.getProblemById(readInt(scanner, "Problem ID: "));
        if (problem == null) {
            System.out.println("Problem not found.");
        } else {
            problem.displayProblem();
        }
    }

    private void addTestCase(Scanner scanner) {
        int problemId = readInt(scanner, "Problem ID: ");
        System.out.print("Input: ");
        String input = scanner.nextLine();
        System.out.print("Expected output: ");
        String expectedOutput = scanner.nextLine();
        double marks = readDouble(scanner, "Marks: ");
        TestCase testCase = problemManager.addTestCase(problemId, input, expectedOutput, marks);
        System.out.println("Added test case ID " + testCase.getTestCaseId());
    }

    private void viewTestCases(Scanner scanner) {
        int problemId = readInt(scanner, "Problem ID: ");
        ArrayList<TestCase> testCases = problemManager.getTestCasesForProblem(problemId);
        if (testCases.isEmpty()) {
            System.out.println("No test cases found for this problem.");
            return;
        }
        for (TestCase testCase : testCases) {
            System.out.println("ID " + testCase.getTestCaseId() + " | Input: " + testCase.getInput()
                    + " | Expected: " + testCase.getExpectedOutput() + " | Marks: " + testCase.getMarks());
        }
    }

    private void createSubmission(Scanner scanner) {
        int studentId = readInt(scanner, "Student ID: ");
        int contestId = readInt(scanner, "Contest ID: ");
        int problemId = readInt(scanner, "Problem ID: ");
        String sourceCode = readMultilineCode(scanner);
        Submission submission = submissionService.createSubmission(studentId, contestId, problemId, sourceCode);
        System.out.println("Submission created with ID " + submission.getSubmissionId()
                + " at " + submission.getSubmissionTime());
    }

    private String readMultilineCode(Scanner scanner) {
        System.out.println("Enter source code. Finish with a line containing END.");
        StringBuilder sourceCode = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("END")) {
                break;
            }
            if (sourceCode.length() > 0) {
                sourceCode.append(System.lineSeparator());
            }
            sourceCode.append(line);
        }
        return sourceCode.toString();
    }

    private void evaluateSubmission(Scanner scanner) {
        int submissionId = readInt(scanner, "Submission ID: ");
        Submission submission = submissionService.getSubmissionById(submissionId);
        if (submission == null) {
            System.out.println("Submission not found.");
            return;
        }
        ArrayList<TestCase> testCases = problemManager.getTestCasesForProblem(submission.getProblemId());
        ArrayList<String> actualOutputs = new ArrayList<>();
        System.out.println("Enter the supplied actual output for each test case in order.");
        for (TestCase testCase : testCases) {
            System.out.print("Output for test case " + testCase.getTestCaseId() + ": ");
            actualOutputs.add(scanner.nextLine());
        }
        EvaluationResult result = submissionService.evaluateSubmission(submissionId, actualOutputs);
        System.out.println("Passed: " + result.getPassedTestCases() + "/" + result.getTotalTestCases());
        System.out.println("Score: " + result.getScore());
        System.out.println("Status: " + result.getStatus());
        System.out.println("Output comparison time: " + result.getEvaluationTime() + " ns");
    }

    private void viewSubmissions() {
        ArrayList<Submission> submissions = submissionService.getAllSubmissions();
        if (submissions.isEmpty()) {
            System.out.println("No submissions available.");
            return;
        }
        for (Submission submission : submissions) {
            String status = submission.getEvaluationResult() == null
                    ? "Not evaluated" : submission.getEvaluationResult().getStatus();
            System.out.println("Submission " + submission.getSubmissionId()
                    + " | Student " + submission.getStudentId()
                    + " | Contest " + submission.getContestId()
                    + " | Problem " + submission.getProblemId()
                    + " | Status: " + status);
        }
    }

    private void showLeaderboard(Scanner scanner) {
        int contestId = readInt(scanner, "Contest ID: ");
        leaderboardService.refreshContestLeaderboard(
                contestId, submissionService.getAllSubmissions(), studentService);
        leaderboardService.displayContestLeaderboard(contestId);
    }

    private void showPlagiarismReport(Scanner scanner) {
        int contestId = readInt(scanner, "Contest ID: ");
        int problemId = readInt(scanner, "Problem ID: ");
        double threshold = readDouble(scanner, "Similarity threshold percent (0-100): ");
        plagiarismService.analyzeProblemSubmissions(
                submissionService.getAllSubmissions(), contestId, problemId, threshold).displayReport();
    }

    private int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private LocalDateTime readDateTime(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDateTime.parse(scanner.nextLine().trim(), dateTimeFormatter);
            } catch (Exception e) {
                System.out.println("Use yyyy-MM-dd HH:mm.");
            }
        }
    }
}
