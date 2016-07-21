package org.kevoree.mwg.benchmark;

import org.kevoree.mwg.benchmark.core.BenchAddRelation;
import org.kevoree.mwg.benchmark.general.BenchKDTreeJava;
import org.kevoree.mwg.benchmark.ml.BenchmarkKDTree;

/**
 * Created by assaad on 13/07/16.
 */
public class ExecuteBenchmark {
    public static void main(String[] arg) {
//        BenchEmpty counter = new BenchEmpty(1000, 200000000, 0, false, 100000);
//        counter.run();

//         BenchJavaRandom javaRandom = new BenchJavaRandom(1000, 100000000, 0, false, 100000);
//        javaRandom.run();

//         BenchHashFunction hashBench = new BenchHashFunction(1000, 100000000, 0, false, 100000);
//         hashBench.run();


//        BenchTimeLineInsert tlinsert1m = new BenchTimeLineInsert(1000, 3000000, 0, false, 5000000);
//        tlinsert1m.run();


//        BenchTimeLineRead tlread1m = new BenchTimeLineRead(1000, 3000000, 0, false, 5000000);
//        tlread1m.run();

//        BenchNewNodes newnode1m= new BenchNewNodes(1000, 3000000, 0, false, 15000000);
//        newnode1m.run();

//        BenchLookupNodes lookupNodes=new BenchLookupNodes(1000, 3000000, 0, false, 15000000);
//        lookupNodes.run();

//        BenchWorldInsert worldinsert1m = new BenchWorldInsert(0, 1000000, 0, false, 5000000);
//        worldinsert1m.run();

//        BenchWorldRead worldread1m = new BenchWorldRead(0, 1000000, 0, false, 5000000);
//        worldread1m.run();

//        BenchWorldStairRead worldstairread1m = new BenchWorldStairRead(0, 10000, 0, false, 100000);
//        worldstairread1m.run();

//        BenchAddRelation addrelbench=new BenchAddRelation(0, 1000000, 0, false, 5000000);
//        addrelbench.run();


//        BenchKDTreeJava benchKDTreeJava=new BenchKDTreeJava(0,1000000,0,false,100000);
//        benchKDTreeJava.run();

        BenchmarkKDTree benchmarkKDTree=new BenchmarkKDTree(0,100000,0,false,5000000);
        benchmarkKDTree.run();
//        BenchAddRelationLongArray addrelbencharr=new BenchAddRelationLongArray(0, 100000, 0, false, 1500000);
//        addrelbencharr.run();

//        BenchAddRelationRandomGraph addrelRand=new BenchAddRelationRandomGraph(0,1000000,0,false,1000000);
//        addrelRand.run();

//        BenchTimeLineInsert tlinsert3m = new BenchTimeLineInsert(1000,3000000,0,false,10000000);
//        tlinsert3m.run();


//

    }

}
