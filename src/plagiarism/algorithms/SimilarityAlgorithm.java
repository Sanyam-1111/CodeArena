package plagiarism.algorithms;

import models.TokenSequence;

public interface SimilarityAlgorithm {

    double computeSimilarity(TokenSequence seqA, TokenSequence seqB);

    String getAlgorithmName();
}