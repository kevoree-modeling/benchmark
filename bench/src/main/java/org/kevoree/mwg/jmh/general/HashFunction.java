package org.kevoree.mwg.jmh.general;

import org.mwg.core.utility.PrimitiveHelper;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class HashFunction {

    @State(Scope.Thread)
    public static class Parameters {
        private Random random = new Random(1256335488963325663L);
        public long v1 = random.nextLong();
        public long v2 = random.nextLong();
        public long v3 = random.nextLong();

        @Param("10000")
        public int cacheSize;
    }

    @Benchmark
    @Warmup(batchSize = 1)
    @Measurement(batchSize = 1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public long javaHashFunction(Parameters parameters) {
        return PrimitiveHelper.tripleHash((byte)0,parameters.v1,parameters.v2,parameters.v3,parameters.cacheSize);
    }

}
