package algorithms;

public class ComplexityAnalyzer {

    public static void displayComplexities() {

        System.out.println("\n==========================================");
        System.out.println("          ALGORITHM COMPLEXITY");
        System.out.println("==========================================");

        System.out.println("\n1. Text Processing");
        System.out.println("   Time Complexity : O(n)");
        System.out.println("   Space Complexity: O(n)");

        System.out.println("\n2. Word Frequency Analysis");
        System.out.println("   Time Complexity : O(n)");
        System.out.println("   Space Complexity: O(n)");

        System.out.println("\n3. Jaccard Similarity");
        System.out.println("   Time Complexity : O(n + m)");
        System.out.println("   Space Complexity: O(n + m)");

        System.out.println("\n4. Merge Sort");
        System.out.println("   Time Complexity : O(n log n)");
        System.out.println("   Space Complexity: O(n)");

        System.out.println("\n5. KMP Pattern Matching");
        System.out.println("   Time Complexity : O(n + m)");
        System.out.println("   Space Complexity: O(m)");

        System.out.println("\n6. Passage Matching");
        System.out.println("   Uses KMP pattern matching");
        System.out.println("   Time Complexity : Depends on query length");
        System.out.println("   Space Complexity: O(m)");

        System.out.println("\n==========================================");
    }
}