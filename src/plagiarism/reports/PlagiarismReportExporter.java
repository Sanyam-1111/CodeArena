package plagiarism.reports;

import java.util.List;

public interface PlagiarismReportExporter<T> {
    String generateReport(List<T> matches, double thresholdUsed);
}