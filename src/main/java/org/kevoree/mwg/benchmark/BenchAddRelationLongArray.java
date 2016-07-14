package org.kevoree.mwg.benchmark;

import org.mwg.Callback;

/**
 * Created by assaad on 14/07/16.
 */
public class BenchAddRelationLongArray extends AbstractBenchmark {

    int count = 0;
    int limit = 2;
    private long[] root = new long[limit];

    public BenchAddRelationLongArray(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "Add Relations Benchmark, " + getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {

        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        if(count<limit){
            root[count]=num;
            count++;
            callback.on(true);
            return;
        }
        else{
            limit=(int)(limit*1.7)+1;
//            limit=limit+1;
            long[] temp=new long[limit];
            System.arraycopy(root, 0, temp, 0, root.length);
            temp[root.length] = num;
            root = temp;
            count++;
            callback.on(true);
            return;
        }
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}