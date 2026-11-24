package student;

import models.Student;
import java.util.ArrayList;

public class StudentService {
    private final ArrayList<Student> students = new ArrayList<>();

    // Register a new student
    public boolean registerStudent(int studentId, String name, String email) {
        if (studentId <= 0) {
            System.out.println("Invalid student ID. Must be a positive number.");
            return false;
        }
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return false;
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            System.out.println("Invalid email address.");
            return false;
        }
        if (getStudentById(studentId) != null) {
            System.out.println("Student ID " + studentId + " is already registered.");
            return false;
        }

        students.add(new Student(studentId, name.trim(), email.trim()));
        System.out.println("Registered student: " + name.trim());
        return true;
    }

    // Find a student by ID
    public Student getStudentById(int studentId) {
        for (Student s : students) {
            if (s.getStudentId() == studentId) {
                return s;
            }
        }
        return null;
    }

    // Get all registered students
    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // Update student details
    public boolean updateStudent(int studentId, String newName, String newEmail) {
        Student s = getStudentById(studentId);
        if (s == null) {
            System.out.println("Student with ID " + studentId + " not found.");
            return false;
        }
        if (newName == null || newName.trim().isEmpty()) {
            System.out.println("Student name cannot be empty.");
            return false;
        }
        if (newEmail == null || newEmail.trim().isEmpty() || !newEmail.contains("@")) {
            System.out.println("Invalid email address.");
            return false;
        }

        s.setName(newName.trim());
        s.setEmail(newEmail.trim());
        System.out.println("Updated student ID " + studentId);
        return true;
    }

    // Delete a student by ID
    public boolean deleteStudent(int studentId) {
        Student s = getStudentById(studentId);
        if (s == null) {
            System.out.println("Student with ID " + studentId + " not found.");
            return false;
        }
        students.remove(s);
        System.out.println("Deleted student ID " + studentId);
        return true;
    }
}