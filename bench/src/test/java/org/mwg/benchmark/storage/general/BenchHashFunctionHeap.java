package org.mwg.benchmark.storage.general;

import org.mwg.benchmark.storage.AbstractBenchTest;


public class BenchHashFunctionHeap extends AbstractBenchTest {
    @Override
    protected void init() {
        _bench = "BenchHashFunction";
        _roundsBefore = 1_000;
        _rounds = 100_000_000;
        _displayEach = 0;
        _useOffHeap = false;
        _cachesize = 100_000;
    }
}
