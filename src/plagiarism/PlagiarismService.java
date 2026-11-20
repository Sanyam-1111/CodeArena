package plagiarism;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import models.Token;
import models.TokenSequence;
import plagiarism.algorithms.SimilarityAlgorithm;
import plagiarism.tokenizers.CodeTokenizer;

public class PlagiarismService implements CodeTokenizer, SimilarityAlgorithm {
    private static final Pattern TOKEN_PATTERN =
            Pattern.compile("[A-Za-z_$][A-Za-z0-9_$]*|\\d+(?:\\.\\d+)?|\\S");

    @Override
    public TokenSequence tokenizeCode(String sourceCode) {
        if (sourceCode == null) {
            throw new IllegalArgumentException("Source code cannot be null");
        }

        List<Token> tokens = new ArrayList<>();
        String[] lines = sourceCode.split("\\R", -1);

        for (int i = 0; i < lines.length; i++) {
            Matcher matcher = TOKEN_PATTERN.matcher(lines[i]);
            while (matcher.find()) {
                String value = matcher.group();
                char firstCharacter = value.charAt(0);
                String type = Character.isLetter(firstCharacter) || firstCharacter == '_' || firstCharacter == '$'
                        ? "WORD"
                        : Character.isDigit(firstCharacter) ? "NUMBER" : "SYMBOL";
                tokens.add(new Token(type, value, i + 1));
            }
        }

        return new TokenSequence(tokens);
    }

    @Override
    public double computeSimilarity(TokenSequence seqA, TokenSequence seqB) {
        if (seqA == null || seqB == null) {
            throw new IllegalArgumentException("Token sequences cannot be null");
        }

        List<String> tokensA = getTokenValues(seqA);
        List<String> tokensB = getTokenValues(seqB);
        int totalTokens = tokensA.size() + tokensB.size();
        if (totalTokens == 0) {
            return 0.0;
        }

        int[][] commonTokens = new int[tokensA.size() + 1][tokensB.size() + 1];
        for (int i = 1; i <= tokensA.size(); i++) {
            for (int j = 1; j <= tokensB.size(); j++) {
                if (tokensA.get(i - 1).equals(tokensB.get(j - 1))) {
                    commonTokens[i][j] = commonTokens[i - 1][j - 1] + 1;
                } else {
                    commonTokens[i][j] = Math.max(commonTokens[i - 1][j], commonTokens[i][j - 1]);
                }
            }
        }

        return commonTokens[tokensA.size()][tokensB.size()] * 200.0 / totalTokens;
    }

    public boolean isReviewRecommended(double similarityPercent, double thresholdPercent) {
        if (similarityPercent < 0.0 || similarityPercent > 100.0
                || thresholdPercent < 0.0 || thresholdPercent > 100.0) {
            throw new IllegalArgumentException("Similarity and threshold must be between 0 and 100");
        }
        return similarityPercent >= thresholdPercent;
    }

    @Override
    public String getSupportedLanguage() {
        return "Java";
    }

    @Override
    public String getAlgorithmName() {
        return "Jaccard token similarity";
    }

    private List<String> getTokenValues(TokenSequence sequence) {
        List<String> values = new ArrayList<>();
        for (Token currentToken : sequence.getTokens()) {
            values.add(currentToken.getValue());
        }
        return values;
    }
}
