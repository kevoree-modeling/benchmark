package org.kevoree.mwg.benchmark.general;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.concurrent.TimeUnit;


public class Empty{

    @Benchmark
    @Fork(10)
    @Warmup(iterations = 1,batchSize = 1, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 1,batchSize = 1, time = 5, timeUnit = TimeUnit.SECONDS)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object emptyBench() {
        return null;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Empty.class.getSimpleName())
                .forks(3)
                .verbosity(VerboseMode.NORMAL)
                .shouldFailOnError(true)
                .build();


        Runner runner = new Runner(opt);
        runner.run();
    }
}
