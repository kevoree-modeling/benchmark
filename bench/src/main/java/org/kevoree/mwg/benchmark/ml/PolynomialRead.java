package org.kevoree.mwg.benchmark.ml;

import org.kevoree.mwg.benchmark.utils.MWGUtil;
import org.mwg.*;
import org.mwg.ml.MLPlugin;
import org.mwg.ml.algorithm.regression.PolynomialNode;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PolynomialRead {

    @State(Scope.Thread)
    public static class Parameter {
        Graph graph;
        PolynomialNode node;
        int counter;
        long startAvailableSpace;

        @Param(value = {"false", "true"})
        boolean useHeap;

        @Param("5000000")
        long cacheSize;

        @Setup
        public void setup() {
            GraphBuilder graphBuilder = new GraphBuilder();
            graphBuilder.withMemorySize(cacheSize).withPlugin(new MLPlugin());
            if (!useHeap) {
                MWGUtil.offHeap(graphBuilder);
            }
            graph = graphBuilder.build();

            graph.connect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    startAvailableSpace = graph.space().available();
                    node = (PolynomialNode) graph.newTypedNode(0, 0, PolynomialNode.NAME);
                    node.set(PolynomialNode.PRECISION, Type.DOUBLE, 0.1);
                    Random rand = new Random(12563L);
                    for (int i = 0; i < 1000000; i++) {
                        int finalI = i;
                        node.travelInTime(i, new Callback<Node>() {
                            @Override
                            public void on(Node result) {
                                result.set(PolynomialNode.VALUE, Type.DOUBLE, 2 * finalI * finalI - 5 * finalI);
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
    @Warmup(iterations = 0, batchSize = 0)
    @Measurement(iterations = 1, batchSize = 1_000_000)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Timeout(time = 5, timeUnit = TimeUnit.MINUTES)
    public Object benchPolynomial(Parameter param) {
        param.node.travelInTime(param.counter, new Callback<Node>() {
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
                .include(PolynomialRead.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
