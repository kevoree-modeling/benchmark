package org.kevoree.mwg.benchmark.connector;

public class BenchData {
    private String bench;
    private Double benchmarkSpeed;
    private Configuration configuration;

    public String getBench() {
        return bench;
    }

    public void setBench(String bench) {
        this.bench = bench;
    }

    public Double getBenchmarkSpeed() {
        return benchmarkSpeed;
    }

    public void setBenchmarkSpeed(Double benchmarkSpeed) {
        this.benchmarkSpeed = benchmarkSpeed;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return bench + "(" + configuration.toString() + ")";
    }

    @Override
    public String toString() {
        return  getId() + " = " + benchmarkSpeed;
    }
}
