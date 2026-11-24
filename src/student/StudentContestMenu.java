package student;

import models.Contest;
import models.Student;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentContestMenu {

    public static void main(String[] args) {
        StudentService studentService = new StudentService();
        ContestService contestService = new ContestService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MEMBER 1: STUDENT & CONTEST MANAGEMENT ===");
            System.out.println("1. Register Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by Name");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Create Contest");
            System.out.println("7. View All Contests");
            System.out.println("8. Search Contest by Name");
            System.out.println("9. Update Contest");
            System.out.println("10. Delete Contest");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Student ID: ");
                    int sid = readInt(scanner);
                    System.out.print("Name: ");
                    String sname = scanner.nextLine();
                    System.out.print("Email: ");
                    String semail = scanner.nextLine();
                    studentService.registerStudent(sid, sname, semail);
                    break;

                case 2:
                    ArrayList<Student> students = studentService.getAllStudents();
                    if (students.isEmpty()) {
                        System.out.println("No students registered.");
                    } else {
                        for (Student s : students) {
                            System.out.println(s);
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter name keyword: ");
                    String skeyword = scanner.nextLine();
                    ArrayList<Student> foundStudents = studentService.searchStudentsByName(skeyword);
                    if (foundStudents.isEmpty()) {
                        System.out.println("No matching students found.");
                    } else {
                        for (Student s : foundStudents) {
                            System.out.println(s);
                        }
                    }
                    break;

                case 4:
                    System.out.print("Student ID to update: ");
                    int usid = readInt(scanner);
                    System.out.print("New Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("New Email: ");
                    String newEmail = scanner.nextLine();
                    studentService.updateStudent(usid, newName, newEmail);
                    break;

                case 5:
                    System.out.print("Student ID to delete: ");
                    int dsid = readInt(scanner);
                    studentService.deleteStudent(dsid);
                    break;

                case 6:
                    System.out.print("Contest ID: ");
                    int cid = readInt(scanner);
                    System.out.print("Contest Name: ");
                    String cname = scanner.nextLine();
                    System.out.print("Start Time: ");
                    String start = scanner.nextLine();
                    System.out.print("End Time: ");
                    String end = scanner.nextLine();
                    contestService.createContest(cid, cname, start, end);
                    break;

                case 7:
                    ArrayList<Contest> contests = contestService.getAllContests();
                    if (contests.isEmpty()) {
                        System.out.println("No contests available.");
                    } else {
                        for (Contest c : contests) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 8:
                    System.out.print("Enter contest name keyword: ");
                    String ckeyword = scanner.nextLine();
                    ArrayList<Contest> foundContests = contestService.searchContestsByName(ckeyword);
                    if (foundContests.isEmpty()) {
                        System.out.println("No matching contests found.");
                    } else {
                        for (Contest c : foundContests) {
                            System.out.println(c);
                        }
                    }
                    break;

                case 9:
                    System.out.print("Contest ID to update: ");
                    int ucid = readInt(scanner);
                    System.out.print("New Contest Name: ");
                    String newCName = scanner.nextLine();
                    System.out.print("New Start Time: ");
                    String newStart = scanner.nextLine();
                    System.out.print("New End Time: ");
                    String newEnd = scanner.nextLine();
                    contestService.updateContest(ucid, newCName, newStart, newEnd);
                    break;

                case 10:
                    System.out.print("Contest ID to delete: ");
                    int dcid = readInt(scanner);
                    contestService.deleteContest(dcid);
                    break;

                case 0:
                    System.out.println("Exiting Member 1 Menu.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, please re-enter: ");
            }
        }
    }
}