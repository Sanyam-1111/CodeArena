package models;

public class SimilarityMatch {

    private final int submissionIdA;
    private final int submissionIdB;
    private final double similarityPercentage;
    private final boolean reviewRecommended;

    public SimilarityMatch(
            int submissionIdA,
            int submissionIdB,
            double similarityPercentage,
            boolean reviewRecommended) {

        if (submissionIdA == submissionIdB) {
                throw new IllegalArgumentException("Cannot compare a submission with itself.");
        }

        if (similarityPercentage < 0 || similarityPercentage > 100) {
                throw new IllegalArgumentException("Similarity must be between 0 and 100.");
        }

        this.submissionIdA = submissionIdA;
        this.submissionIdB = submissionIdB;
        this.similarityPercentage = similarityPercentage;
        this.reviewRecommended = reviewRecommended;
    }

    public int getSubmissionIdA() {
        return submissionIdA;
    }

    public int getSubmissionIdB() {
        return submissionIdB;
    }

    public double getSimilarityPercentage() {
        return similarityPercentage;
    }

    public boolean isReviewRecommended() {
        return reviewRecommended;
    }

    @Override
    public String toString() {
        return "Submission " + submissionIdA
                + " vs Submission " + submissionIdB
                + " | Similarity: " + similarityPercentage + "%"
                + " | Review Recommended: " + reviewRecommended;
    }
}