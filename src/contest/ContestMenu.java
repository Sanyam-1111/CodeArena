package contest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ContestMenu {

    private static Scanner scanner = new Scanner(System.in);
    private static ContestManager contestManager = new ContestManager();
    private static ProblemManager problemManager = new ProblemManager();
    private static ContestRegistrationManager registrationManager =
            new ContestRegistrationManager(contestManager);

    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n========== CODEARENA CONTEST MANAGEMENT ==========");
            System.out.println("1. Add Contest");
            System.out.println("2. View All Contests");
            System.out.println("3. Search Contest");
            System.out.println("4. Update Contest");
            System.out.println("5. Delete Contest");
            System.out.println("6. Add Problem");
            System.out.println("7. View All Problems");
            System.out.println("8. Search Problem");
            System.out.println("9. Update Problem");
            System.out.println("10. Delete Problem");
            System.out.println("11. View Problems By Contest");
            System.out.println("12. Register Student");
            System.out.println("13. View All Registrations");
            System.out.println("14. View Students By Contest");
            System.out.println("15. Cancel Registration");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = readInteger();

            switch (choice) {
                case 1:
                    addContest();
                    break;
                case 2:
                    contestManager.viewAllContests();
                    break;
                case 3:
                    searchContest();
                    break;
                case 4:
                    updateContest();
                    break;
                case 5:
                    deleteContest();
                    break;
                case 6:
                    addProblem();
                    break;
                case 7:
                    problemManager.viewAllProblems();
                    break;
                case 8:
                    searchProblem();
                    break;
                case 9:
                    updateProblem();
                    break;
                case 10:
                    deleteProblem();
                    break;
                case 11:
                    viewProblemsByContest();
                    break;
                case 12:
                    registerStudent();
                    break;
                case 13:
                    registrationManager.viewAllRegistrations();
                    break;
                case 14:
                    viewStudentsByContest();
                    break;
                case 15:
                    cancelRegistration();
                    break;
                case 0:
                    System.out.println("Exiting CodeArena Contest Management.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 0);
    }

    private static int readInteger() {

        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    private static LocalDateTime readDateTime(String message) {

        while (true) {
            try {
                System.out.print(message);
                return LocalDateTime.parse(scanner.nextLine().trim(), formatter);
            } catch (Exception e) {
                System.out.println("Invalid format. Use yyyy-MM-dd HH:mm.");
            }
        }
    }

    private static void addContest() {

        System.out.print("Enter contest name: ");
        String name = scanner.nextLine();

        System.out.print("Enter contest description: ");
        String description = scanner.nextLine();

        LocalDateTime startTime =
                readDateTime("Enter start time (yyyy-MM-dd HH:mm): ");

        LocalDateTime endTime =
                readDateTime("Enter end time (yyyy-MM-dd HH:mm): ");

        try {
            contestManager.addContest(name, description, startTime, endTime);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void searchContest() {

        System.out.print("Enter Contest ID: ");
        int id = readInteger();

        Contest contest = contestManager.searchContestById(id);

        if (contest != null) {
            contest.displayContest();
        } else {
            System.out.println("Contest not found.");
        }
    }

    private static void updateContest() {

        System.out.print("Enter Contest ID to update: ");
        int id = readInteger();

        if (contestManager.getContestById(id) == null) {
            System.out.println("Contest not found.");
            return;
        }

        System.out.print("Enter new contest name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new description: ");
        String description = scanner.nextLine();

        LocalDateTime startTime =
                readDateTime("Enter new start time (yyyy-MM-dd HH:mm): ");

        LocalDateTime endTime =
                readDateTime("Enter new end time (yyyy-MM-dd HH:mm): ");

        try {
            boolean updated = contestManager.updateContest(
                    id, name, description, startTime, endTime
            );

            System.out.println(updated
                    ? "Contest updated successfully."
                    : "Contest update failed.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteContest() {

        System.out.print("Enter Contest ID to delete: ");
        int id = readInteger();

        boolean deleted = contestManager.deleteContest(id);

        System.out.println(deleted
                ? "Contest deleted successfully."
                : "Contest not found.");
    }

    private static void addProblem() {

        System.out.print("Enter Contest ID: ");
        int contestId = readInteger();

        if (contestManager.getContestById(contestId) == null) {
            System.out.println("Contest not found. Add the contest first.");
            return;
        }

        System.out.print("Enter problem title: ");
        String title = scanner.nextLine();

        System.out.print("Enter problem description: ");
        String description = scanner.nextLine();

        System.out.print("Enter difficulty (Easy/Medium/Hard): ");
        String difficulty = scanner.nextLine();

        System.out.print("Enter maximum score: ");
        int maxScore = readInteger();

        System.out.print("Enter sample input: ");
        String sampleInput = scanner.nextLine();

        System.out.print("Enter sample output: ");
        String sampleOutput = scanner.nextLine();

        try {
            problemManager.addProblem(
                    contestId, title, description, difficulty,
                    maxScore, sampleInput, sampleOutput
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void searchProblem() {

        System.out.print("Enter Problem ID: ");
        int id = readInteger();

        Problem problem = problemManager.searchProblemById(id);

        if (problem != null) {
            problem.displayProblem();
        } else {
            System.out.println("Problem not found.");
        }
    }

    private static void updateProblem() {

        System.out.print("Enter Problem ID to update: ");
        int id = readInteger();

        if (problemManager.getProblemById(id) == null) {
            System.out.println("Problem not found.");
            return;
        }

        System.out.print("Enter new problem title: ");
        String title = scanner.nextLine();

        System.out.print("Enter new description: ");
        String description = scanner.nextLine();

        System.out.print("Enter new difficulty (Easy/Medium/Hard): ");
        String difficulty = scanner.nextLine();

        System.out.print("Enter new maximum score: ");
        int maxScore = readInteger();

        System.out.print("Enter new sample input: ");
        String sampleInput = scanner.nextLine();

        System.out.print("Enter new sample output: ");
        String sampleOutput = scanner.nextLine();

        try {
            boolean updated = problemManager.updateProblem(
                    id, title, description, difficulty,
                    maxScore, sampleInput, sampleOutput
            );

            System.out.println(updated
                    ? "Problem updated successfully."
                    : "Problem update failed.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteProblem() {

        System.out.print("Enter Problem ID to delete: ");
        int id = readInteger();

        boolean deleted = problemManager.deleteProblem(id);

        System.out.println(deleted
                ? "Problem deleted successfully."
                : "Problem not found.");
    }

    private static void viewProblemsByContest() {

        System.out.print("Enter Contest ID: ");
        int contestId = readInteger();

        problemManager.viewProblemsByContestId(contestId);
    }

    private static void registerStudent() {

        System.out.print("Enter Contest ID: ");
        int contestId = readInteger();

        System.out.print("Enter Student ID: ");
        int studentId = readInteger();

        registrationManager.registerStudent(contestId, studentId);
    }

    private static void viewStudentsByContest() {

        System.out.print("Enter Contest ID: ");
        int contestId = readInteger();

        registrationManager.viewStudentsByContestId(contestId);
    }

    private static void cancelRegistration() {

        System.out.print("Enter Contest ID: ");
        int contestId = readInteger();

        System.out.print("Enter Student ID: ");
        int studentId = readInteger();

        registrationManager.cancelRegistration(contestId, studentId);
    }
}

