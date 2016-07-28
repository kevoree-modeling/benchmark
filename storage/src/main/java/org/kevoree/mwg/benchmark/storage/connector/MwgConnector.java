package org.kevoree.mwg.benchmark.storage.connector;

import com.alibaba.fastjson.JSON;
import org.kevoree.mwg.benchmark.storage.json.BenchJsonBenchmark;
import org.mwg.Graph;

import java.util.List;

public class MwgConnector implements Connector {
    private Graph _graph;

    public void init() {
//        _graph = new GraphBuilder().withPlugin(new )
    }

    public void process(String jsonData) {
        List<BenchJsonBenchmark> benchsData = JSON.parseArray(jsonData,BenchJsonBenchmark.class);

        System.out.println(benchsData.size());
        for(BenchJsonBenchmark bench : benchsData) {
            System.out.println(bench.getBenchmark());
        }

    }
}
