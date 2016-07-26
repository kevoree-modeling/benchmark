package org.kevoree.mwg.jmh.general;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class AddRelationLongArray {

    @State(Scope.Thread)
    public static class Parameter {
        @Param("1")
        public int coeff;

        @Param("1")
        public int added;

        long[] array = new long[0];
        int counter = 0;
    }

    @Benchmark
    @Fork(10)
    @Warmup(iterations = 100, batchSize = 1)
    @Measurement(iterations = 1_000_000, batchSize = 1)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public long benchAddRelationLongArray(Parameter parameter) {
            if(parameter.counter<parameter.array.length) {
                parameter.array[parameter.counter] = parameter.counter;
            } else {
                long[] tmp = new long[parameter.array.length * parameter.coeff + parameter.added];
                System.arraycopy(parameter.array,0,tmp,0,parameter.array.length);
                tmp[parameter.array.length] = 1L;
                parameter.array = tmp;
            }
        parameter.counter++;
        return parameter.array.length;
    }
}
