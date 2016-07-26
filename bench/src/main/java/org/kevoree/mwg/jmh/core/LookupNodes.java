package org.kevoree.mwg.jmh.core;

import org.mwg.Callback;
import org.mwg.Graph;
import org.mwg.GraphBuilder;
import org.mwg.Node;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by ludovicmouline on 26/07/16.
 */
public class LookupNodes {
    @State(Scope.Thread)
    public static class Parameter {
        Graph graph;
        Node root;
        long[] children;

        int counter;



        @Param(value = {"false","true"})
        boolean useHeap;

        @Param("500000")
        long cacheSize;

        @Param("1000000")
        int nbChildren;

        @Setup
        public void setup() {
            GraphBuilder graphBuilder = new GraphBuilder();
            graphBuilder.withMemorySize(cacheSize);
            if(!useHeap) {
                graphBuilder.withOffHeapMemory();
            }
            graph = graphBuilder.build();

            graph.connect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    root = graph.newNode(0,0);
                    children = new long[nbChildren];

                    for(int i=0;i<nbChildren;i++) {
                        children[i] = graph.newNode(0,0).id();
                    }
                }
            });
        }

        @TearDown
        public void tearDown() {
            graph.disconnect(null);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(10)
    @Warmup(iterations = 100, batchSize = 1)
    @Measurement(iterations = 1_000_000, batchSize = 1)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void benchLookupNodes(Parameter parameter) {
        parameter.graph.lookup(0,0,parameter.children[parameter.counter],null);
        parameter.counter++;
    }
}
