package algorithms;

import java.util.ArrayList;
import utils.TextProcessor;

public class PassageMatcher {

    // Find matching passages from the query
    public static ArrayList<String> findMatchingPassages(
            String query,
            String songLyrics) {

        ArrayList<String> matches = new ArrayList<>();

        String cleanQuery =
                TextProcessor.cleanText(query);

        String cleanSong =
                TextProcessor.cleanText(songLyrics);

        String[] queryWords =
                cleanQuery.split("\\s+");

        // Minimum 3 words are required
        if (queryWords.length < 3) {
            return matches;
        }

        // Create 3-word phrases
        for (int i = 0; i <= queryWords.length - 3; i++) {

            String phrase =
                    queryWords[i] + " "
                    + queryWords[i + 1] + " "
                    + queryWords[i + 2];

            ArrayList<Integer> positions =
                    KMPMatcher.search(
                            cleanSong,
                            phrase
                    );

            if (!positions.isEmpty()) {

                if (matches.isEmpty()) {

                    matches.add(phrase);

                } else {

                    int lastIndex =
                            matches.size() - 1;

                    String previous =
                            matches.get(lastIndex);

                    String[] previousWords =
                            previous.split("\\s+");

                    // Combine overlapping phrases
                    if (previousWords[previousWords.length - 1]
                            .equals(queryWords[i + 1])) {

                        String extended =
                                previous + " "
                                + queryWords[i + 2];

                        matches.set(lastIndex, extended);

                    } else {

                        matches.add(phrase);
                    }
                }
            }
        }

        return matches;
    }
}