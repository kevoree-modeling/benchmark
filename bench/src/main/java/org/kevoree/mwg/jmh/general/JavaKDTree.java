package org.kevoree.mwg.jmh.general;

import org.kevoree.mwg.benchmark.storage.utils.KDNodeJava;
import org.mwg.ml.common.distance.EuclideanDistance;
import org.openjdk.jmh.annotations.*;

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
    @Fork(1)
    @Warmup(iterations = 1,batchSize = 1, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 1,batchSize = 1, time = 10, timeUnit = TimeUnit.SECONDS)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object javaKDTree(Parameter parameter) {
        parameter.root.insert(parameter.keys,parameter.value,null);
        return null;
    }
}
