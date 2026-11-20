package models;

public class LeaderboardEntry {
    private final Student student;
    private final double score;

    public LeaderboardEntry(Student student, double score) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        this.student = student;
        this.score = score;
    }

    public Student getStudent() {
        return student;
    } 

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return student.getName() + " - " + score;
    }
}
