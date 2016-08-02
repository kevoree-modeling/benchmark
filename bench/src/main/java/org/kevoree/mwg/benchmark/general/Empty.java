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
        runner.list();
        try {
            Collection<RunResult> result = runner.run();
//            BenchmarkParams params = result.getParams();
//            System.out.println(params.getBenchmark());
            System.err.println(result.size());
        } catch (RunnerException ex) {
            System.err.println("titi");
            System.err.println(ex.getCause());
//            StringWriter stringWriter = new StringWriter();
//            PrintWriter pw = new PrintWriter(stringWriter);
//            ex.getCause().printStackTrace(pw);
//            System.err.println(stringWriter.toString());

        }

    }
}
