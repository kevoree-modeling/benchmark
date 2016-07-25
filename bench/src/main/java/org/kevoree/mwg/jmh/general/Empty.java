package org.kevoree.mwg.jmh.general;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;


public class Empty {

    @Benchmark
    @Warmup(batchSize = 1)
    @Measurement(batchSize = 1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object emptyBench() {
        return null;
    }
}
