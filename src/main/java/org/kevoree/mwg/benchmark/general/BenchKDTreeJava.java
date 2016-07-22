package org.kevoree.mwg.benchmark.general;

import org.kevoree.mwg.benchmark.AbstractBenchmark;
import org.kevoree.mwg.benchmark.utils.KDNodeJava;
import org.mwg.Callback;
import org.mwg.ml.common.distance.EuclideanDistance;

import java.util.ArrayList;

/**
 * Created by assaad on 21/07/16.
 */
public class BenchKDTreeJava extends AbstractBenchmark {

    public int dim=4;
    private ArrayList<double[]> vecs=new ArrayList<double[]>();
    private Object[] values;
    KDNodeJava root=new KDNodeJava();


    public BenchKDTreeJava(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "KDTree java Benchmark, " + getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        root.setThreshold(1e-30);
        root.setDistance(new EuclideanDistance());
        values=new Object[rounds];

        for(int i=0;i<rounds;i++){
            double[] v= new double[dim];
            for(int j=0;j<dim;j++){
                v[j]=random.nextDouble();
            }
            vecs.add(v);
            values[i]=new Object();
        }

        callback.on(true);
    }

    protected void oneRoundBench(final int num, final boolean warmup, final Callback<Boolean> callback) {
        root.insert(vecs.get(num), values[num], new Callback<Boolean>() {
            public void on(Boolean result) {
                callback.on(true);
            }
        });

    }

    protected void runAfterBench(final Callback<Boolean> callback) {
        callback.on(true);
    }
}