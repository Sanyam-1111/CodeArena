package plagiarism;

import java.util.ArrayList;
import java.util.List;

import models.TokenSequence;
import models.SimilarityMatch;
import models.PlagiarismReport;
import models.Submission;

import plagiarism.algorithms.SimilarityAlgorithm;
import plagiarism.algorithms.LCSSimilarityAlgorithm;

import plagiarism.tokenizers.CodeTokenizer;
import plagiarism.tokenizers.JavaCodeTokenizer;

public class PlagiarismService implements CodeTokenizer, SimilarityAlgorithm {

    private final CodeTokenizer tokenizer;
    private final SimilarityAlgorithm algorithm;
    private final PlagiarismAnalyzer analyzer;

    public PlagiarismService() {

        this.tokenizer = new JavaCodeTokenizer();
        this.algorithm = new LCSSimilarityAlgorithm();

        this.analyzer = new PlagiarismAnalyzer(
                tokenizer,
                algorithm
        );
    }

    public PlagiarismService(
            CodeTokenizer tokenizer,
            SimilarityAlgorithm algorithm) {

        if (tokenizer == null || algorithm == null) {
            throw new IllegalArgumentException(
                    "Tokenizer and algorithm cannot be null."
            );
        }

        this.tokenizer = tokenizer;
        this.algorithm = algorithm;

        this.analyzer = new PlagiarismAnalyzer(
                tokenizer,
                algorithm
        );
    }

    // Convert source code into tokens
    @Override
    public TokenSequence tokenizeCode(String sourceCode) {
        return tokenizer.tokenizeCode(sourceCode);
    }

    // Calculate similarity between two token sequences
    @Override
    public double computeSimilarity(
            TokenSequence seqA,
            TokenSequence seqB) {

        return algorithm.computeSimilarity(seqA, seqB);
    }

    // Check whether manual review is recommended
    public boolean isReviewRecommended(
            double similarityPercent,
            double thresholdPercent) {

        if (similarityPercent < 0 || similarityPercent > 100
                || thresholdPercent < 0 || thresholdPercent > 100) {

            throw new IllegalArgumentException(
                    "Similarity and threshold must be between 0 and 100."
            );
        }

        return similarityPercent >= thresholdPercent;
    }

    // Compare two submissions
    public SimilarityMatch compareSubmissions(
            int submissionIdA,
            String codeA,
            int submissionIdB,
            String codeB,
            double threshold) {

        return analyzer.compareSubmissions(
                submissionIdA,
                codeA,
                submissionIdB,
                codeB,
                threshold
        );
    }

    // Generate plagiarism report
    public PlagiarismReport generateReport(
            List<SimilarityMatch> matches) {

        if (matches == null) {
            throw new IllegalArgumentException(
                    "Matches cannot be null."
            );
        }

        return new PlagiarismReport(matches);
    }

    public PlagiarismReport analyzeProblemSubmissions(
            List<Submission> submissions,
            int contestId,
            int problemId,
            double threshold) {
        if (submissions == null || contestId <= 0 || problemId <= 0) {
            throw new IllegalArgumentException("Submissions and positive contest and problem IDs are required.");
        }
        if (threshold < 0 || threshold > 100) {
            throw new IllegalArgumentException("Threshold must be between 0 and 100.");
        }

        List<String> sourceCodes = new ArrayList<>();
        List<Integer> submissionIds = new ArrayList<>();
        for (Submission submission : submissions) {
            if (submission == null) {
                throw new IllegalArgumentException("Submissions cannot contain null entries.");
            }
            if (submission.getContestId() == contestId
                    && submission.getProblemId() == problemId) {
                sourceCodes.add(submission.getSourceCode());
                submissionIds.add(submission.getSubmissionId());
            }
        }

        return generateReport(compareMultipleSubmissions(sourceCodes, submissionIds, threshold));
    }

    // Compare multiple pairs of submissions
    public List<SimilarityMatch> compareMultipleSubmissions(
            List<String> sourceCodes,
            List<Integer> submissionIds,
            double threshold) {

        if (sourceCodes == null || submissionIds == null) {
            throw new IllegalArgumentException(
                    "Submission lists cannot be null."
            );
        }

        if (sourceCodes.size() != submissionIds.size()) {
            throw new IllegalArgumentException(
                    "Submission IDs and source codes must have equal sizes."
            );
        }

        List<SimilarityMatch> matches = new ArrayList<>();

        for (int i = 0; i < sourceCodes.size(); i++) {

            for (int j = i + 1; j < sourceCodes.size(); j++) {

                SimilarityMatch match = compareSubmissions(
                        submissionIds.get(i),
                        sourceCodes.get(i),
                        submissionIds.get(j),
                        sourceCodes.get(j),
                        threshold
                );

                matches.add(match);
            }
        }

        return matches;
    }

    @Override
    public String getSupportedLanguage() {
        return tokenizer.getSupportedLanguage();
    }

    @Override
    public String getAlgorithmName() {
        return algorithm.getAlgorithmName();
    }
}
