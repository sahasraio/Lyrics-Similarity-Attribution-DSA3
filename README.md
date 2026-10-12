# Lyrics Similarity and Attribution

## 1. Project Overview

Lyrics Similarity and Attribution is a Java-based Data Structures and
Algorithms project designed to compare a given set of lyrics with a
collection of stored songs.

The system analyzes the input lyrics, calculates similarity with each
song, ranks the results, and detects matching passages using pattern
matching algorithms.

The project demonstrates the practical application of data structures
and algorithms in text analysis.

---

## 2. Objectives

The main objectives of this project are:

- To compare lyrics using textual similarity.
- To identify songs with similar words.
- To detect matching lyric passages.
- To rank songs based on their similarity scores.
- To demonstrate the use of Data Structures and Algorithms.
- To generate a simple attribution-oriented analysis report.

---

## 3. Technologies Used

- Java
- Java Collections Framework
- File Handling
- Visual Studio Code
- Java 25

---

## 4. Data Structures Used

### HashMap

HashMap is used for storing word frequencies.

Example:

word -> frequency

This allows the program to efficiently count how many times a word
appears.

### HashSet

HashSet is used to store unique words.

It removes duplicate words and helps in calculating the similarity
between two lyrics.

### ArrayList

ArrayList is used to store:

- Words
- Matching passages
- KMP search positions

### Arrays

Arrays are used to store similarity results before sorting.

---

## 5. Algorithms Used

### 5.1 Text Processing

The input lyrics are converted to lowercase and special characters
are removed.

This makes the comparison more consistent.

---

### 5.2 Jaccard Similarity

Jaccard Similarity compares the unique words present in the query and
the song.

Formula:

Jaccard Similarity =

Number of Common Words / Number of Unique Words

The result is converted into a percentage.

---

### 5.3 Merge Sort

Merge Sort is used to rank songs according to their similarity scores.

The songs are arranged from the highest similarity score to the lowest.

Time Complexity:

O(n log n)

Space Complexity:

O(n)

---

### 5.4 KMP Pattern Matching

The Knuth-Morris-Pratt algorithm is used to search for matching
patterns inside song lyrics.

KMP avoids unnecessary comparisons by using the Longest Prefix Suffix
(LPS) array.

Time Complexity:

O(n + m)

where:

n = length of text

m = length of pattern

---

### 5.5 Passage Matching

The PassageMatcher divides the query lyrics into groups of three words
and uses KMP to search for these phrases in the stored songs.

Matching overlapping phrases can be combined into longer matching
passages.

---

## 6. Project Workflow

The project follows these steps:

1. User enters lyrics.
2. The text is cleaned and normalized.
3. Words are extracted from the input.
4. Unique words are identified.
5. Lyrics files are loaded from the lyrics folder.
6. Jaccard similarity is calculated for every song.
7. Similarity results are sorted using Merge Sort.
8. KMP searches for matching lyric passages.
9. The system generates an attribution report.
10. The user can perform another search.

---

## 7. Project Structure

LyricsSimilarity/

    algorithms/
        WordAnalyzer.java
        SimilarityCalculator.java
        SimilarityResult.java
        MergeSorter.java
        KMPMatcher.java
        PassageMatcher.java
        ComplexityAnalyzer.java

    lyrics/
        song1.txt
        song2.txt
        song3.txt
        song4.txt

    model/

    utils/
        LyricsReader.java
        TextProcessor.java

    Main.java

    README.md

---

## 8. Main Features

### Search Lyrics

The user can enter lyrics and compare them with all stored songs.

### Ranked Similarity Results

Songs are ranked from highest to lowest based on their similarity
score.

### Similar Passage Detection

The system identifies matching three-word passages using KMP.

### Lyrics Catalogue

The user can view all lyrics stored in the system.

### Algorithm Complexity

The application displays the time and space complexity of the
algorithms used.

### Attribution Report

The system displays:

- Top similarity candidate
- Similarity score
- Similarity level
- Passage match status

---

## 9. Sample Input

walking through the night looking for someone

---

## 10. Sample Output

RANKED SIMILARITY RESULTS

1. song2.txt -> 37.50%
2. song1.txt -> 33.33%
3. song4.txt -> 20.00%
4. song3.txt -> 4.17%

TOP SIMILARITY CANDIDATE

song2.txt -> 37.50%

---

## 11. Complexity Summary

| Component | Time Complexity |
|-----------|-----------------|
| Text Processing | O(n) |
| Word Analysis | O(n) |
| Jaccard Similarity | O(n + m) |
| Merge Sort | O(n log n) |
| KMP Matching | O(n + m) |
| Passage Matching | Depends on query length |

---

## 12. Limitations

The current system uses word-based similarity and exact pattern
matching.

Therefore:

- Synonyms are not identified.
- Similar meanings with different words may not receive a high score.
- The system works with the lyrics stored in the local lyrics folder.
- Similarity scores do not prove actual authorship or ownership.

---

## 13. Future Enhancements

Possible future improvements include:

- Adding a graphical user interface.
- Supporting larger lyrics datasets.
- Adding semantic similarity.
- Using TF-IDF based comparison.
- Adding plagiarism visualization.
- Supporting database storage.
- Adding more advanced text matching techniques.
- Exporting analysis reports.

---

## 14. Conclusion

The Lyrics Similarity and Attribution project demonstrates how Data
Structures and Algorithms can be applied to a practical text-analysis
problem.

The combination of HashMap, HashSet, ArrayList, Jaccard Similarity,
Merge Sort, and KMP Pattern Matching allows the system to compare
lyrics, rank songs, and identify matching passages efficiently.

The project provides a simple foundation for further development into
a larger lyric analysis and attribution system.