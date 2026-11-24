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
        if (findStudentByEmail(email.trim()) != null) {
            System.out.println("Email " + email.trim() + " is already registered.");
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

    // Find a student by email (case-insensitive)
    public Student findStudentByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        String target = email.trim().toLowerCase();
        for (Student s : students) {
            if (s.getEmail().toLowerCase().equals(target)) {
                return s;
            }
        }
        return null;
    }

    // Search students by name keyword (case-insensitive)
    public ArrayList<Student> searchStudentsByName(String keyword) {
        ArrayList<Student> matches = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return matches;
        }
        String query = keyword.trim().toLowerCase();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(query)) {
                matches.add(s);
            }
        }
        return matches;
    }

    // Get all registered students
    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // Get total count of registered students
    public int getTotalStudents() {
        return students.size();
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
        Student existingEmailOwner = findStudentByEmail(newEmail.trim());
        if (existingEmailOwner != null && existingEmailOwner.getStudentId() != studentId) {
            System.out.println("Email " + newEmail.trim() + " is already used by another student.");
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