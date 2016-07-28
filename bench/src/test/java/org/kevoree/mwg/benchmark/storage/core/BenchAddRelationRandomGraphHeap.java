package org.kevoree.mwg.benchmark.storage.core;

import org.kevoree.mwg.benchmark.storage.AbstractBenchTest;

public class BenchAddRelationRandomGraphHeap extends AbstractBenchTest {
    @Override
    protected void init() {
        _bench = "BenchAddRelationRandomGraph";
        _roundsBefore = 0;
        _rounds = 1_000_000;
        _displayEach = 0;
        _useOffHeap = false;
        _cachesize = 1_000_000;
    }
}
