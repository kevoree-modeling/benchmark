package org.mwg.benchmark.storage.connector;

/**
 * Created by ludovicmouline on 22/07/16.
 */
public interface Connector {

    void process(String jsonData);
    void start();
    void stop();

}
