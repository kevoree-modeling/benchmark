package org.mwg.benchmark.storage.connector;

import org.mwg.*;
import org.mwg.bench.BenchModel;
import org.mwg.bench.Execution;
import org.mwg.core.utility.CoreDeferCounterSync;
import org.mwg.task.Task;

import static org.mwg.core.task.Actions.newTask;

public class MwgConnector implements Connector {
    private final Graph _graph;
    private final WSServer _server;

//    private static final String MWG_DB_PATH = "/home/mwg-bench/benchmark-mwg-db";
    private static final String MWG_DB_PATH = "./benchmark-mwg-db";
    private static final int MWG_SERVER_PORT = 6547;

    private static final String timeVar = "time";
    private static final String worldVar = "world";
    private static final String commitIdVar = "commitId";
    private static final String mwgVersionVar = "mwgVersion";
    private static final String jsonLinesVar = "jsonLines";

    private static final Task createExec = newTask()
            .createTypedNode(Execution.NODE_NAME)
            .setAttribute(Execution.COMMITID,Execution.COMMITID_TYPE,"{{" + commitIdVar + "}}")
            .setAttribute(Execution.MWGVERSION,Execution.MWGVERSION_TYPE,"{{" + mwgVersionVar + "}}")
            .addToGlobalIndex(BenchModel.IDX_IDX_EXECUTION,Execution.COMMITID);

//    private static final Task updateVal = newTask()
//            .travelInTime("{{" + timeVar + "}}")
//            .travelInWorld("{{" + worldVar + "}}")
//            .then(readJsonLines("{{" + jsonLinesVar + "}}"))
//            .then(ImporterActions.jsonMatch("commitId",newTask().println("{{result}}")))
//            .then(ImporterActions.jsonMatch("mwgVersion",newTask().println("{{result}}")))
            ;
//            .readGlobalIndex(BenchModel.IDX_IDX_EXECUTION, Execution.COMMITID,"{{" + commitIdVar + "}}")
//            .ifThen(context -> context.result().size() == 0, createExec)




    public MwgConnector() {
        _graph = new GraphBuilder()
                .withStorage(new LevelDBStorage(MWG_DB_PATH))
                .build();

        _server = new WSServer(_graph,MWG_SERVER_PORT);

    }

    public void process(String jsonData) {
//        newTask()
//                .inject(System.currentTimeMillis())
//                .setAsVar(timeVar)
//                .inject("0")
//                .setAsVar(worldVar)
//                .inject(jsonData)
//                .setAsVar(jsonLinesVar)
//                .mapReduce(updateVal)
//                .addHook(new VerboseHook())
//                .executeSync(_graph);
    }

    @Override
    public void start() {
        DeferCounterSync defer = new CoreDeferCounterSync(1);
        _graph.connect(new Callback<Boolean>() {
            @Override
            public void on(Boolean result) {
                _server.start();
                defer.count();
            }
        });
        defer.waitResult();
    }

    @Override
    public void stop() {
        DeferCounterSync defer = new CoreDeferCounterSync(1);
        _graph.disconnect(new Callback<Boolean>() {
            @Override
            public void on(Boolean result) {
                _server.stop();
                defer.count();
            }
        });
        defer.waitResult();
    }
}
