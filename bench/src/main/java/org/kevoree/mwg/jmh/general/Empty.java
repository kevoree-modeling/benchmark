package org.kevoree.mwg.jmh.general;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;


public class Empty {

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 1,batchSize = 1, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 1,batchSize = 1, time = 5, timeUnit = TimeUnit.SECONDS)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object emptyBench() {
        return null;
    }
}
