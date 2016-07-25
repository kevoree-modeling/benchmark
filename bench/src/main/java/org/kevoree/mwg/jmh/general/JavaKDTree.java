package org.kevoree.mwg.jmh.general;

import org.kevoree.mwg.benchmark.utils.KDNodeJava;
import org.mwg.ml.common.distance.EuclideanDistance;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JavaKDTree {

    private static int dim=4;
    private static ArrayList<double[]> vecs=new ArrayList<double[]>();
    private static Object[] values;
    private static KDNodeJava root=new KDNodeJava();


    @State(Scope.Benchmark)
    public static class Parameter {
        @Param
        public int vecsSize;

        @Setup
        public void setup() {
            Random random = new Random(1256335488963325663L);
            root.setThreshold(1e-30);
            root.setDistance(new EuclideanDistance());
            values=new Object[vecsSize];

            for(int i=0;i<vecsSize;i++){
                double[] v= new double[dim];
                for(int j=0;j<dim;j++){
                    v[j]=random.nextDouble();
                }
                vecs.add(v);
                values[i]=new Object();
            }
        }

    }


    @Benchmark
    @Warmup(batchSize = 1)
    @Measurement(batchSize = 1)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public Object javaKDTree() {
        for(int i=0;i<vecs.size();i++) {
            root.insert(vecs.get(i), values[i], null);
        }
        return null;
    }
}
