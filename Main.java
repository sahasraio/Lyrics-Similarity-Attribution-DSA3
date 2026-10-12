import utils.LyricsReader;
import utils.TextProcessor;

import algorithms.WordAnalyzer;
import algorithms.SimilarityCalculator;
import algorithms.SimilarityResult;
import algorithms.MergeSorter;
import algorithms.PassageMatcher;
import algorithms.ComplexityAnalyzer;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            System.out.println("\n==========================================");
            System.out.println("      LYRICS SIMILARITY & ATTRIBUTION");
            System.out.println("==========================================");

            System.out.println("1. Search Lyrics");
            System.out.println("2. View Lyrics Catalogue");
            System.out.println("3. View Algorithm Complexity");
            System.out.println("4. Exit");

            System.out.print("\nEnter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    searchLyrics(scanner);
                    break;

                case "2":
                    viewCatalogue();
                    break;

                case "3":
                    ComplexityAnalyzer.displayComplexities();
                    break;

                case "4":
                    running = false;
                    System.out.println(
                            "\nThank you for using Lyrics Similarity & Attribution!"
                    );
                    break;

                default:
                    System.out.println(
                            "\nInvalid choice. Please enter 1, 2, 3, or 4."
                    );
            }
        }

        scanner.close();
    }


    // =========================================================
    // SEARCH LYRICS
    // =========================================================

    public static void searchLyrics(Scanner scanner) {

        System.out.println("\n==========================================");
        System.out.println("             SEARCH LYRICS");
        System.out.println("==========================================");

        System.out.println("\nEnter lyrics to search:");
        System.out.print("> ");

        String queryLyrics = scanner.nextLine();

        if (queryLyrics.trim().isEmpty()) {

            System.out.println("\nNo lyrics entered.");
            return;
        }

        System.out.println("\nAnalyzing lyrics...");
        System.out.println("------------------------------------------");

        ArrayList<String> queryWordList =
                TextProcessor.getWords(queryLyrics);

        HashSet<String> queryWords =
                WordAnalyzer.getUniqueWords(queryWordList);

        Map<String, String> songs =
                LyricsReader.readAllSongs("lyrics");

        if (songs.isEmpty()) {

            System.out.println(
                    "\nNo lyric files found in the lyrics folder."
            );

            return;
        }

        SimilarityResult[] results =
                new SimilarityResult[songs.size()];

        int index = 0;

        for (Map.Entry<String, String> song : songs.entrySet()) {

            ArrayList<String> songWordList =
                    TextProcessor.getWords(song.getValue());

            HashSet<String> songWords =
                    WordAnalyzer.getUniqueWords(songWordList);

            double similarity =
                    SimilarityCalculator.calculateJaccardSimilarity(
                            queryWords,
                            songWords
                    );

            results[index] =
                    new SimilarityResult(
                            song.getKey(),
                            similarity
                    );

            index++;
        }

        MergeSorter.sort(results);

        System.out.println("\n==========================================");
        System.out.println("       RANKED SIMILARITY RESULTS");
        System.out.println("==========================================");

        for (int i = 0; i < results.length; i++) {

            System.out.printf(
                    "%d. %-12s -> %.2f%%%n",
                    i + 1,
                    results[i].getSongName(),
                    results[i].getScore()
            );
        }

        System.out.println("\n==========================================");
        System.out.println("     TOP SIMILARITY CANDIDATE");
        System.out.println("==========================================");

        if (results.length > 0) {

            System.out.println(
                    results[0].getSongName()
                    + " -> "
                    + String.format(
                            "%.2f%%",
                            results[0].getScore()
                    )
            );

            System.out.println(
                    "\nThis is the top candidate according to "
                    + "the implemented similarity metric."
            );
        }

        System.out.println("\n==========================================");
        System.out.println("     SIMILAR PASSAGE DETECTION");
        System.out.println("==========================================");

        boolean passageFound = false;

        for (Map.Entry<String, String> song : songs.entrySet()) {

            ArrayList<String> matchingPassages =
                    PassageMatcher.findMatchingPassages(
                            queryLyrics,
                            song.getValue()
                    );

            if (!matchingPassages.isEmpty()) {

                passageFound = true;

                System.out.println("\nSong: " + song.getKey());

                System.out.println("Matching passages:");

                for (String passage : matchingPassages) {

                    System.out.println(
                            "  -> \"" + passage + "\""
                    );
                }
            }
        }

        if (!passageFound) {

            System.out.println(
                    "No similar passages found."
            );
        }

        System.out.println("\n==========================================");
        System.out.println("          ATTRIBUTION REPORT");
        System.out.println("==========================================");

        System.out.println(
                "Query analyzed successfully."
        );

        System.out.println(
                "\nTop Candidate   : "
                + results[0].getSongName()
        );

        System.out.println(
                "Similarity Score: "
                + String.format(
                        "%.2f%%",
                        results[0].getScore()
                )
        );

        double topScore =
                results[0].getScore();

        String similarityLevel;

        if (topScore >= 50) {

            similarityLevel = "High";

        } else if (topScore >= 25) {

            similarityLevel = "Moderate";

        } else if (topScore >= 10) {

            similarityLevel = "Low";

        } else {

            similarityLevel = "Very Low";
        }

        System.out.println(
                "Similarity Level: "
                + similarityLevel
        );

        if (passageFound) {

            System.out.println(
                    "Similar Passage : FOUND"
            );

            System.out.println(
                    "Status          : Potentially Similar Source"
            );

        } else {

            System.out.println(
                    "Similar Passage : NOT FOUND"
            );

            System.out.println(
                    "Status          : No Strong Passage Match"
            );
        }

        System.out.println(
                "\nNote: Similarity scores indicate textual "
                + "overlap according to the implemented algorithms "
                + "and do not by themselves prove authorship."
        );

        System.out.println("\n==========================================");
        System.out.println("            ANALYSIS COMPLETE");
        System.out.println("==========================================");

        System.out.println(
                "\nPress Enter to return to the main menu..."
        );

        scanner.nextLine();
    }


    // =========================================================
    // VIEW LYRICS CATALOGUE
    // =========================================================

    public static void viewCatalogue() {

        System.out.println("\n==========================================");
        System.out.println("           LYRICS CATALOGUE");
        System.out.println("==========================================");

        Map<String, String> songs =
                LyricsReader.readAllSongs("lyrics");

        if (songs.isEmpty()) {

            System.out.println(
                    "\nNo lyric files found."
            );

            return;
        }

        int number = 1;

        for (Map.Entry<String, String> song : songs.entrySet()) {

            System.out.println(
                    "\n" + number + ". " + song.getKey()
            );

            System.out.println(
                    "------------------------------------------"
            );

            System.out.println(
                    song.getValue()
            );

            number++;
        }

        System.out.println("\n==========================================");
        System.out.println(
                "Total Songs: " + songs.size()
        );
        System.out.println("==========================================");
    }
}