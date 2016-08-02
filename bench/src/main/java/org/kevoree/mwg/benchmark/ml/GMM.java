package org.kevoree.mwg.benchmark.ml;

import org.mwg.Callback;
import org.mwg.Graph;
import org.mwg.GraphBuilder;
import org.mwg.ml.MLPlugin;
import org.mwg.ml.algorithm.profiling.GaussianMixtureNode;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GMM {
    @State(Scope.Thread)
    public static class Parameter {
        ArrayList<double[]> vecs=new ArrayList<double[]>();
        GaussianMixtureNode profiler;
        int counter;

        long startAvailableSpace;

        @Param(value = {"true"})
        boolean useHeap;

        @Param("1000000")
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
                    startAvailableSpace = graph.space().available();

                    profiler = (GaussianMixtureNode) graph.newTypedNode(0,0, GaussianMixtureNode.NAME);
                    int maxlev = 3;
                    int width =50;
                    double factor=1.8;
                    int iter=20;
                    double threshold =1.6;
                    double[] err = new double[]{0.5 * 0.5, 10 * 10};

                    profiler.set(GaussianMixtureNode.LEVEL, maxlev); //max levels allowed
                    profiler.set(GaussianMixtureNode.WIDTH, width); //each level can have 24 components
                    profiler.set(GaussianMixtureNode.COMPRESSION_FACTOR, factor); //Factor of times before compressing, so at 24x10=240, compressions executes
                    profiler.set(GaussianMixtureNode.COMPRESSION_ITER, iter); //iteration in the compression function, keep default
                    profiler.set(GaussianMixtureNode.THRESHOLD, threshold); //At the lower level, at higher level will be: threashold + level/2 -> number of variance tolerated to insert in the same node
                    profiler.set(GaussianMixtureNode.PRECISION, err); //Minimum covariance in both axis


                    double[] v= new double[2];

                    for(int i=0;i<100_000;i++){
                        v[0]=random.nextInt(48);
                        v[0]=v[0]/2;
                        v[1]=random.nextDouble()*2000;

                        vecs.add(v);
                    }

                }
            });
        }

        @TearDown
        public void end() {
            Graph graph = profiler.graph();
            profiler.free();
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
    public Object benchGMM(Parameter param) {
        param.profiler.learnVector(param.vecs.get(param.counter),null);
        return param.counter++;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(GMM.class.getSimpleName())
                .forks(1)
                .param("useHeap","false")
                .build();
        new Runner(opt).run();
    }
}