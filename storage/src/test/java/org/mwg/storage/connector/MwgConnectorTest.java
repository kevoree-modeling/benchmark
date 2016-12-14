package org.mwg.storage.connector;

import org.junit.Test;
import org.mwg.benchmark.storage.connector.MwgConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ludovicmouline on 09/12/2016.
 */
public class MwgConnectorTest {

    @Test
    public void test() {
        MwgConnector connector = new MwgConnector();
        connector.start();

        StringBuilder linesBuilder = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/sample.json")));
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
