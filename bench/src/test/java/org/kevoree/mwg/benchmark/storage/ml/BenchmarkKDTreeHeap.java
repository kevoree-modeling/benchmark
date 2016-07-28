package org.kevoree.mwg.benchmark.storage.ml;


import org.kevoree.mwg.benchmark.storage.AbstractBenchTest;

public class BenchmarkKDTreeHeap extends AbstractBenchTest{
    @Override
    protected void init() {
        _bench = "BenchmarkKDTree";
        _roundsBefore = 0;
        _rounds = 100_000;
        _displayEach = 0;
        _useOffHeap = false;
        _cachesize = 5_000_000;
    }
}
