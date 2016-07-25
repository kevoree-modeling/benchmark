package org.kevoree.mwg.jmh.general;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class JavaRandom {

    @State(Scope.Thread)
    public static class MyRandom {
        public Random random = new Random(1256335488963325663L);
    }

    @Benchmark
    @Warmup(batchSize = 1)
    @Measurement(batchSize = 1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public long benchJavaRandom(MyRandom myRandom) {
        return myRandom.random.nextLong();
    }
}
