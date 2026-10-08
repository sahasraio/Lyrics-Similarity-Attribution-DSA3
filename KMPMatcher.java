package algorithms;

import java.util.ArrayList;

public class KMPMatcher {

    // Create LPS (Longest Prefix Suffix) array
    private static int[] createLPS(String pattern) {

        int[] lps = new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(length)) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    // Search for pattern inside text
    public static ArrayList<Integer> search(
            String text,
            String pattern) {

        ArrayList<Integer> positions =
                new ArrayList<>();

        if (pattern.isEmpty()) {
            return positions;
        }

        int[] lps = createLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {

                i++;
                j++;

                if (j == pattern.length()) {

                    positions.add(i - j);

                    j = lps[j - 1];
                }

            } else {

                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return positions;
    }
}