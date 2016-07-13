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

    //Number formating tools
    protected static NumberFormat df = new DecimalFormat("#,###");
    protected static NumberFormat nf = new DecimalFormat("#,###.00");

    //Warmup rounds to run before actual benchmark
    protected final int roundsBefore;

    //Actual Benchmark rounds
    protected final int rounds;
    //To display results each many rounds, leave it 0 to get max speed perf
    protected final int displayEach;

    //MWG graph to initialize
    protected Graph graph;

    //Output of the test speed In value/sec
    private double benchmarkspeed;

    //Reel benchmark time in nanosec
    private long execTime;

    //Benchmark test name
    protected abstract String getName();


    //Getter zone
    public int getRounds() {
        return rounds;
    }

    public long getExecTime() {
        return execTime;
    }

    public double getBenchmarkspeed() {
        return benchmarkspeed;
    }

    public String getBenchmarkspeedString(){
        return df.format(benchmarkspeed);
    }

    public AbstractBenchmark(final int roundsBefore, final int rounds, final int displayEach){
        this.roundsBefore=roundsBefore;
        this.rounds=rounds;
        this.displayEach=displayEach;
    }

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


    //Main implementation methods
    protected abstract void initializeGraph(Callback<Boolean> callback);

    protected abstract void runBeforeBench(Callback<Boolean> callback);

    protected abstract void oneRoundBench(Callback<Boolean> callback);

    protected abstract void runAfterBench(Callback<Boolean> callback);


    //The benchmark function
    public void run() {
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
                                        long endTime=System.nanoTime();
                                        execTime=endTime-startTime;
                                        double speed = getTime(endTime, startTime, rounds);
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



}
