package org.kevoree.mwg.benchmark.storage.connector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

public class CSVConnector implements Connector{

    String folderPath = "/home/mwg-bench";
//    String folderPath = ".";
    File folder;

    public CSVConnector() {
        folder = new File(folderPath);
        folder.mkdirs();
    }

    @Override
    public void process(String jsonData) {
        System.out.println("[" + new Date() + "] Process data by VSCConnector");

        JSONArray benchsData = new JSONArray(jsonData);



        File csv = new File(folder.getAbsolutePath() + "/" + System.currentTimeMillis() + "-bench.csv");
        try {
            csv.createNewFile();

            FileWriter writer = new FileWriter(csv);
            StringBuilder toWrite;
            for(int i=0;i<benchsData.length();i++) {
                JSONObject bench = benchsData.getJSONObject(i);
                toWrite = new StringBuilder();

                toWrite.append(bench.getString("name"));


                try {
                    JSONObject params = bench.getJSONObject("params");
                    Iterator<String> itpNames = params.keys();
                    while(itpNames.hasNext()) {
                        String pName = itpNames.next();
                        toWrite.append(",")
                                .append(pName)
                                .append("=")
                                .append(params.getString(pName));
                    }
                }catch (JSONException ex) {
                    //do nothing -> no params
                }
                toWrite.append(");");

                double score;
                if(bench.getString("status").equals("succeed")) {
                        score = bench.getDouble("score");
                } else {
                    score = -1;
                }

                toWrite.append(String.format("%1$,.6f",score))
                        .append("\n");
                writer.write(toWrite.toString());

            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
