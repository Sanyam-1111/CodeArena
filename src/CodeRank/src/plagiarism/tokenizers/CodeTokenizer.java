package plagiarism.tokenizers;

import models.tokensequence;

public interface CodeTokenizer {
    tokensequence tokenizeCode(String cleanSourceCode);
    String getSupportedLanguage();
}