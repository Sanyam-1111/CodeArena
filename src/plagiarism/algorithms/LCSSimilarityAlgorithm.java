package plagiarism.algorithms;

import java.util.List;
import models.TokenSequence;

public class LCSSimilarityAlgorithm implements SimilarityAlgorithm {

    @Override
    public double computeSimilarity(TokenSequence seqA, TokenSequence seqB) {

        if (seqA == null || seqB == null) {
            throw new IllegalArgumentException(
                    "Token sequences cannot be null."
            );
        }

        List<String> tokensA = getTokenValues(seqA);
        List<String> tokensB = getTokenValues(seqB);

        int totalTokens = tokensA.size() + tokensB.size();

        if (totalTokens == 0) {
            return 0.0;
        }

        int[][] dp = new int[tokensA.size() + 1][tokensB.size() + 1];

        for (int i = 1; i <= tokensA.size(); i++) {

            for (int j = 1; j <= tokensB.size(); j++) {

                if (tokensA.get(i - 1).equals(tokensB.get(j - 1))) {

                    dp[i][j] = dp[i - 1][j - 1] + 1;

                } else {

                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);

                }
            }
        }

        int commonTokens = dp[tokensA.size()][tokensB.size()];

        return (commonTokens * 200.0) / totalTokens;
    }

    private List<String> getTokenValues(TokenSequence sequence) {

        List<String> values = new java.util.ArrayList<>();

        for (models.Token token : sequence.getTokens()) {
            values.add(token.getValue());
        }

        return values;
    }

    @Override
    public String getAlgorithmName() {
        return "Longest Common Subsequence (LCS)";
    }
}