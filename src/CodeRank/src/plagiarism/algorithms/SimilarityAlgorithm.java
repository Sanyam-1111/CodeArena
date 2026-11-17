package plagiarism.algorithms;

import models.tokensequence;

public interface SimilarityAlgorithm {
    double computeSimilarity(tokensequence seqA, tokensequence seqB);
    String getAlgorithmName();
}