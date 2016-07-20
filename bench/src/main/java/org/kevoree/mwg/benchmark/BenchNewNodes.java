package org.kevoree.mwg.benchmark;

import org.mwg.Callback;
import org.mwg.Node;

/**
 * Created by assaad on 13/07/16.
 */
public class BenchNewNodes extends AbstractBenchmark {

    public BenchNewNodes(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "Insert new nodes Benchmark, "+getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        Node insert = graph.newNode(0, 0);
        insert.set(value,random.nextDouble());
        insert.free();
        callback.on(true);
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}
