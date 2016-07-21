package org.kevoree.mwg.benchmark.general;

import org.kevoree.mwg.benchmark.AbstractBenchmark;
import org.mwg.Callback;
import org.mwg.core.utility.PrimitiveHelper;


/**
 * Created by assaad on 14/07/16.
 */
public class BenchHashFunction extends AbstractBenchmark {

    private static byte p0 = 0;
    long[] ids;

    public BenchHashFunction(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
        ids = new long[3 * rounds];
    }


    protected String getName() {
        return "Triple Hash function Benchmark, " + getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        for (int i = 0; i < 3 * rounds; i++) {
            ids[i] = random.nextLong();
        }
        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        PrimitiveHelper.tripleHash(p0, ids[num], ids[num+rounds], ids[num+2*rounds], cachesize);
        callback.on(true);
    }


    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}
