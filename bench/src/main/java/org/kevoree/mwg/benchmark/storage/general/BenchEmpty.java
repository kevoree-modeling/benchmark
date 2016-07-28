package org.kevoree.mwg.benchmark.storage.general;

import org.kevoree.mwg.benchmark.storage.AbstractBenchmark;
import org.mwg.Callback;

/**
 * Created by assaad on 13/07/16.
 */
public class BenchEmpty extends AbstractBenchmark {

    public BenchEmpty(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "Empty Benchmark, "+getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        callback.on(true);
    }

    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}
