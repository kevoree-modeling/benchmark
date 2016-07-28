package org.kevoree.mwg.benchmark.storage;

import org.kevoree.mwg.benchmark.storage.rest.RestAPI;

public class RunServer {

    private static final int DEFAULT_PORT = 9876;

    public static void main(String[] args) {

        int port = -1;
        if(args.length > 1) {
            System.err.println("Wrong usage. Usage: java -jar <executable> [port]");
            System.exit(2);
        } else if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Wrong argument " + args[0] + ". Usage: java -jar <executable> [port].");
                System.exit(2);
            }
        } else {
            port = DEFAULT_PORT;
        }

        RestAPI httpServer = new RestAPI();
        try {
            httpServer.start(port);
        }catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
            System.exit(2);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                httpServer.stop();
            }
        });
    }
}
