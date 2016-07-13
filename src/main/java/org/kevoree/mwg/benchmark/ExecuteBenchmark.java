package org.kevoree.mwg.benchmark;

/**
 * Created by assaad on 13/07/16.
 */
public class ExecuteBenchmark {
    public static void main(String[] arg){
        CounterBenchmark counter = new CounterBenchmark(0,100000000,0);
        counter.run();
    }

}
