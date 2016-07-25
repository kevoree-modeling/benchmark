package org.kevoree.mwg.jmh.general;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class AddRelationLongArray {

    @State(Scope.Benchmark)
    public static class Parameter {
        @Param("1")
        public int lenghtArray;
    }

    @Benchmark
    @Warmup(batchSize = 1)
    @Measurement(batchSize = 1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public long benchAddRelationLongArray(Parameter parameter) {
        long[] array = new long[0];
        for(int i=0;i<parameter.lenghtArray;i++) {

            long[] tmp = new long[array.length + 1];
            System.arraycopy(array,0,tmp,0,array.length);
            tmp[array.length] = 1L;
            array = tmp;
        }
        return array.length;
    }
}
