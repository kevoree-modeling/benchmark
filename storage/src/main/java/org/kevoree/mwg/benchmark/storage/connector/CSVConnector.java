package org.kevoree.mwg.benchmark.storage.connector;

import com.alibaba.fastjson.JSON;
import org.kevoree.mwg.benchmark.storage.json.BenchJsonBenchmark;
import org.kevoree.mwg.benchmark.storage.json.BenchJsonMetrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVConnector implements Connector{

    String folderPath = "/home/mwg-bench";
    File folder;

    public CSVConnector() {
        folder = new File(folderPath);
        folder.mkdirs();
    }

    @Override
    public void process(String jsonData) {
        List<BenchJsonBenchmark> benchsData = JSON.parseArray(jsonData,BenchJsonBenchmark.class);

        File csv = new File(System.currentTimeMillis() + "-bench.csv");
        try {
            csv.createNewFile();

            FileWriter writer = new FileWriter(csv);
            StringBuilder toWrite;
            for(int i = 0; i< benchsData.size();i++) {
                toWrite = new StringBuilder();
                BenchJsonBenchmark bench = benchsData.get(i);
                toWrite.append(bench.getBenchmark())
                        .append(";");
                double score;
                BenchJsonMetrics metrics = bench.getPrimaryMetric();
                if(metrics.getScoreUnit().equals("s/op")) {
                    score = metrics.getScore() / bench.getMeasurementBatchSize();
                } else {
                    score = metrics.getScore();
                }
                toWrite.append(String.format("%1$,.2f",score)).append("\n");
                writer.append(toWrite.toString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}