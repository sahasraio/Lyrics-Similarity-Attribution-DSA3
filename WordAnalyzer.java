package algorithms;

import java.util.*;

public class WordAnalyzer {

    // Count the frequency of every word
    public static HashMap<String, Integer> getWordFrequency(
            ArrayList<String> words) {

        HashMap<String, Integer> frequency = new HashMap<>();

        for (String word : words) {

            frequency.put(
                word,
                frequency.getOrDefault(word, 0) + 1
            );
        }

        return frequency;
    }

    // Get only unique words
    public static HashSet<String> getUniqueWords(
            ArrayList<String> words) {

        return new HashSet<>(words);
    }
}