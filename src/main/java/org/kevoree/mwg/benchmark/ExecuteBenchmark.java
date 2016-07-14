package org.kevoree.mwg.benchmark;

/**
 * Created by assaad on 13/07/16.
 */
public class ExecuteBenchmark {
    public static void main(String[] arg) {
//        BenchEmpty counter = new BenchEmpty(1000, 100000000, 0, false, 100000);
//        counter.run();


//        BenchTimeLineInsert tlinsert1m = new BenchTimeLineInsert(1000, 3000000, 0, false, 5000000);
//        tlinsert1m.run();

//        BenchTimeLineRead tlread1m = new BenchTimeLineRead(1000, 3000000, 0, false, 5000000);
//        tlread1m.run();

//        BenchNewNodes newnode1m= new BenchNewNodes(1000, 3000000, 0, false, 15000000);
//        newnode1m.run();

//        BenchLookupNodes lookupNodes=new BenchLookupNodes(1000, 3000000, 0, false, 15000000);
//        lookupNodes.run();

        BenchAddRelation addrelbench=new BenchAddRelation(0, 100000, 0, false, 1500000);
        addrelbench.run();

//        BenchTimeLineInsert tlinsert3m = new BenchTimeLineInsert(1000,3000000,0,false,10000000);
//        tlinsert3m.run();


//        BenchHashFunction hashBench = new BenchHashFunction(1000, 100000000, 0, false, 100000);
//        hashBench.run();

//        BenchJavaRandom javaRandom = new BenchJavaRandom(1000, 100000000, 0, false, 100000);
//        javaRandom.run();


    }

}
