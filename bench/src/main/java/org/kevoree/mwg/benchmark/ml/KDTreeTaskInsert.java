package org.kevoree.mwg.benchmark.ml;

import org.kevoree.mwg.benchmark.utils.MWGUtil;
import org.mwg.Callback;
import org.mwg.Graph;
import org.mwg.GraphBuilder;
import org.mwg.Node;
import org.mwg.ml.MLPlugin;
import org.mwg.ml.common.structure.KDTree;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class KDTreeTaskInsert {
    @State(Scope.Thread)
    public static class Parameter {
        int dim=4;
        ArrayList<double[]> vecs=new ArrayList<double[]>();
        Node[] values;
        KDTree root;
        int counter;

        long startAvailableSpace;



//        @Param(value = {"false","true"})
        @Param("true")
        boolean useHeap;

        @Param("10000000")
        long cacheSize;

        @Setup
        public void setup() {
            GraphBuilder graphBuilder = new GraphBuilder();
            graphBuilder.withMemorySize(cacheSize).withPlugin(new MLPlugin());
            if(!useHeap) {
                MWGUtil.offHeap(graphBuilder);
            }
            Graph graph = graphBuilder.build();

            Random random = new Random(12345L);

            graph.connect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    startAvailableSpace = graph.space().available();

                    root = (KDTree) graph.newTypedNode(0,0, KDTree.NAME);
                    root.set(KDTree.DISTANCE_THRESHOLD,1e-30);

                    values=new Node[1_00_000];

                    for(int i=0;i<1_00_000;i++){
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

        @TearDown
        public void end() {
            for(int i=0;i<values.length;i++) {
                values[i].free();
            }
            Graph graph = root.graph();
            root.free();
            graph.save(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    long endAvailableSpace = graph.space().available();
                    if(endAvailableSpace != startAvailableSpace) {
                        throw new RuntimeException("Memory leak detected: startAvailableSpace=" + startAvailableSpace + "; endAvailableSpace=" + endAvailableSpace + "; diff= " + (startAvailableSpace - endAvailableSpace));
                    }
                }
            });

        }

    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(10)
    @Warmup(iterations = 0)
    @Measurement(iterations = 1, batchSize = 100_000)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Timeout(time = 5, timeUnit = TimeUnit.MINUTES)
    public Object KDTreeTaskInsert(Parameter param) {
        param.root.insert(param.vecs.get(param.counter), param.values[param.counter], null);
        return param.counter++;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(KDTreeTaskInsert.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
