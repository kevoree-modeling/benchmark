package org.kevoree.mwg.benchmark.core;

import org.kevoree.mwg.benchmark.AbstractBenchmark;
import org.mwg.Callback;
import org.mwg.DeferCounter;
import org.mwg.Node;
import org.mwg.plugin.Job;

/**
 * Created by assaad on 15/07/16.
 */
public class BenchWorldStairRead extends AbstractBenchmark {

    private Node node;
    private long[] currentWorld;

    public BenchWorldStairRead(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "World read stair Benchmark, "+getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        node=graph.newNode(0,0);
        currentWorld=new long[rounds];

        final DeferCounter defer=graph.newCounter(rounds-1);
        for(int i=0;i<rounds-1;i++){
            node.set(value,i);
            currentWorld[i+1]=graph.fork(currentWorld[i]);
            graph.lookup(currentWorld[i+1], i+1, node.id(), new Callback<Node>() {
                public void on(Node result) {
                    node.free();
                    node=result;
                    defer.count();
                }
            });
        }
        node.set(value,rounds-1);
        defer.then(new Job() {
            public void run() {
                callback.on(true);
            }
        });

    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        graph.lookup(currentWorld[rounds-1], num, node.id(), new Callback<Node>() {
            public void on(Node result) {
                if(((Integer)result.get(value))!=num){
                    throw new RuntimeException("Incorrect result, expected: "+num+" got: "+(Integer)result.get(value));
                }
                result.free();
                callback.on(true);
            }
        });
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}
