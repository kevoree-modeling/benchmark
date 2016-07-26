package org.kevoree.mwg.jmh.ml;

import org.mwg.Callback;
import org.mwg.Graph;
import org.mwg.GraphBuilder;
import org.mwg.Node;
import org.mwg.ml.MLPlugin;
import org.mwg.ml.common.structure.KDNode;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by ludovicmouline on 26/07/16.
 */
public class KDTree {
    @State(Scope.Thread)
    public static class Parameter {
        int dim=4;
        ArrayList<double[]> vecs=new ArrayList<double[]>();
        Node[] values;
        KDNode root;
        int counter;

        @Param(value = {"false","true"})
        boolean useHeap;

        @Param("500000")
        long cacheSize;

        @Setup
        public void setup() {
            GraphBuilder graphBuilder = new GraphBuilder();
            graphBuilder.withMemorySize(cacheSize).withPlugin(new MLPlugin());
            if(!useHeap) {
                graphBuilder.withOffHeapMemory();
            }
            Graph graph = graphBuilder.build();

            Random random = new Random(12345L);

            graph.connect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    root = (KDNode) graph.newTypedNode(0,0,KDNode.NAME);
                    root.set(KDNode.DISTANCE_THRESHOLD,1e-30);

                    values=new Node[1_000_000];

                    for(int i=0;i<1_000_000;i++){
                        double[] v= new double[dim];
                        for(int j=0;j<dim;j++){
                            v[j]=random.nextDouble();
                        }
                        vecs.add(v);
                        values[i]= graph.newNode(0,0);
                    }

                }
            });
        }

    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(10)
    @Warmup(iterations = 100, batchSize = 1)
    @Measurement(iterations = 1_000_000, batchSize = 1)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void newNodes(Parameter param) {
        param.root.insert(param.vecs.get(param.counter), param.values[param.counter], null);
        param.counter++;
    }
}
