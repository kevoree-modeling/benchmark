package org.kevoree.mwg.benchmark.core;

import org.kevoree.mwg.benchmark.utils.MWGUtil;
import org.mwg.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WorldRead {
    @State(Scope.Thread)
    public static class Parameter {
        Graph graph;
        Node node;
        int counter;
        long[] worlds;
        long startAvailableSpace;

        @Param(value = {"false", "true"})
        boolean useHeap;

        @Param("5000000")
        long cacheSize;


        @Setup
        public void setup() {
            GraphBuilder graphBuilder = new GraphBuilder();
            graphBuilder.withMemorySize(cacheSize);
            if (!useHeap) {
                MWGUtil.offHeap(graphBuilder);
            }
            graph = graphBuilder.build();

            worlds = new long[1_000_010];

            Random rand = new Random(1256335488963325663L);
            graph.connect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    startAvailableSpace = graph.space().available();
                    node = graph.newNode(0, 0);

                    worlds[0] = 0L;
                    for (int i = 1; i < 1_000_010; i++) {
                        worlds[i] = graph.fork(worlds[0]);
                        graph.lookup(worlds[i], 0, node.id(), new Callback<Node>() {
                            @Override
                            public void on(Node result) {
                                result.set("value", Type.DOUBLE, rand.nextDouble());
                                result.free();
                            }
                        });

                    }
                }
            });
        }

        @TearDown
        public void tearDown() {
            node.free();
            graph.save(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    long endAvailableSpace = graph.space().available();
                    if (endAvailableSpace != startAvailableSpace) {
                        throw new RuntimeException("Memory leak detected: startAvailableSpace=" + startAvailableSpace + "; endAvailableSpace=" + endAvailableSpace + "; diff= " + (startAvailableSpace - endAvailableSpace));
                    }
                }
            });
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(10)
    @Warmup(iterations = 1, batchSize = 10)
    @Measurement(iterations = 1, batchSize = 1_000_000)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Timeout(time = 5, timeUnit = TimeUnit.MINUTES)
    public Object benchWorldRead(Parameter param) {
        param.graph.lookup(param.worlds[param.counter], 0, param.node.id(), new Callback<Node>() {
            @Override
            public void on(Node result) {
                result.get("value");
                result.free();
            }
        });

        return param.counter++;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(WorldRead.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

}
