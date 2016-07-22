package org.kevoree.mwg.benchmark.rest;

public class RunServer {

    public static void main(String[] args) {
        RestAPI httpServer = new RestAPI();
        httpServer.start(9876);
    }
}
