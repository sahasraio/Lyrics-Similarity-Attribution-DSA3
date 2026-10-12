import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import algorithms.MergeSorter;
import algorithms.PassageMatcher;
import algorithms.SimilarityCalculator;
import algorithms.SimilarityResult;
import algorithms.WordAnalyzer;

import utils.LyricsReader;
import utils.TextProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class BackendServer {

    public static void main(String[] args) throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );

       server.createContext(
        "/api/analyze",
        BackendServer::analyzeLyrics
);

server.createContext(
        "/",
        BackendServer::serveWebsite
);

server.start();

        System.out.println("==========================================");
        System.out.println("       LYRICS SIMILARITY BACKEND");
        System.out.println("==========================================");
        System.out.println();
        System.out.println("Backend server started successfully.");
        System.out.println();
        System.out.println("Server running at:");
        System.out.println("http://localhost:8080");
        System.out.println();
        System.out.println("Waiting for frontend requests...");
        System.out.println("==========================================");
    }


   // =========================================================
// SERVE WEBSITE FILES
// =========================================================

private static void serveWebsite(
        HttpExchange exchange) throws IOException {

    if (!"GET".equalsIgnoreCase(
            exchange.getRequestMethod())) {

        sendResponse(
                exchange,
                "Method Not Allowed",
                405
        );

        return;
    }

    String path =
            exchange.getRequestURI().getPath();

    String fileName;

    if (path.equals("/") ||
            path.equals("/index.html")) {

        fileName = "index.html";

    } else if (path.equals("/style.css")) {

        fileName = "style.css";

    } else if (path.equals("/script.js")) {

        fileName = "script.js";

    } else {

        sendResponse(
                exchange,
                "404 Not Found",
                404
        );

        return;
    }


    java.nio.file.Path file =
            java.nio.file.Paths.get(
                    "web",
                    fileName
            );


    if (!java.nio.file.Files.exists(file)) {

        sendResponse(
                exchange,
                "File Not Found",
                404
        );

        return;
    }


    byte[] content =
            java.nio.file.Files.readAllBytes(file);


    String contentType;

    if (fileName.endsWith(".html")) {

        contentType =
                "text/html; charset=UTF-8";

    } else if (fileName.endsWith(".css")) {

        contentType =
                "text/css; charset=UTF-8";

    } else {

        contentType =
                "application/javascript; charset=UTF-8";
    }


    exchange.getResponseHeaders().set(
            "Content-Type",
            contentType
    );


    exchange.sendResponseHeaders(
            200,
            content.length
    );


    try (OutputStream output =
                 exchange.getResponseBody()) {

        output.write(content);
    }
}

    private static void analyzeLyrics(
            HttpExchange exchange) throws IOException {

        // Allow frontend requests
        exchange.getResponseHeaders().add(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().add(
                "Content-Type",
                "application/json; charset=UTF-8"
        );

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {

            exchange.sendResponseHeaders(204, -1);

            return;
        }


        // Only accept POST requests
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {

            sendResponse(
                    exchange,
                    "{\"error\":\"Only POST requests are allowed.\"}",
                    405
            );

            return;
        }


        // Read request body
        String requestBody =
                new String(
                        exchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8
                );


        // Extract lyrics from JSON
        String queryLyrics =
                extractLyrics(requestBody);


        if (queryLyrics == null ||
                queryLyrics.trim().isEmpty()) {

            sendResponse(
                    exchange,
                    "{\"error\":\"Lyrics cannot be empty.\"}",
                    400
            );

            return;
        }


        // -----------------------------------------------------
        // Read stored songs
        // -----------------------------------------------------

        Map<String, String> songs =
                LyricsReader.readAllSongs("lyrics");


        if (songs.isEmpty()) {

            sendResponse(
                    exchange,
                    "{\"error\":\"No songs found.\"}",
                    500
            );

            return;
        }


        // -----------------------------------------------------
        // Process query
        // -----------------------------------------------------

        ArrayList<String> queryWordList =
                TextProcessor.getWords(queryLyrics);

        HashSet<String> queryWords =
                WordAnalyzer.getUniqueWords(queryWordList);


        // -----------------------------------------------------
        // Calculate similarity
        // -----------------------------------------------------

        SimilarityResult[] results =
                new SimilarityResult[songs.size()];

        int index = 0;

        for (Map.Entry<String, String> song :
                songs.entrySet()) {

            ArrayList<String> songWordList =
                    TextProcessor.getWords(song.getValue());

            HashSet<String> songWords =
                    WordAnalyzer.getUniqueWords(songWordList);

            double similarity =
                    SimilarityCalculator
                            .calculateJaccardSimilarity(
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


        // -----------------------------------------------------
        // Sort results
        // -----------------------------------------------------

        MergeSorter.sort(results);


        // -----------------------------------------------------
        // Build JSON response
        // -----------------------------------------------------

        StringBuilder json =
                new StringBuilder();

        json.append("{");

        json.append("\"results\":[");


        for (int i = 0;
                i < results.length;
                i++) {

            json.append("{");

            json.append("\"song\":\"")
                    .append(escapeJson(
                            results[i].getSongName()
                    ))
                    .append("\",");

            json.append("\"score\":")
                    .append(
                            String.format(
                                    "%.2f",
                                    results[i].getScore()
                            )
                    );

            json.append("}");

            if (i < results.length - 1) {
                json.append(",");
            }
        }


        json.append("],");


        // -----------------------------------------------------
        // Top candidate
        // -----------------------------------------------------

        json.append("\"topCandidate\":\"")
                .append(
                        escapeJson(
                                results[0].getSongName()
                        )
                )
                .append("\",");

        json.append("\"topScore\":")
                .append(
                        String.format(
                                "%.2f",
                                results[0].getScore()
                        )
                )
                .append(",");


        // -----------------------------------------------------
        // Passage detection
        // -----------------------------------------------------

        json.append("\"passages\":[");

        boolean firstPassage = true;

        for (Map.Entry<String, String> song :
                songs.entrySet()) {

            ArrayList<String> passages =
                    PassageMatcher.findMatchingPassages(
                            queryLyrics,
                            song.getValue()
                    );

            for (String passage : passages) {

                if (!firstPassage) {
                    json.append(",");
                }

                json.append("{");

                json.append("\"song\":\"")
                        .append(
                                escapeJson(
                                        song.getKey()
                                )
                        )
                        .append("\",");

                json.append("\"passage\":\"")
                        .append(
                                escapeJson(
                                        passage
                                )
                        )
                        .append("\"");

                json.append("}");

                firstPassage = false;
            }
        }

        json.append("]");

        json.append("}");


        // -----------------------------------------------------
        // Send response
        // -----------------------------------------------------

        sendResponse(
                exchange,
                json.toString(),
                200
        );
    }


    // =========================================================
    // EXTRACT LYRICS FROM JSON
    // =========================================================

    private static String extractLyrics(
            String json) {

        String key =
                "\"lyrics\"";

        int keyPosition =
                json.indexOf(key);

        if (keyPosition == -1) {
            return null;
        }

        int colonPosition =
                json.indexOf(
                        ":",
                        keyPosition
                );

        if (colonPosition == -1) {
            return null;
        }

        int firstQuote =
                json.indexOf(
                        "\"",
                        colonPosition + 1
                );

        if (firstQuote == -1) {
            return null;
        }

        StringBuilder result =
                new StringBuilder();

        boolean escaped = false;

        for (int i = firstQuote + 1;
                i < json.length();
                i++) {

            char c =
                    json.charAt(i);

            if (escaped) {

                if (c == 'n') {
                    result.append('\n');
                } else if (c == 'r') {
                    result.append('\r');
                } else if (c == 't') {
                    result.append('\t');
                } else {
                    result.append(c);
                }

                escaped = false;

            } else if (c == '\\') {

                escaped = true;

            } else if (c == '"') {

                break;

            } else {

                result.append(c);
            }
        }

        return result.toString();
    }


    // =========================================================
    // SEND RESPONSE
    // =========================================================

    private static void sendResponse(
            HttpExchange exchange,
            String response,
            int statusCode) throws IOException {

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(bytes);
        }
    }


    // =========================================================
    // ESCAPE JSON
    // =========================================================

    private static String escapeJson(
            String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}