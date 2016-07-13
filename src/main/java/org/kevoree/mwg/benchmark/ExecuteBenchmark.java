package org.kevoree.mwg.benchmark;

/**
 * Created by assaad on 13/07/16.
 */
public class ExecuteBenchmark {
    public static void main(String[] arg){
       /* BenchNormalCounter counter = new BenchNormalCounter(0,100000000,0,false,300000);
        counter.run();*/

        BenchTimeLineInsert tlinsert1m = new BenchTimeLineInsert(1000,1000000,1000000,false,10000000);
        tlinsert1m.run();

        /*BenchTimeLineInsert tlinsert3m = new BenchTimeLineInsert(1000,3000000,0,false,10000000);
        tlinsert3m.run();*/
    }

}
