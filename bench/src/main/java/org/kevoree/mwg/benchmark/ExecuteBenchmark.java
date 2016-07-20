package org.kevoree.mwg.benchmark;

/**
 * Created by assaad on 13/07/16.
 */
public class ExecuteBenchmark {
    private static final int NB_ARGS = 6;


    private static Object[] checkAndGetParams(String[] args) {
        if(args.length != NB_ARGS) {
            throw new RuntimeException("Wrong number of arguments. Expected: " + NB_ARGS + ". Actual: " + args.length);
        }
        Object[] res = new Object[NB_ARGS - 1];

        res[0] = Integer.parseInt(args[1]);
        res[1] = Integer.parseInt(args[2]);
        res[2] = Integer.parseInt(args[3]);
        res[3] = Boolean.parseBoolean(args[4]);
        res[4] = Integer.parseInt(args[5]);


        return res;
    }
    public static void main(String[] args) {
        Object[] params = checkAndGetParams(args);

        if(args[0].equalsIgnoreCase("BenchEmpty")) {
            new BenchEmpty((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchJavaRandom")) {
            new BenchJavaRandom((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchHashFunction")) {
            new BenchHashFunction((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchTimeLineInsert")) {
            new BenchTimeLineInsert((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchTimeLineRead")) {
            new BenchTimeLineRead((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchNewNodes")) {
            new BenchNewNodes((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchLookupNodes")) {
            new BenchLookupNodes((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchWorldInsert")) {
            new BenchWorldInsert((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchWorldRead")) {
            new BenchWorldRead((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        }  else if(args[0].equalsIgnoreCase("BenchWorldStairRead")) {
            new BenchWorldStairRead((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchAddRelation")) {
            new BenchAddRelation((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchAddRelationLongArray")) {
            new BenchAddRelationLongArray((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else if(args[0].equalsIgnoreCase("BenchAddRelationRandomGraph")) {
            new BenchAddRelationRandomGraph((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]).run();
        } else {
            throw new RuntimeException("Unknown benchmark " + args[0]);
        }
    }

}
