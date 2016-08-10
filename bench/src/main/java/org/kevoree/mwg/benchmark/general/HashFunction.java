package org.kevoree.mwg.benchmark.general;

import org.mwg.utility.HashHelper;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class HashFunction {

    @State(Scope.Thread)
    public static class Parameters {
        private Random random = new Random(1256335488963325663L);
        public long v1 = random.nextLong();
        public long v2 = random.nextLong();
        public long v3 = random.nextLong();

        @Param("10000")
        public int cacheSize;
    }

    @Benchmark
    @Fork(10)
    @Warmup(iterations = 1,batchSize = 1, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 1,batchSize = 1, time = 5, timeUnit = TimeUnit.SECONDS)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public long javaHashFunction(Parameters parameters) {
        return HashHelper.tripleHash((byte)0,parameters.v1,parameters.v2,parameters.v3,parameters.cacheSize);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HashFunction.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
