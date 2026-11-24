package plagiarism.tokenizers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Token;
import models.TokenSequence;

public class JavaCodeTokenizer implements CodeTokenizer {

    private static final Pattern TOKEN_PATTERN =
            Pattern.compile("[A-Za-z_$][A-Za-z0-9_$]*|\\d+(?:\\.\\d+)?|\\S");

    @Override
    public TokenSequence tokenizeCode(String sourceCode) {

        if (sourceCode == null) {
            throw new IllegalArgumentException(
                    "Source code cannot be null."
            );
        }

        List<Token> tokens = new ArrayList<>();

        String[] lines = sourceCode.split("\\R", -1);

        for (int i = 0; i < lines.length; i++) {

            Matcher matcher = TOKEN_PATTERN.matcher(lines[i]);

            while (matcher.find()) {

                String value = matcher.group();

                char firstCharacter = value.charAt(0);

                String type;

                if (Character.isLetter(firstCharacter)
                        || firstCharacter == '_'
                        || firstCharacter == '$') {

                    type = "WORD";

                } else if (Character.isDigit(firstCharacter)) {

                    type = "NUMBER";

                } else {

                    type = "SYMBOL";
                }

                tokens.add(new Token(type, value, i + 1));
            }
        }

        return new TokenSequence(tokens);
    }

    @Override
    public String getSupportedLanguage() {
        return "Java";
    }
}