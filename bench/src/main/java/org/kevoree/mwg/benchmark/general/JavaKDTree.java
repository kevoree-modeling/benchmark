package org.kevoree.mwg.benchmark.general;

import org.kevoree.mwg.benchmark.utils.KDNodeJava;
import org.mwg.ml.common.distance.EuclideanDistance;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JavaKDTree {

    @State(Scope.Thread)
    public static class Parameter {

        KDNodeJava root;
        double[] keys;
        Object value;

        private Random random;

        @Setup(Level.Trial)
        public void benchSetup() {
            random = new Random(1256335488963325663L);
            root=new KDNodeJava();
            root.setThreshold(1e-30);
            root.setDistance(new EuclideanDistance());

            keys = new double[4];
        }

        @Setup(Level.Invocation)
        public void methodSetup() {
            for(int i=0;i<keys.length;i++) {
                keys[i] = random.nextDouble();
            }
            value = new Object();
        }

    }


    @Benchmark
    @Fork(10)
    @Warmup(iterations = 1,batchSize = 1, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 1,batchSize = 1, time = 5, timeUnit = TimeUnit.SECONDS)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object javaKDTree(Parameter parameter) {
        parameter.root.insert(parameter.keys,parameter.value,null);
        return null;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JavaKDTree.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
