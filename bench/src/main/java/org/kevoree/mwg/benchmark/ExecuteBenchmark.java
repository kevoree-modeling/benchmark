package org.kevoree.mwg.benchmark;

import org.kevoree.mwg.benchmark.core.*;
import org.kevoree.mwg.benchmark.general.*;
import org.kevoree.mwg.benchmark.ml.BenchmarkKDTree;

/**
 *  Useful for manual bench
 */
public class ExecuteBenchmark {

    public static void main(String[] args) {
        Object[] params = new Object[]{
                10,
                200_000,
                0,
                true,
                5_000_000
        };


        AbstractBenchmark bench = null;
        String benchToLaunch = "BenchAddRelation";
        String url = "";


        //core benchs
        if(benchToLaunch.equalsIgnoreCase("BenchAddRelation")) {
            bench = new BenchAddRelation((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchAddRelationRandomGraph")) {
        bench = new BenchAddRelationRandomGraph((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchLookupNodes")) {
        bench = new BenchLookupNodes((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchNewNodes")) {
        bench = new BenchNewNodes((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchTimeLineInsert")) {
        bench = new BenchTimeLineInsert((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchTimeLineRead")) {
        bench = new BenchTimeLineRead((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchWorldInsert")) {
        bench = new BenchWorldInsert((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchWorldRead")) {
        bench = new BenchWorldRead((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchWorldStairRead")) {
        bench = new BenchWorldStairRead((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        }
        //general bench/reference bench
         else if(benchToLaunch.equalsIgnoreCase("BenchAddRelationLongArray")) {
        bench = new BenchAddRelationLongArray((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchEmpty")) {
        bench = new BenchEmpty((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchHashFunction")) {
        bench = new BenchHashFunction((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchJavaRandom")) {
        bench = new BenchJavaRandom((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else if(benchToLaunch.equalsIgnoreCase("BenchKDTreeJava")) {
        bench = new BenchKDTreeJava((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        }

        //ml benchmarks
          else if(benchToLaunch.equalsIgnoreCase("BenchmarkKDTree")) {
        bench = new BenchmarkKDTree((int) params[0], (int) params[1], (int) params[2], (boolean) params[3], (int) params[4]);
        } else {
            System.err.println(benchToLaunch + " is unknown. Please update the main file");
            return;
        }

        bench.run();

        //Uncomment the two following lines if you want to save the result
//        Storage.save(url, JsonFormat.result2Json(bench));



    }

}
