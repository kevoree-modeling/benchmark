package org.kevoree.mwg.benchmark.general;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class JavaRandom {

    @State(Scope.Thread)
    public static class MyRandom {
        Random random = new Random(1256335488963325663L);
    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 1, batchSize = 1, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 1,batchSize = 1, time = 10, timeUnit = TimeUnit.SECONDS)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public long benchJavaRandom(MyRandom myRandom) {
        return myRandom.random.nextLong();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JavaRandom.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
