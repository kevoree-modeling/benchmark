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
public class WorldStairRead {
    @State(Scope.Thread)
    public static class Parameter {
        Graph graph;
        Node node;
        int counter;
        long[] worlds;
        long startAvailableSpace;

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

            worlds = new long[1_000_010];

            graph.connect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    startAvailableSpace = graph.space().available();
                    node = graph.newNode(0,0);

                    worlds[0] = 0L;
                    for(int i=1;i<1_000_010;i++) {
                        graph.lookup(worlds[i], i, node.id(), new Callback<Node>() {
                            @Override
                            public void on(Node result) {
                                result.set("value",55);
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
                    if(endAvailableSpace != startAvailableSpace) {
                        throw new RuntimeException("Memory leak detected: startAvailableSpace=" + startAvailableSpace + "; endAvailableSpace=" + endAvailableSpace + "; diff= " + (endAvailableSpace - startAvailableSpace));
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
    public void benchWorldStairRead(Parameter param) {
        param.graph.lookup(param.worlds[param.worlds.length - 1], param.counter, param.node.id(), new Callback<Node>() {
            @Override
            public void on(Node result) {
                result.get("value");
                result.free();
            }
        });
        param.counter++;
    }

}
