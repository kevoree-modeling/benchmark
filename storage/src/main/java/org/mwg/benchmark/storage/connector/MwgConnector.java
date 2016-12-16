package org.mwg.benchmark.storage.connector;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mwg.*;
import org.mwg.bench.BenchModel;
import org.mwg.bench.BenchPlugin;
import org.mwg.bench.Benchmark;
import org.mwg.bench.Execution;
import org.mwg.core.utility.CoreDeferCounterSync;
import org.mwg.task.ActionFunction;
import org.mwg.task.Task;
import org.mwg.task.TaskContext;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.mwg.core.task.Actions.newTask;

public class MwgConnector implements Connector {
    private final Graph _graph;
    private final WSServer _server;

    private static final String MWG_DB_PATH = "/home/mwg-bench/benchmark-mwg-db";
//    private static final String MWG_DB_PATH = "./benchmark-mwg-db";
    private static final int MWG_SERVER_PORT = 6547;

    private String logErrorFolder = "/var/storage-logs/csv";


    private static final String timeVar = "time";
    private static final String worldVar = "world";
    private static final String commitIdVar = "commitId";
    private static final String mwgVersionVar = "mwgVersion";
    private static final String jsonObj = "jsonObj";
    private static final String execNode = "execNode";

    private static final Task createExec = newTask()
            .createTypedNode(Execution.NODE_NAME)
            .setAttribute(Execution.COMMITID,Execution.COMMITID_TYPE,"{{" + commitIdVar + "}}")
            .setAttribute(Execution.MWGVERSION,Execution.MWGVERSION_TYPE,"{{" + mwgVersionVar + "}}")
            .setAttribute(Execution.ID,Execution.ID_TYPE,"{{" + commitIdVar +"}}_{{" + mwgVersionVar + "}}")
            .addToGlobalIndex(BenchModel.IDX_IDX_EXEC,Execution.ID);

    private static final Task createBench = newTask()
            .thenDo(new ActionFunction() {
                @Override
                public void eval(TaskContext ctx) {
                    JSONObject object = (JSONObject) ctx.result().get(0);
                    Benchmark bench = (Benchmark) ctx.graph().newTypedNode(ctx.world(),ctx.time(),Benchmark.NODE_NAME);
                    bench.set(Benchmark.NAME,Benchmark.NAME_TYPE,object.getString("name"));

                    JSONObject params = object.getJSONObject("params");
                    Iterator<String> itpNames = params.keys();
                    while(itpNames.hasNext()) {
                        String pName = itpNames.next();
                        if(pName.equals(Benchmark.CACHESIZE)) {
                            bench.set(Benchmark.CACHESIZE,Benchmark.CACHESIZE_TYPE,params.getLong(pName));
                        } else if(pName.equals(Benchmark.USEHEAP)) {
                            bench.set(Benchmark.USEHEAP,Benchmark.USEHEAP_TYPE,Boolean.parseBoolean(params.getString(pName)));
                        } else if(pName.equals(Benchmark.BATCHSIZE)) {
                            bench.set(Benchmark.BATCHSIZE,Benchmark.BATCHSIZE_TYPE,params.getLong(pName));
                        } else {
                            bench.set(pName,Type.STRING,params.get(pName).toString());
                        }
                    }

                    if(object.getString("status").equals("succeed")) {
                        bench.set(Benchmark.HASFAILED,Benchmark.HASFAILED_TYPE,false);
                        bench.set(Benchmark.SCORE,Benchmark.SCORE_TYPE,object.getDouble("score"));
                        bench.set(Benchmark.SCOREUNIT,Benchmark.SCOREUNIT_TYPE,object.get("scoreUnit"));
                    } else {
                        bench.set(Benchmark.HASFAILED,Benchmark.HASFAILED_TYPE,true);
                        bench.set(Benchmark.SCORE,Benchmark.SCORE_TYPE,-1.);
                    }

                    ctx.continueWith(ctx.wrap(bench));

                }
            })
            .setAsVar("benchNode")
            .readVar(execNode)
            .addVarToRelation(Execution.BENCHS,"benchNode");


    private static final Task addBenchs = newTask()
            .travelInTime("{{" + timeVar + "}}")
            .travelInWorld("{{" + worldVar + "}}")
            .readGlobalIndex(BenchModel.IDX_IDX_EXEC, Execution.ID,"{{" + commitIdVar +"}}_{{" + mwgVersionVar + "}}")
            .ifThen(context -> context.result().size() == 0, createExec)
            .setAsVar(execNode)
            .readVar(jsonObj)
            .thenDo(new ActionFunction() {
                @Override
                public void eval(TaskContext ctx) {
                    JSONObject benchs = (JSONObject) ctx.result().get(0);
                    JSONArray array = benchs.getJSONArray("benchs");

                    List<Object> benchsData = new ArrayList<>();

                    for(int i=0;i<array.length();i++) {
                        benchsData.add(array.get(i));
                    }
                    ctx.continueWith(ctx.wrap(benchsData));
                }
            })
            .forEach(createBench);



    public MwgConnector() {
        _graph = new GraphBuilder()
                .withStorage(new LevelDBStorage(MWG_DB_PATH))
                .withPlugin(new BenchPlugin())
                .build();

        _server = new WSServer(_graph,MWG_SERVER_PORT);

    }

    public void process(String jsonData) {
        try {
            JSONObject benchsObjs = new JSONObject(jsonData);

            newTask()
                    .inject(System.currentTimeMillis())
                    .setAsVar(timeVar)
                    .inject(0)
                    .setAsVar(worldVar)
                    .inject(benchsObjs)
                    .setAsVar(jsonObj)
                    .inject(benchsObjs.getString("commitId"))
                    .setAsVar(commitIdVar)
                    .inject(benchsObjs.getString("mwgVersion"))
                    .setAsVar(mwgVersionVar)
                    .mapReduce(addBenchs)
                    .save()
                    .executeSync(_graph);
        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder log = new StringBuilder();
            log.append("[")
                    .append(System.currentTimeMillis())
                    .append("]: ")
                    .append("EROOR")
                    .append("\n");

            StringWriter sw = new StringWriter();
            if(e.getCause() == null) {
                e.printStackTrace(new PrintWriter(sw));
            } else {
                e.getCause().printStackTrace(new PrintWriter(sw));
            }

            log.append(sw.getBuffer().toString());
            writeLogs(log.toString());
        }
    }

    @Override
    public void start() {
        try {
            DeferCounterSync defer = new CoreDeferCounterSync(1);
            _graph.connect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    _server.start();
                    defer.count();
                }
            });
            defer.waitResult();
        }catch (Exception e) {
            e.printStackTrace();
            StringBuilder log = new StringBuilder();
            log.append("[")
                    .append(System.currentTimeMillis())
                    .append("]: ")
                    .append("EROOR")
                    .append("\n");

            StringWriter sw = new StringWriter();
            if(e.getCause() == null) {
                e.printStackTrace(new PrintWriter(sw));
            } else {
                e.getCause().printStackTrace(new PrintWriter(sw));
            }

            log.append(sw.getBuffer().toString());
            writeLogs(log.toString());
        }
    }

    @Override
    public void stop() {
        try {
            DeferCounterSync defer = new CoreDeferCounterSync(1);
            _graph.disconnect(new Callback<Boolean>() {
                @Override
                public void on(Boolean result) {
                    _server.stop();
                    defer.count();
                }
            });
            defer.waitResult();
        }catch (Exception e) {
            e.printStackTrace();
            StringBuilder log = new StringBuilder();
            log.append("[")
                    .append(System.currentTimeMillis())
                    .append("]: ")
                    .append("EROOR")
                    .append("\n");

            StringWriter sw = new StringWriter();
            if(e.getCause() == null) {
                e.printStackTrace(new PrintWriter(sw));
            } else {
                e.getCause().printStackTrace(new PrintWriter(sw));
            }

            log.append(sw.getBuffer().toString());
            writeLogs(log.toString());
        }
    }

    private void writeLogs(String log ) {
        File logFolder = new File(logErrorFolder);
        logFolder.mkdirs();
        File logFile = new File(logFolder.getAbsolutePath() + "/" + System.currentTimeMillis() + "-bench-log.json");

        try {
            FileWriter logWriter = new FileWriter(logFile);
            logWriter.append(log);
            logWriter.flush();
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
