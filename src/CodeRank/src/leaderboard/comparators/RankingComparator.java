package leaderboard.comparators;

import java.util.Comparator;

public interface RankingComparator<T> extends Comparator<T> {
    String getRuleName();

    default RankingComparator<T> thenBreakTieWith(RankingComparator<T> nextRule) {
        return new RankingComparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                int primaryResult = RankingComparator.this.compare(o1, o2);
                return (primaryResult != 0) ? primaryResult : nextRule.compare(o1, o2);
            }

            @Override
            public String getRuleName() {
                return RankingComparator.this.getRuleName() + " -> " + nextRule.getRuleName();
            }
        };
    }
}