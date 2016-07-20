package org.kevoree.mwg.benchmark;

import org.mwg.Callback;
import org.mwg.Node;

/**
 * Created by assaad on 15/07/16.
 */
public class BenchWorldInsert extends AbstractBenchmark {

    private Node node;
    private long currentWorld=0;

    public BenchWorldInsert(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "World insert Benchmark, "+getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        node=graph.newNode(0,0);
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        node.set(value,num);
        currentWorld=graph.fork(currentWorld);
        graph.lookup(currentWorld, 0, node.id(), new Callback<Node>() {
            public void on(Node result) {
                node.free();
                node=result;
                callback.on(true);
            }
        });
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}
