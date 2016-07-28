package org.kevoree.mwg.benchmark.storage.json;

public class BenchJsonMetrics {
    private double score;
    private double scoreError;
    private String scoreUnit;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScoreError() {
        return scoreError;
    }

    public void setScoreError(double scoreError) {
        this.scoreError = scoreError;
    }

    public String getScoreUnit() {
        return scoreUnit;
    }

    public void setScoreUnit(String scoreUnit) {
        this.scoreUnit = scoreUnit;
    }
}
