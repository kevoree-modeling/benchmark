package org.kevoree.mwg.jmh.core;

import org.mwg.Callback;
import org.mwg.Graph;
import org.mwg.GraphBuilder;
import org.mwg.Node;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class AddRelation {

    @State(Scope.Thread)
    public static class Parameter {
        Graph graph;
        Node root;
        Node[] children;

        int counter;

        //todo put offheap when the Node.add() offheap implementation will be fixed
        @Param(value = {"true"})
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
                graphBuilder.withOffHeapMemory();
            }
            graph = graphBuilder.build();

            graph.connect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
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
    public void benchAddRelation(Parameter parameter) {
        parameter.root.add("childs",parameter.children[parameter.counter]);
        parameter.counter++;
    }
}