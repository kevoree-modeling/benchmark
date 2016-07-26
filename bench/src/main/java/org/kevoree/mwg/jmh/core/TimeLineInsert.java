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
public class TimeLineInsert {

    @State(Scope.Thread)
    public static class Parameter {
        Graph graph;
        Node node;
        int counter;

        @Param(value = {"false","true"})
        boolean useHeap;

        @Param("500000")
        long cacheSize;

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
                    node = graph.newNode(0,0);
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
    public void newNodes(Parameter param) {
        param.node.jump(param.counter, new Callback<Node>() {
            @Override
            public void on(Node result) {
                result.set("value",param.counter);
                result.free();
            }
        });
        param.counter++;
    }
}
