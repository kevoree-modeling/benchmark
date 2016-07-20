package org.kevoree.mwg.benchmark;

import org.mwg.Callback;
import org.mwg.Node;

/**
 * Created by assaad on 14/07/16.
 */
public class BenchAddRelationRandomGraph extends AbstractBenchmark {

    private static int NUMNODES=100000;
    private Node[] children;
    private int[] randomNum;

    public BenchAddRelationRandomGraph(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
        children = new Node[NUMNODES];
    }


    protected String getName() {
        return "Add Relations Benchmark, " + getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {

        for (int i = 0; i < NUMNODES; i++) {
            children[i] = graph.newNode(0, 0);
        }
        randomNum=new int[2*rounds];

        for(int i=0;i<2*rounds;i++){
            randomNum[i]=random.nextInt(NUMNODES);
        }
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        children[randomNum[num]].add(relation,children[randomNum[num+rounds]]);
        callback.on(true);
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}