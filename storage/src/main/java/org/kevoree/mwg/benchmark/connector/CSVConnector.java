package org.kevoree.mwg.benchmark.connector;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ludovicmouline on 22/07/16.
 */
public class CSVConnector implements Connector {
    String csvFile = "data.csv";


    public void process(String jsonData) {
        BenchData benchData = JSON.parseObject(jsonData,BenchData.class);

        Path path = Paths.get(csvFile);
        File f = path.toFile();

        if(!f.exists()) {
            try {
                f.createNewFile();
                FileWriter writter = new FileWriter(f,true);
                writter.write("timestamp");
                writter.flush();
                writter.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = reader.readLine();

            String[] columns = line.split(";");

            int nbColumns = columns.length;
            int i;
            for(i=1;i<columns.length;i++) {
                if(columns[i].equalsIgnoreCase(benchData.getId())) {
                    break;
                }
            }

            FileWriter writer;
            if(i == columns.length) {
                writer = new FileWriter(f,false);
                writer.write(line);
                writer.append(";").append(benchData.getId()).append("\n");
                while((line = reader.readLine()) != null) {
                    writer.append(line).append(";\n");
                }
                nbColumns++;
            } else {
                writer = new FileWriter(f,true);
            }

            reader.close();



            writer.append(System.currentTimeMillis() + "");
            int j;
            for(j=0;j<i;j++) {
                writer.append(";");
            }
            writer.append(benchData.getBenchmarkSpeed() + "");
            for(j=i;j<nbColumns - 1;j++) {
                writer.append(";");
            }
            writer.append("\n");

            writer.flush();
            writer.close();

        }catch (Exception e) {
            e.printStackTrace();
            return;
        }



        System.out.println(benchData);
    }
}
