package algorithms;

public class MergeSorter {

    public static void sort(SimilarityResult[] results) {

        if (results.length < 2) {
            return;
        }

        int middle = results.length / 2;

        SimilarityResult[] left =
                new SimilarityResult[middle];

        SimilarityResult[] right =
                new SimilarityResult[results.length - middle];

        for (int i = 0; i < middle; i++) {
            left[i] = results[i];
        }

        for (int i = middle; i < results.length; i++) {
            right[i - middle] = results[i];
        }

        sort(left);
        sort(right);

        merge(results, left, right);
    }

    private static void merge(
            SimilarityResult[] results,
            SimilarityResult[] left,
            SimilarityResult[] right) {

        int i = 0;
        int j = 0;
        int k = 0;

        while (i < left.length && j < right.length) {

            if (left[i].getScore() >= right[j].getScore()) {

                results[k] = left[i];
                i++;

            } else {

                results[k] = right[j];
                j++;
            }

            k++;
        }

        while (i < left.length) {
            results[k] = left[i];
            i++;
            k++;
        }

        while (j < right.length) {
            results[k] = right[j];
            j++;
            k++;
        }
    }
}