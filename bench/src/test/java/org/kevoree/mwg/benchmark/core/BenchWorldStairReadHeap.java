package org.kevoree.mwg.benchmark.core;

import org.kevoree.mwg.benchmark.AbstractBenchTest;

public class BenchWorldStairReadHeap extends AbstractBenchTest{
    @Override
    protected void init() {
        _bench = "BenchWorldStairRead";
        _roundsBefore = 0;
        _rounds = 10_000;
        _displayEach = 0;
        _useOffHeap = false;
        _cachesize = 100_000;
    }
}
