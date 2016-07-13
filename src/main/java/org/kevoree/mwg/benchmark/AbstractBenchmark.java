package org.kevoree.mwg.benchmark;

import org.mwg.Callback;
import org.mwg.DeferCounter;
import org.mwg.Graph;
import org.mwg.plugin.Job;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by assaad on 13/07/16.
 */
public abstract class AbstractBenchmark {

    protected final int roundsBefore;
    protected final int rounds;
    protected final int displayEach;
    // In value/sec
    private double benchmarkspeed;

    public long getExecTime() {
        return execTime;
    }


    public AbstractBenchmark(final int roundsBefore, final int rounds, final int displayEach){
        this.roundsBefore=roundsBefore;
        this.rounds=rounds;
        this.displayEach=displayEach;
    }

    private long execTime; //in nanosec;

    protected abstract String getName();


    protected Graph graph;

    protected static NumberFormat df = new DecimalFormat("#,###");
    protected static NumberFormat nf = new DecimalFormat("#,###.00");

    protected abstract void initializeGraph(Callback<Boolean> callback);

    protected abstract void runBeforeBench(Callback<Boolean> callback);

    protected abstract void oneRoundBench(Callback<Boolean> callback);

    protected abstract void runAfterBench(Callback<Boolean> callback);



    public static double getTime(long endTime, long startTime, int counter) {
        double x = endTime - startTime;
        x = 1000000000 * (counter / x);
        return x;
    }

    public String getExecTimeString(){
        double x = execTime;
        if(x<1000){
            return nf.format(x)+" ns";
        }
        else if(x<1000000){
            x=x/1000.0;
            return nf.format(x)+" μs";
        }
        else if(x<1000000000){
            x=x/1000000.0;
            return nf.format(x)+" ms";
        }
        else{
            x=x/1000000000.0;
            return nf.format(x)+" s";
        }
    }

    public void runBenchmark() {
        initializeGraph(new Callback<Boolean>() {
            public void on(Boolean result) {

                runBeforeBench(new Callback<Boolean>() {
                    public void on(final Boolean result) {


                        final DeferCounter defer = graph.newCounter(roundsBefore);
                        for (int i = 0; i < roundsBefore; i++) {
                            oneRoundBench(new Callback<Boolean>() {
                                public void on(Boolean result) {
                                    defer.count();
                                }
                            });
                        }

                        defer.then(new Job() {
                            public void run() {

                                final DeferCounter defer2 = graph.newCounter(rounds);

                                final long startTime = System.nanoTime();

                                for (int i = 0; i < rounds; i++) {
                                    oneRoundBench(new Callback<Boolean>() {
                                        public void on(Boolean result) {
                                            defer2.count();
                                            if(displayEach>0) {
                                                int count = defer2.getCount();
                                                if (count % displayEach == 0) {
                                                    double speed = getTime(System.nanoTime(), startTime, count);
                                                    System.out.println("Avg Speed at counter " + df.format(count) + " is: " + df.format(speed) + " v/s");
                                                }
                                            }
                                        }
                                    });
                                }
                                defer2.then(new Job() {
                                    public void run() {
                                        double speed = getTime(System.nanoTime(), startTime, rounds);
                                        benchmarkspeed =speed;
                                        System.out.println("Final Avg speed at counter " + df.format(rounds) + " is: " + df.format(speed)+" v/s");

                                        final DeferCounter defer3=graph.newCounter(1);
                                        runAfterBench(new Callback<Boolean>() {
                                            public void on(Boolean result) {
                                                defer3.count();
                                            }
                                        });
                                        defer3.then(new Job() {
                                            public void run() {
                                                execTime=System.nanoTime()-startTime;
                                                System.out.println(getName()+" is completed in "+getExecTimeString());
                                                System.out.println();
                                            }
                                        });


                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public double getBenchmarkspeed() {
        return benchmarkspeed;
    }

    public String getBenchresultString(){
        return df.format(benchmarkspeed);
    }

}
