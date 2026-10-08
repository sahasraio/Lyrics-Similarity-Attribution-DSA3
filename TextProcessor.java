package utils;

import java.util.*;

public class TextProcessor {

    // Convert lyrics into clean lowercase text
    public static String cleanText(String text) {

        text = text.toLowerCase();

        // Remove punctuation and special characters
        text = text.replaceAll("[^a-zA-Z\\s]", " ");

        // Remove extra spaces
        text = text.replaceAll("\\s+", " ");

        return text.trim();
    }

    // Convert lyrics into individual words
    public static ArrayList<String> getWords(String text) {

        String cleanedText = cleanText(text);

        if (cleanedText.isEmpty()) {
            return new ArrayList<>();
        }

        String[] words = cleanedText.split(" ");

        return new ArrayList<>(Arrays.asList(words));
    }
}