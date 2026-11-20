package plagiarism.normalizers;

public interface CodeNormalizer {
    String normalize(String rawSourceCode);
    String getStepName();
}