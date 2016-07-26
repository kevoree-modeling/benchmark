package org.kevoree.mwg.jmh.general;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class JavaRandom {

    @State(Scope.Thread)
    public static class MyRandom {
        Random random = new Random(1256335488963325663L);
    }

    @Benchmark
    @Fork(10)
    @Warmup(iterations = 100, batchSize = 1)
    @Measurement(iterations = 100_000,batchSize = 1)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public long benchJavaRandom(MyRandom myRandom) {
        return myRandom.random.nextLong();
    }
}
