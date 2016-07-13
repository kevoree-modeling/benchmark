package org.kevoree.mwg.benchmark;

import org.mwg.Callback;

/**
 * Created by assaad on 13/07/16.
 */
public class BenchNormalCounter extends AbstractBenchmark {

    int i = 0;

    public BenchNormalCounter(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "Normal Counter Benchmark, "+getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        i++;
        callback.on(true);
    }

    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}
