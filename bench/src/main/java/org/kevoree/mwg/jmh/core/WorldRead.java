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
public class WorldRead {
    @State(Scope.Thread)
    public static class Parameter {
        Graph graph;
        long node;
        int counter;
        long[] worlds;

        @Param(value = {"false","true"})
        boolean useHeap;

        @Param("5000000")
        long cacheSize;

        @Param(value = {"false","true"})
        boolean stairWorlds;

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
                    node = graph.newNode(0,0).id();

                    worlds[0] = 0L;
                    for(int i=1;i<1_000_010;i++) {
                        if(stairWorlds) {
                            worlds[i] = graph.fork(worlds[i-1]);
                        } else {
                            worlds[i] = graph.fork(0);
                        }
                        graph.lookup(worlds[i], 0, node, new Callback<Node>() {
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
    public void benchWorldRead(Parameter param) {
        param.graph.lookup(param.worlds[param.counter], 0, param.node, new Callback<Node>() {
            @Override
            public void on(Node result) {
                result.get("value");
                result.free();
            }
        });
        param.counter++;
    }

}
