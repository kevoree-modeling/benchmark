package org.kevoree.mwg.benchmark;


import org.openjdk.jmh.runner.BenchmarkList;
import org.openjdk.jmh.runner.BenchmarkListEntry;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.format.OutputFormat;
import org.openjdk.jmh.runner.format.OutputFormatFactory;
import org.openjdk.jmh.runner.options.CommandLineOptionException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.openjdk.jmh.util.Optional;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Runner {

    public static void main(String[] args) throws ClassNotFoundException, IOException, RunnerException, CommandLineOptionException {

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        BenchmarkList list = BenchmarkList.defaultList();
        Options options = new OptionsBuilder().build();
        OutputFormat out = OutputFormatFactory.createFormatInstance(System.out, VerboseMode.NORMAL);
        Set<BenchmarkListEntry> benchmarks = list.find(out,options.getIncludes(),options.getExcludes());

        for(BenchmarkListEntry bench : benchmarks) {
            Optional<Map<String,String[]>> params = bench.getParams();


            Options opt = new OptionsBuilder()
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
            }
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
