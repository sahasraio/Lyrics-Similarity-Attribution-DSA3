package algorithms;

public class SimilarityResult {

    String songName;
    double score;

    public SimilarityResult(String songName, double score) {
        this.songName = songName;
        this.score = score;
    }

    public String getSongName() {
        return songName;
    }

    public double getScore() {
        return score;
    }
}