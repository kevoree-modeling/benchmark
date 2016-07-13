package org.kevoree.mwg.benchmark;

import org.mwg.Callback;
import org.mwg.Node;

/**
 * Created by assaad on 13/07/16.
 */
public class BenchTimeLineInsert extends AbstractBenchmark {

    private Node node;
    private static String value="value";

    public BenchTimeLineInsert(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "Timeline insert Benchmark, "+getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        node=graph.newNode(0,0);
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        if(warmup){
            node.jump(num, new Callback<Node>() {
                public void on(Node result) {
                    result.set(value,num*0.1);
                    result.free();
                    callback.on(true);
                }
            });
        }
        else {
            node.jump(num+roundsBefore, new Callback<Node>() {
                public void on(Node result) {
                    result.set(value,num*0.1);
                    result.free();
                    callback.on(true);
                }
            });
        }
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}
