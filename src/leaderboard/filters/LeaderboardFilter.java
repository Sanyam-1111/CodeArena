package leaderboard.filters;

import java.util.List;

public interface LeaderboardFilter<T> {
    List<T> applyFilter(List<T> entries);
    String getFilterDescription();
}