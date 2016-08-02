package org.kevoree.mwg.benchmark;


import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.BenchmarkList;
import org.openjdk.jmh.runner.BenchmarkListEntry;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.format.OutputFormat;
import org.openjdk.jmh.runner.format.OutputFormatFactory;
import org.openjdk.jmh.runner.options.*;
import org.openjdk.jmh.util.Optional;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BenchRunner {
    private static final String START_STRING = "\"";
    private static final String END_STRING = START_STRING;
    private static final String EOL = "\n";
    private static final String DATA_SEP = ",";
    private static final String START_OBJ = "{\n";
    private static final String END_OBJ = "}\n";
    private static final String DATA_VALUE_SEP = ":";

    public static void main(String[] args) throws ClassNotFoundException, IOException, RunnerException, CommandLineOptionException {

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        StringBuilder failedBench;

        Set<BenchmarkListEntry> benchmarks;
        {
            BenchmarkList list = BenchmarkList.defaultList();
            Options options = new OptionsBuilder().build();
            OutputFormat out = OutputFormatFactory.createFormatInstance(System.out, VerboseMode.NORMAL);
            benchmarks = list.find(out, options.getIncludes(), options.getExcludes());
        }

        ChainedOptionsBuilder optBuilder;
        Optional<Map<String,String[]>> params;
        StringBuilder temporyBenchInfo;
        for(BenchmarkListEntry bench : benchmarks) {
            jsonBuilder.append(START_OBJ)
                    // "name" : "benchName",\n
                    .append(START_STRING).append("name").append(END_STRING)
                    .append(DATA_VALUE_SEP).append(START_STRING).append(bench.getUsername()).append(END_STRING)
                    .append(DATA_SEP).append(EOL);

            temporyBenchInfo = new StringBuilder();
            temporyBenchInfo.append(bench.getUsername()).append("[");

            optBuilder = new OptionsBuilder()
                    .include(bench.getUsername())
                    .shouldDoGC(true);

            params = bench.getParams();
            if(params.hasValue()) {
                // "params" : {
                jsonBuilder.append(START_STRING).append("params").append(END_STRING).append(DATA_VALUE_SEP)
                        .append(START_OBJ);
                Map<String,String[]> paramsMap = params.get();
                int nbSolution = 1;

                for(String[] paramValues : paramsMap.values()) {
                    nbSolution *= paramValues.length;
                }

                String[] keys = new String[paramsMap.keySet().size()];
                {
                    Iterator<String> itKey = paramsMap.keySet().iterator();
                    int index = 0;
                    while(itKey.hasNext()) {
                        keys[index] = itKey.next();
                        index++;
                    }
                }

                for(int index=0;index<nbSolution;index++) {
                }

                jsonBuilder.append(END_OBJ).append(DATA_SEP).append(EOL);
            }

            Options opt = optBuilder.build();
            StringBuffer error = null;
            RunResult runResult = null;
            /*try {
                Runner runner = new Runner(opt);
                Collection<RunResult> results = runner.run();
                Iterator<RunResult> resultIterator = results.iterator();
                if(resultIterator.hasNext()) {
                    runResult = resultIterator.next();
                    if(resultIterator.hasNext()) {
                        throw new RuntimeException("There is more than one result. Please check the runner code");
                    }
                } else {
                    throw new RuntimeException("There is more than one result. Please check the runner code");
                }
                runner.runSystemGC();
            } catch (RunnerException exception) {
                StringWriter sw = new StringWriter();
                if(exception.getCause() == null) {
                    exception.printStackTrace(new PrintWriter(sw));
                } else {
                    exception.getCause().printStackTrace(new PrintWriter(sw));
                }
                error = sw.getBuffer();
            }*/

            // "status":

            /*jsonBuilder.append(START_STRING).append("status").append(END_STRING).append(DATA_VALUE_SEP);
            if(error == null) {
                // "succeed", \n
                // "score" :
                jsonBuilder.append(START_STRING).append("succeed").append(END_STRING).append(DATA_SEP).append(EOL)
                        .append(START_STRING).append("score").append(END_STRING).append(DATA_VALUE_SEP);

                double score = runResult.getPrimaryResult().getScore();
                if(runResult.getParams().getMode().shortLabel().equals(Mode.SingleShotTime.shortLabel())) {
                    score = runResult.getParams().getMeasurement().getBatchSize() / score;
                }
                jsonBuilder.append(score).append(DATA_SEP).append(EOL);
                jsonBuilder.append(START_STRING).append("scoreUnit").append(END_STRING).append(DATA_VALUE_SEP)
                        .append(START_STRING).append("op/s").append(END_STRING).append(DATA_SEP).append(EOL);

            } else {
                // "failed",\n
                // "reason": "exception caught",\n
                jsonBuilder.append(START_STRING).append("failed").append(END_STRING).append(DATA_SEP).append(EOL)
                        .append(START_STRING).append("reason").append(END_STRING).append(DATA_VALUE_SEP)
                        .append(START_STRING).append(error).append(END_STRING).append(END_STRING)
                        .append(DATA_SEP).append(EOL);
            }*/

            jsonBuilder.append(END_OBJ);
            System.out.println(jsonBuilder);





            /*Options opt = new OptionsBuilder()
                    .include(bench.getUsername())
                    .shouldDoGC(true)
                    .forks(1)
                    .build();
            try {
                org.openjdk.jmh.runner.Runner runner = new org.openjdk.jmh.runner.Runner(opt);
                runner.run();
                runner.runSystemGC();
            }catch (RunnerException ex) {
                ex.printStackTrace();
            }*/
        }
        /*for(BenchmarkListEntry bench : benchmarks) {
            Optional<Map<String,String[]>> params = bench.getParams();

            if(params.hasValue()) {
                Map<String,String[]> pp = params.get();
                int nbSolution = 1;

                for(String[] k : pp.values()) {
                    nbSolution *= k.length;
                }

                String[] key = new String[pp.keySet().size()];
                Iterator<String> itKey = pp.keySet().iterator();
                int ii = 0;
                while(itKey.hasNext()) {
                    key[ii] = itKey.next();
                    ii++;
                }
//                System.out.println(Arrays.toString(pp.keySet().toArray()));
                for(int i=0;i<nbSolution;i++) {
                    StringBuffer paramsJson = new StringBuffer();
                    paramsJson.append("\"params\": {\n" );

                    int j=1;
                    ChainedOptionsBuilder optBuilder = new OptionsBuilder()
                            .include(bench.getUsername())
                            .shouldFailOnError(true);
//                    List<String> toShow = new ArrayList<>();
//                    for(String[] k : pp.values()) {
                    int index = 0;
                    for(String[] k : pp.values()) {
//                        toShow.add(k[(i/j)%k.length]);
                        optBuilder.param(key[index],k[(i/j)%k.length]);
                        if(index > 0) {
                            paramsJson.append(",");
                        }
                        paramsJson.append("\"").append(key[index]).append("\":").append("\"").append(k[(i/j)%k.length]).append("\"");
                        j *= k.length;
                        index++;
                    }
                    paramsJson.append("}");
//                    System.out.println(Arrays.toString(toShow.toArray()));
                    Options opt = optBuilder.forks(1).build();
                    org.openjdk.jmh.runner.Runner runner = new org.openjdk.jmh.runner.Runner(opt);

                    StringBuffer exeption = null;
                    RunResult resBench = null;
                    try {
                        resBench = runner.runSingle();
                    }catch (RunnerException ex) {
                        if(ex.getCause() != null) {
                            StringWriter sw = new StringWriter();
                            ex.getCause().printStackTrace(new PrintWriter(sw));
                            exeption = sw.getBuffer();
                        } else {
                            ex.printStackTrace();
                        }
                    }

                    if(i>0) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("{\n");
                    jsonBuilder.append("\"name\"").append("\"").append(bench.getUsername()).append("\",\n");
                    if(exeption == null) {
                        jsonBuilder.append("\"status\": \"succeed\",\n");
                    } else {
                        jsonBuilder.append("\"status\": \"failed\",\n")
                                .append("\"error\":").append("\"").append(exeption.toString()).append("\",");
                    }
                    jsonBuilder.append(paramsJson).append(",\n")
                            .append("\"mode\":").append("\"").append(bench.getMode().shortLabel()).append("\"");
                            //.append("\"iterations\":").append(resBench.getAggregatedResult().getPrimaryResult().);


                    jsonBuilder.append("}\n");
                    System.gc();
                }

            }
        }*/
        jsonBuilder.append("]\n");
        System.out.println(jsonBuilder);

    }
}
