package org.kevoree.mwg.benchmark.general;

import org.kevoree.mwg.benchmark.AbstractBenchTest;

public class BenchAddRelationLongArrayHeap extends AbstractBenchTest {
    @Override
    protected void init() {
        _bench = "BenchAddRelationLongArray";
        _roundsBefore = 0;
        _rounds = 100_000;
        _displayEach = 0;
        _useOffHeap = false;
        _cachesize = 1_500_000;
    }
}
