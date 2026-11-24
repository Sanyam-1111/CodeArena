package models;

import java.util.ArrayList;
import java.util.List;

public class PlagiarismReport {

    private final List<SimilarityMatch> matches;
    private final int totalComparisons;
    private final int suspiciousMatches;

    public PlagiarismReport(List<SimilarityMatch> matches) {

        if (matches == null) {
            throw new IllegalArgumentException("Matches cannot be null.");
        }

        this.matches = new ArrayList<>(matches);
        this.totalComparisons = matches.size();

        int count = 0;

        for (SimilarityMatch match : matches) {
            if (match.isReviewRecommended()) {
                count++;
            }
        }

        this.suspiciousMatches = count;
    }

    public List<SimilarityMatch> getMatches() {
        return new ArrayList<>(matches);
    }

    public int getTotalComparisons() {
        return totalComparisons;
    }

    public int getSuspiciousMatches() {
        return suspiciousMatches;
    }

    public void displayReport() {

        System.out.println("\n========== PLAGIARISM REPORT ==========");

        System.out.println("Total Comparisons: " + totalComparisons);

        System.out.println("Suspicious Matches: " + suspiciousMatches);

        if (matches.isEmpty()) {
            System.out.println("No submissions were compared.");
        } else {
            for (SimilarityMatch match : matches) {
                System.out.println(match);
            }
        }

        System.out.println("=======================================\n");
    }
}