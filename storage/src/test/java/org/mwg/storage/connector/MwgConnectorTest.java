package org.mwg.storage.connector;

import org.junit.Test;
import org.mwg.*;
import org.mwg.bench.BenchModel;
import org.mwg.benchmark.storage.connector.MwgConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by ludovicmouline on 09/12/2016.
 */
public class MwgConnectorTest {

    @Test
    public void test() {
        MwgConnector connector = new MwgConnector();
        connector.start();

        StringBuilder linesBuilder = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/test.json")));
        String line;
        try {
            while((line = reader.readLine()) != null) {
                linesBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        connector.process(linesBuilder.toString());

    }


    @Test
    public void testTest() {
        Graph graph = new GraphBuilder().withStorage(new WSClient("ws://localhost:6547")).build();

        graph.connect(new Callback<Boolean>() {
            @Override
            public void on(Boolean result) {
                graph.index(0, System.currentTimeMillis(), BenchModel.IDX_IDX_BENCHMARK, new Callback<NodeIndex>() {
                    @Override
                    public void on(NodeIndex result) {
                        result.find(new Callback<Node[]>() {
                            @Override
                            public void on(Node[] result) {
                                System.out.println(Arrays.toString(result));
                                System.out.println("Done");
                            }
                        });
                    }
                });
            }
        });
    }
}
