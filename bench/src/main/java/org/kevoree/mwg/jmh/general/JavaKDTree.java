package org.kevoree.mwg.jmh.general;

import org.kevoree.mwg.benchmark.utils.KDNodeJava;
import org.mwg.ml.common.distance.EuclideanDistance;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JavaKDTree {

    @State(Scope.Thread)
    public static class Parameter {
        @Param("1000000")
        int vecsSize;

        ArrayList<double[]> vecs=new ArrayList<double[]>();
        Object[] values;
        KDNodeJava root=new KDNodeJava();
        int counter = 0;

        @Setup
        public void setup() {
            Random random = new Random(1256335488963325663L);
            root.setThreshold(1e-30);
            root.setDistance(new EuclideanDistance());
            values=new Object[vecsSize];

            int dim = 4;

            for(int i=0;i<vecsSize;i++){
                double[] v= new double[dim];
                for(int j = 0; j< dim; j++){
                    v[j]=random.nextDouble();
                }
                vecs.add(v);
                values[i]=new Object();
            }
        }

    }


    @Benchmark
    @Fork(10)
    @Warmup(iterations = 0, batchSize = 1)
    @Measurement(iterations = 1_000_000, batchSize = 1)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object javaKDTree(Parameter parameter) {
        parameter.root.insert(parameter.vecs.get(parameter.counter),parameter.values[parameter.counter],null);
        parameter.counter++;
        return null;
    }
}
