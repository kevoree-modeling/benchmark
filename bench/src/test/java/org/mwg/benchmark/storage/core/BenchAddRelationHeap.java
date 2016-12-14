package org.mwg.benchmark.storage.core;

import org.mwg.benchmark.storage.AbstractBenchTest;

public class BenchAddRelationHeap extends AbstractBenchTest {
    @Override
    protected void init() {
        _bench = "BenchAddRelation";
        _roundsBefore = 0;
        _rounds = 1_000_000;
        _displayEach = 0;
        _useOffHeap = false;
        _cachesize = 5_000_000;
    }
}
