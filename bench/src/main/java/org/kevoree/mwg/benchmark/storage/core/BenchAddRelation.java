package org.kevoree.mwg.benchmark.storage.core;

import org.kevoree.mwg.benchmark.storage.AbstractBenchmark;
import org.mwg.Callback;
import org.mwg.Node;

/**
 * Created by assaad on 14/07/16.
 */
public class BenchAddRelation extends AbstractBenchmark {

    private Node root;
    private Node[] children;

    public BenchAddRelation(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
        children = new Node[rounds];
    }


    protected String getName() {
        return "Add Relations Benchmark, " + getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        root = graph.newNode(0, 0);

        for (int i = 0; i < rounds; i++) {
            children[i] = graph.newNode(0, 0);
        }
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        root.add(relation, children[num]);
        callback.on(true);
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}