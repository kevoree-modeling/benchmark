package org.kevoree.mwg.benchmark.core;

import org.kevoree.mwg.benchmark.utils.MWGUtil;
import org.mwg.Callback;
import org.mwg.Graph;
import org.mwg.GraphBuilder;
import org.mwg.Node;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class LookupNodes{
    @State(Scope.Thread)
    public static class Parameter {
        Graph graph;
        Node root;
        Node[] children;
        long startAvailableSpace;

        int counter;



        @Param(value = {"false","true"})
        boolean useHeap;

        @Param("5000000")
        long cacheSize;

        @Param("1000010")
        int nbChildren;

        @Setup
        public void setup() {
            GraphBuilder graphBuilder = new GraphBuilder();
            graphBuilder.withMemorySize(cacheSize);
            if(!useHeap) {
                MWGUtil.offHeap(graphBuilder);
            }
            graph = graphBuilder.build();

            graph.connect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    startAvailableSpace = graph.space().available();
                    root = graph.newNode(0,0);
                    children = new Node[nbChildren];

                    for(int i=0;i<nbChildren;i++) {
                        children[i] = graph.newNode(0,0);
                    }
                }
            });
        }

        @TearDown
        public void tearDown() {
            for(int i=0;i<children.length;i++) {
                children[i].free();
            }
            root.free();

            graph.save(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    long endAvailableSpace = graph.space().available();
                    if(endAvailableSpace != startAvailableSpace) {
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
    public int benchLookupNodes(Parameter parameter) {
        parameter.graph.lookup(0, 0, parameter.children[parameter.counter].id(), new Callback<Node>() {
            @Override
            public void on(Node result) {
                result.free();
            }
        });
        return parameter.counter++;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LookupNodes.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
