package algorithms;

import java.util.*;

public class SimilarityCalculator {

    public static double calculateJaccardSimilarity(
            HashSet<String> queryWords,
            HashSet<String> songWords) {

        // Find common words
        HashSet<String> intersection =
                new HashSet<>(queryWords);

        intersection.retainAll(songWords);

        // Find all unique words
        HashSet<String> union =
                new HashSet<>(queryWords);

        union.addAll(songWords);

        // Avoid division by zero
        if (union.isEmpty()) {
            return 0.0;
        }

        return ((double) intersection.size()
                / union.size()) * 100;
    }
}