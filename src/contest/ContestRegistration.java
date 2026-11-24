package contest;

import java.time.LocalDateTime;

public class ContestRegistration {

    private int registrationId;
    private int contestId;
    private int studentId;
    private LocalDateTime registrationDate;
    private String registrationStatus;

    public ContestRegistration(int registrationId, int contestId,
                               int studentId) {

        if (registrationId <= 0 || contestId <= 0 || studentId <= 0) {
            throw new IllegalArgumentException(
                "IDs must be greater than zero."
            );
        }

        this.registrationId = registrationId;
        this.contestId = contestId;
        this.studentId = studentId;
        this.registrationDate = LocalDateTime.now();
        this.registrationStatus = "Registered";
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public int getContestId() {
        return contestId;
    }

    public int getStudentId() {
        return studentId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {

        if (registrationStatus == null ||
            (!registrationStatus.equalsIgnoreCase("Registered") &&
             !registrationStatus.equalsIgnoreCase("Cancelled"))) {

            throw new IllegalArgumentException(
                "Status must be Registered or Cancelled."
            );
        }

        this.registrationStatus =
            registrationStatus.substring(0, 1).toUpperCase()
            + registrationStatus.substring(1).toLowerCase();
    }

    public void displayRegistration() {

        System.out.println("\n======= REGISTRATION DETAILS =======");
        System.out.println("Registration ID : " + registrationId);
        System.out.println("Contest ID      : " + contestId);
        System.out.println("Student ID      : " + studentId);
        System.out.println("Registration Date: " + registrationDate);
        System.out.println("Status          : " + registrationStatus);
        System.out.println("====================================");
    }
}

