package plagiarism;

import models.TokenSequence;
import models.SimilarityMatch;
import plagiarism.tokenizers.CodeTokenizer;
import plagiarism.algorithms.SimilarityAlgorithm;

public class PlagiarismAnalyzer {

    private final CodeTokenizer tokenizer;
    private final SimilarityAlgorithm algorithm;

    public PlagiarismAnalyzer(
            CodeTokenizer tokenizer,
            SimilarityAlgorithm algorithm) {

        if (tokenizer == null || algorithm == null) {
            throw new IllegalArgumentException(
                    "Tokenizer and algorithm cannot be null."
            );
        }

        this.tokenizer = tokenizer;
        this.algorithm = algorithm;
    }

    public SimilarityMatch compareSubmissions(
            int submissionIdA,
            String codeA,
            int submissionIdB,
            String codeB,
            double threshold) {

        if (threshold < 0 || threshold > 100) {
            throw new IllegalArgumentException(
                    "Threshold must be between 0 and 100."
            );
        }

        TokenSequence seqA = tokenizer.tokenizeCode(codeA);
        TokenSequence seqB = tokenizer.tokenizeCode(codeB);

        double similarity = algorithm.computeSimilarity(seqA, seqB);

        boolean reviewRecommended = similarity >= threshold;

        return new SimilarityMatch(
                submissionIdA,
                submissionIdB,
                similarity,
                reviewRecommended
        );
    }

    public String getAlgorithmName() {
        return algorithm.getAlgorithmName();
    }

    public String getSupportedLanguage() {
        return tokenizer.getSupportedLanguage();
    }
}