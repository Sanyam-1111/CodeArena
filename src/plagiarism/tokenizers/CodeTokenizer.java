package plagiarism.tokenizers;

import models.TokenSequence;

public interface CodeTokenizer {
    TokenSequence tokenizeCode(String cleanSourceCode);
    String getSupportedLanguage();
}