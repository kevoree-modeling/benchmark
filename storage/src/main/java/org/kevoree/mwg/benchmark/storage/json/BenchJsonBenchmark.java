package org.kevoree.mwg.benchmark.storage.json;

public class BenchJsonBenchmark {
    private String benchmark;
    private String mode;
    private int threads;
    private int forks;
    private int warmupIterations;
    private String warmupTime;
    private int warmupBatchSize;
    private int measurementIterations;
    private String measurementTime;
    private int measurementBatchSize;
    private BenchJsonMetrics primaryMetric;

    public String getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getWarmupIterations() {
        return warmupIterations;
    }

    public void setWarmupIterations(int warmupIterations) {
        this.warmupIterations = warmupIterations;
    }

    public String getWarmupTime() {
        return warmupTime;
    }

    public void setWarmupTime(String warmupTime) {
        this.warmupTime = warmupTime;
    }

    public int getWarmupBatchSize() {
        return warmupBatchSize;
    }

    public void setWarmupBatchSize(int warmupBatchSize) {
        this.warmupBatchSize = warmupBatchSize;
    }

    public int getMeasurementIterations() {
        return measurementIterations;
    }

    public void setMeasurementIterations(int measurementIterations) {
        this.measurementIterations = measurementIterations;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public int getMeasurementBatchSize() {
        return measurementBatchSize;
    }

    public void setMeasurementBatchSize(int measurementBatchSize) {
        this.measurementBatchSize = measurementBatchSize;
    }

    public BenchJsonMetrics getPrimaryMetric() {
        return primaryMetric;
    }

    public void setPrimaryMetric(BenchJsonMetrics primaryMetric) {
        this.primaryMetric = primaryMetric;
    }
}
