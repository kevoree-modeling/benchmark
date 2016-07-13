package org.kevoree.mwg.benchmark;

import org.mwg.Callback;
import org.mwg.GraphBuilder;

/**
 * Created by assaad on 13/07/16.
 */
public class CounterBenchmark extends AbstractBenchmark {

    int i = 0;

    public CounterBenchmark(int roundsBefore, int rounds, int displayEach) {
        super(roundsBefore, rounds, displayEach);
    }

    protected String getName() {
        return "Normal Counter Benchmark";
    }

    protected void initializeGraph(Callback<Boolean> callback) {
        graph = new GraphBuilder().build();
        graph.connect(callback);
    }

    protected void runBeforeBench(Callback<Boolean> callback) {
        callback.on(true);
    }

    protected void oneRoundBench(Callback<Boolean> callback) {
        i++;
        callback.on(true);
    }

    protected void runAfterBench(Callback<Boolean> callback) {
        callback.on(true);
    }
}
