package leaderboard.exporters;

import java.util.List;

public interface LeaderboardExporter<T> {
    String export(List<T> entries);
    String getFormatType();
}