package org.kevoree.mwg.benchmark.storage.general;

import org.kevoree.mwg.benchmark.storage.AbstractBenchmark;
import org.mwg.Callback;

/**
 * Created by assaad on 14/07/16.
 */
public class BenchJavaRandom extends AbstractBenchmark {


    public BenchJavaRandom(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "Java random Benchmark, " + getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        random.nextLong();
        callback.on(true);
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}