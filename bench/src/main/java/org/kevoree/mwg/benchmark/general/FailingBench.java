package org.kevoree.mwg.benchmark.general;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by ludovicmouline on 02/08/16.
 */
public class FailingBench {

    @Benchmark
    @Fork(10)
    @Warmup(iterations = 1,batchSize = 1, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 1,batchSize = 1, time = 5, timeUnit = TimeUnit.SECONDS)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object failingBench() {
        throw new RuntimeException("An exception");
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FailingBench.class.getSimpleName())
                .forks(3)
                .verbosity(VerboseMode.NORMAL)
                .shouldFailOnError(true)
                .build();


        Runner runner = new Runner(opt);
        runner.list();
        try {
            Collection<RunResult> result = runner.run();
            System.err.println(result.size());
        } catch (RunnerException ex) {
            System.err.println(ex.getCause());
        }

    }
}
