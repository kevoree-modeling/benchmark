package org.kevoree.mwg.benchmark;


import org.kevoree.mwg.benchmark.utils.RestCall;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.BenchmarkList;
import org.openjdk.jmh.runner.BenchmarkListEntry;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.format.OutputFormat;
import org.openjdk.jmh.runner.format.OutputFormatFactory;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.openjdk.jmh.util.Optional;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BenchRunner {
    private static final String START_STRING = "\"";
    private static final String END_STRING = START_STRING;
    private static final String EOL = "\n";
    private static final String DATA_SEP = ",";
    private static final String START_OBJ = "{";
    private static final String END_OBJ = "}";
    private static final String START_ARRAY = "[";
    private static final String END_ARRAY = "]";
    private static final String DATA_VALUE_SEP = ":";
    private static final String INIT_FAILED_MSG = "One or more benchmark(s) had encountered error, and fail on error was requested";


    private static String putEndOfLineCharacter(String toModify) {
        char[] toReturn = new char[toModify.length()];
        char eol = System.getProperty("line.separator").charAt(0);
        int indexAdded = 0;
        for(int i=0;i<toModify.length();i++) {
            if(toModify.charAt(i) == eol || toModify.charAt(i) == '\t') {
                char[] tmp = new char[toReturn.length + 1];
                System.arraycopy(toReturn,0,tmp,0,toReturn.length);
                toReturn = tmp;
                toReturn[indexAdded] = '\\';
                indexAdded++;
                if(toModify.charAt(i) == eol) {
                    toReturn[indexAdded] = 'n';
                } else {
                    toReturn[indexAdded] = 't';
                }
                indexAdded++;
            } else {
                toReturn[indexAdded] = toModify.charAt(i);
                indexAdded++;
            }
        }
        return new String(toReturn);
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            System.err.println("Wrong arguments number: should be 2: [logFolder] [http address to push data]");
            System.exit(2);
        }

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append(START_ARRAY).append(EOL);

        Set<BenchmarkListEntry> benchmarks;
        {
            BenchmarkList list = BenchmarkList.defaultList();
            Options options = new OptionsBuilder().build();
            OutputFormat out = OutputFormatFactory.createFormatInstance(System.out, VerboseMode.NORMAL);
            benchmarks = list.find(out, options.getIncludes(), options.getExcludes());
        }

        StringBuilder failedBench = new StringBuilder();
        failedBench.append(INIT_FAILED_MSG);
        for (Iterator<BenchmarkListEntry> iterator = benchmarks.iterator(); iterator.hasNext(); ) {
            BenchmarkListEntry bench = iterator.next();
            Optional<Map<String, String[]>> params = bench.getParams();

            String[] keys;
            String[][] values;
            //get all executions
            if (params.hasValue()) {
                Map<String, String[]> paramsMap = params.get();

                int nbSolution = 1;
                for (String[] paramValues : paramsMap.values()) {
                    nbSolution *= paramValues.length;
                }


                keys = new String[paramsMap.keySet().size()];
                {
                    Iterator<String> itKey = paramsMap.keySet().iterator();
                    int index = 0;
                    while (itKey.hasNext()) {
                        keys[index] = itKey.next();
                        index++;
                    }
                }

                values = new String[nbSolution][keys.length];
                for (int sol = 0; sol < nbSolution; sol++) {
                    int j = 1;
                    int index = 0;
                    String[] toAdd = new String[keys.length];
                    for (String[] v : paramsMap.values()) {
                        toAdd[index] = v[(sol / j) % v.length];
                        index++;
                        j *= v.length;
                    }
                    values[sol] = toAdd;
                }
            } else {
                keys= new String[0];
                values = new String[1][0];
            }


            //Run and create
            for(int nbExec=0;nbExec<values.length;nbExec++) {
                jsonBuilder.append(START_OBJ);
                //name
                jsonBuilder.append(START_STRING).append("name").append(END_STRING)
                        .append(DATA_VALUE_SEP).append(START_STRING).append(bench.getUsername()).append(END_STRING)
                        .append(DATA_SEP).append(EOL);

                ChainedOptionsBuilder optionsBuilder = new OptionsBuilder().include(bench.getUsername()).shouldFailOnError(true);
                StringBuilder paramsStr = null;
                jsonBuilder.append(START_STRING).append("params").append(END_STRING).append(DATA_VALUE_SEP)
                        .append(START_OBJ).append(EOL);
                paramsStr = new StringBuilder();


                for(int numKey=0;numKey<keys.length;numKey++) {
                    optionsBuilder.param(keys[numKey],values[nbExec][numKey]);
                    jsonBuilder.append(START_STRING).append(keys[numKey]).append(END_STRING).append(DATA_VALUE_SEP)
                            .append(START_STRING).append(values[nbExec][numKey]).append(END_STRING);
                    paramsStr.append(keys[numKey]).append("=").append(values[nbExec][numKey]);
                        jsonBuilder.append(DATA_SEP);
                        paramsStr.append(DATA_SEP);
                    jsonBuilder.append(EOL);
                }

                jsonBuilder.append(START_STRING).append("batchSize").append(END_STRING).append(DATA_VALUE_SEP)
                        .append(START_STRING).append(bench.getMeasurementBatchSize().orElse(-1)).append(END_STRING)
                        .append(EOL);

                paramsStr.append("batchSize").append("=").append(bench.getMeasurementBatchSize().orElse(-1));

                jsonBuilder.append(END_OBJ).append(DATA_SEP).append(EOL);
                Options options = optionsBuilder.build();
                runBench(jsonBuilder, bench.getUsername(), options, failedBench, paramsStr);

                jsonBuilder.append(END_OBJ);
                if(nbExec <= values.length - 2){
                    jsonBuilder.append(DATA_SEP);
                }
                jsonBuilder.append(EOL);
            }

            if(iterator.hasNext()) {
                jsonBuilder.append(DATA_SEP);
            }

        }

        jsonBuilder.append(END_ARRAY);


        File logFolder = new File(args[0]);
        logFolder.mkdirs();

        File logFile = new File(logFolder.getAbsolutePath() + "/" + System.currentTimeMillis() + "-bench.json");
        FileWriter logWriter = new FileWriter(logFile);
        logWriter.append(jsonBuilder);
        logWriter.flush();
        logWriter.close();

        RestCall.save(args[1],jsonBuilder.toString());

        if(failedBench.length() > INIT_FAILED_MSG.length()) {
            throw new RuntimeException(failedBench.toString());
        }

    }

    private static void runBench(StringBuilder jsonBuilder, String benchName, Options options, StringBuilder failedBenchs, StringBuilder params) {
        StringBuffer error = null;
        RunResult runResult = null;
        try {
            Runner runner = new Runner(options);
            Collection<RunResult> results = runner.run();
            Iterator<RunResult> resultIterator = results.iterator();
            if(resultIterator.hasNext()) {
                runResult = resultIterator.next();
                if(resultIterator.hasNext()) {
                    throw new RuntimeException("There is more than one result. Please check the runner code" + benchName);
                }
            } else {
                throw new RuntimeException("There is more than one result " + results.size() + ". Please check the runner code: " + benchName);
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

            failedBenchs.append(benchName).append(START_ARRAY).append(params).append(END_ARRAY).append(EOL)
                    .append(error).append(EOL);
        }

        jsonBuilder.append(START_STRING).append("status").append(END_STRING).append(DATA_VALUE_SEP);
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
                    .append(START_STRING).append("op/s").append(END_STRING).append(EOL);

        } else {
            // "failed",\n
            // "reason": "exception caught",\n
            jsonBuilder.append(START_STRING).append("failed").append(END_STRING).append(DATA_SEP).append(EOL)
                    .append(START_STRING).append("reason").append(END_STRING).append(DATA_VALUE_SEP)
                    .append(START_STRING)
                    .append(putEndOfLineCharacter(error.toString())).append(END_STRING).append(EOL);
        }
    }
}
