package plagiarism.algorithms;

import java.util.HashSet;
import java.util.Set;
import models.Token;
import models.TokenSequence;

public class JaccardSimilarityAlgorithm implements SimilarityAlgorithm {

    @Override
    public double computeSimilarity(TokenSequence seqA, TokenSequence seqB) {

        if (seqA == null || seqB == null) {
            throw new IllegalArgumentException(
                    "Token sequences cannot be null."
            );
        }

        Set<String> tokensA = getTokenValues(seqA);
        Set<String> tokensB = getTokenValues(seqB);

        Set<String> commonTokens = new HashSet<>(tokensA);
        commonTokens.retainAll(tokensB);

        Set<String> allTokens = new HashSet<>(tokensA);
        allTokens.addAll(tokensB);

        if (allTokens.isEmpty()) {
            return 0.0;
        }

        return (commonTokens.size() * 100.0) / allTokens.size();
    }

    private Set<String> getTokenValues(TokenSequence sequence) {

        Set<String> values = new HashSet<>();

        for (Token token : sequence.getTokens()) {
            values.add(token.getValue());
        }

        return values;
    }

    @Override
    public String getAlgorithmName() {
        return "Jaccard Similarity";
    }
}