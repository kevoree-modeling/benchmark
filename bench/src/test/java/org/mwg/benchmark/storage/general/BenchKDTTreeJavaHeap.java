package org.mwg.benchmark.storage.general;

import org.mwg.benchmark.storage.AbstractBenchTest;


public class BenchKDTTreeJavaHeap extends AbstractBenchTest {
    @Override
    protected void init() {
        _bench = " BenchKDTreeJava";
        _roundsBefore = 0;
        _rounds = 1_000_000;
        _displayEach = 0;
        _useOffHeap = false;
        _cachesize = 100_000;
    }
}
