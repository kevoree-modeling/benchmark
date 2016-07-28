package org.kevoree.mwg.benchmark.storage.core;

import org.kevoree.mwg.benchmark.storage.AbstractBenchmark;
import org.mwg.Callback;
import org.mwg.Node;

/**
 * Created by assaad on 13/07/16.
 */
public class BenchLookupNodes extends AbstractBenchmark {

    private long[] ids;

    public BenchLookupNodes(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
        ids = new long[rounds];
    }


    protected String getName() {
        return "Lookup Benchmark, " + getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        for (int i = 0; i < rounds; i++) {
            Node insert = graph.newNode(0, 0);
            insert.set(value,random.nextDouble());
            ids[i]=insert.id();
            insert.free();
        }
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        graph.lookup(0, 0, ids[num], new Callback<Node>() {
            public void on(Node result) {
                result.free();
                callback.on(true);
            }
        });

    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}
