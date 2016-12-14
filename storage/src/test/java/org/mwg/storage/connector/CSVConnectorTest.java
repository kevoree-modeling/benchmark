package org.mwg.storage.connector;


import org.mwg.benchmark.storage.connector.CSVConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CSVConnectorTest {


   //@Test
    public void test() {
        CSVConnector connector = new CSVConnector();

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
}
