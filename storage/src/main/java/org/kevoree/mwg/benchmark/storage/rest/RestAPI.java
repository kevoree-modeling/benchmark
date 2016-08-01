package org.kevoree.mwg.benchmark.storage.rest;


import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.kevoree.mwg.benchmark.storage.connector.CSVConnector;
import org.kevoree.mwg.benchmark.storage.connector.Connector;
import org.kevoree.mwg.benchmark.storage.connector.MwgConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class RestAPI implements HttpHandler{
    private Undertow _httpServer;
    private final Connector[] _connectors;

    public RestAPI() {
        _connectors = new Connector[] {
                new MwgConnector(),
                new CSVConnector()
        };
    }

    public void start(int port) {
        if(_httpServer != null) {
            _httpServer.stop();
        }
        _httpServer = Undertow.builder().addHttpListener(port,"0.0.0.0").setHandler(this).build();
        _httpServer.start();

    }

    public void stop() {
        if(_httpServer !=null) {
            _httpServer.stop();
            _httpServer = null;
        }
    }

    public void handleRequest(HttpServerExchange exchange){
        System.out.println("[" + new Date() + "] " + exchange.getRequestMethod() + " Request received: " + exchange.getRequestURL());
        if(exchange.getRequestMethod().equalToString("POST")) {
            if(exchange.isInIoThread()) {
                exchange.dispatch(this);
                return;
            }

            exchange.startBlocking();
            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getInputStream()));
            StringBuilder jsonFile = new StringBuilder();
            String line;
            try {
                while((line = reader.readLine()) != null) {
                    jsonFile.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            exchange.endExchange();

            forward2Connectors(jsonFile.toString());
        }

    }

    private void forward2Connectors(String data) {
        for(int i=0;i<_connectors.length;i++) {
            _connectors[i].process(data);
        }
    }
}
