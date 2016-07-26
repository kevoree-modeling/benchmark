package org.kevoree.mwg.jmh.general;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;


public class Empty {

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 10, batchSize = 1)
    @Measurement(iterations = 100_000, batchSize = 1)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object emptyBench() {
        return null;
    }
}
