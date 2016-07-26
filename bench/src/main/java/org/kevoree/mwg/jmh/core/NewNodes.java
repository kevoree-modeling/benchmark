package org.kevoree.mwg.jmh.core;

import org.mwg.Graph;
import org.mwg.GraphBuilder;
import org.mwg.Node;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by ludovicmouline on 26/07/16.
 */
public class NewNodes {

    @State(Scope.Thread)
    public static class Parameter {
        Graph graph;

        @Param(value = {"false","true"})
        boolean useHeap;

        @Param("5000000")
        long cacheSize;

        @Setup
        public void setup() {
            GraphBuilder graphBuilder = new GraphBuilder();
            graphBuilder.withMemorySize(cacheSize);
            if(!useHeap) {
                graphBuilder.withOffHeapMemory();
            }
            graph = graphBuilder.build();

            graph.connect(null);
        }

        @TearDown
        public void tearDown() {
            graph.disconnect(null);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Fork(10)
    @Warmup(iterations = 1, batchSize = 10)
    @Measurement(iterations = 1, batchSize = 1_000_000)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Timeout(time = 5, timeUnit = TimeUnit.MINUTES)
    public Object benchNewNodes(Parameter param) {
        Node insert = param.graph.newNode(0,0);
        insert.set("value",33);
        insert.free();
        return null;
    }

}
