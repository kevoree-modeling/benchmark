package org.kevoree.mwg.benchmark.storage.core;

import org.kevoree.mwg.benchmark.storage.AbstractBenchmark;
import org.mwg.Callback;
import org.mwg.DeferCounter;
import org.mwg.Node;
import org.mwg.plugin.Job;

/**
 * Created by assaad on 13/07/16.
 */
public class BenchTimeLineRead extends AbstractBenchmark {

    private Node node;

    public BenchTimeLineRead(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "Timeline read Benchmark, "+getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        node=graph.newNode(0,0);
        final DeferCounter defer=graph.newCounter(roundsBefore+rounds);
        for(int i=0;i<roundsBefore+rounds;i++){
            final int finalI = i;
            node.jump(i, new Callback<Node>() {
                public void on(Node result) {
                    result.set(value, finalI *0.1);
                    defer.count();
                    result.free();
                }
            });
        }
        defer.then(new Job() {
            public void run() {
                callback.on(true);
            }
        });
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        node.jump(num, new Callback<Node>() {
            public void on(Node result) {
                result.get(value);
                result.free();
                callback.on(true);
            }
        });
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}
