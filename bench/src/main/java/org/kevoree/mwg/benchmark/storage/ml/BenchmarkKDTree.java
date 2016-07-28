package org.kevoree.mwg.benchmark.storage.ml;

import org.kevoree.mwg.benchmark.storage.AbstractBenchmark;
import org.mwg.Callback;
import org.mwg.Node;
import org.mwg.ml.common.structure.KDNode;

import java.util.ArrayList;

/**
 * Created by assaad on 21/07/16.
 */
public class BenchmarkKDTree extends AbstractBenchmark {

    public int dim=4;
    private ArrayList<double[]> vecs=new ArrayList<double[]>();
    private Node[] values;
    KDNode root;


    public BenchmarkKDTree(int roundsBefore, int rounds, int displayEach, boolean useOffHeap, int cachesize) {
        super(roundsBefore, rounds, displayEach, useOffHeap, cachesize);
    }


    protected String getName() {
        return "KDTree java Benchmark, " + getGraphSettings();
    }

    protected void runBeforeBench(final Callback<Boolean> callback) {
        root = (KDNode) graph.newTypedNode(0,0,KDNode.NAME);
        root.set(KDNode.DISTANCE_THRESHOLD,1e-30);

        values=new Node[rounds];

        for(int i=0;i<rounds;i++){
            double[] v= new double[dim];
            for(int j=0;j<dim;j++){
                v[j]=random.nextDouble();
            }
            vecs.add(v);
            values[i]= graph.newNode(0,0);
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