package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LyricsReader {

    // Read one lyrics file
    public static String readFile(String filePath) {

        StringBuilder lyrics = new StringBuilder();

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (String line : lines) {
                lyrics.append(line).append(" ");
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }

        return lyrics.toString().trim();
    }

    // Read all .txt files from lyrics folder
    public static Map<String, String> readAllSongs(String folderPath) {

        Map<String, String> songs = new LinkedHashMap<>();

        try {
            File folder = new File(folderPath);

            File[] files = folder.listFiles();

            if (files != null) {

                for (File file : files) {

                    if (file.isFile() && file.getName().endsWith(".txt")) {

                        String lyrics = readFile(file.getPath());

                        songs.put(file.getName(), lyrics);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading lyrics folder.");
        }

        return songs;
    }
}