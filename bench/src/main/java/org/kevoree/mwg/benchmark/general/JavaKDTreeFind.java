package org.kevoree.mwg.benchmark.general;

import org.kevoree.mwg.benchmark.utils.KDNodeJava;
import org.mwg.Callback;
import org.mwg.structure.distance.EuclideanDistance;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JavaKDTreeFind {

    @State(Scope.Thread)
    public static class Parameter {
        int dim=4;
        ArrayList<double[]> vecs=new ArrayList<double[]>();

        KDNodeJava root;
        double[] keys;

        private Random random;

        @Setup
        public void benchSetup() {
            random = new Random(1256335488963325663L);
            root=new KDNodeJava();
            root.setThreshold(1e-30);
            root.setDistance(EuclideanDistance.instance());

            keys = new double[dim];
            for(int i=0;i<100000;i++){
                for(int j=0;j<4;j++){
                    keys[j]=random.nextDouble();
                }
                root.insert(keys, new Object(), null);
            }

        }

        @Setup(Level.Invocation)
        public void methodSetup() {
            for(int i=0;i<keys.length;i++) {
                keys[i] = random.nextDouble();
            }
        }

    }


    @Benchmark
    @Fork(10)
    @Warmup(iterations = 1,batchSize = 1, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 1,batchSize = 1, time = 5, timeUnit = TimeUnit.SECONDS)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object KDTreeRead(Parameter parameter) {
        parameter.root.nearestN(parameter.keys, 1, new Callback<Object[]>() {
            @Override
            public void on(Object[] result) {

            }
        });
        return null;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JavaKDTreeFind.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
