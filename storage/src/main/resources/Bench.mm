class org.mwg.bench.Benchmark {
    att name: String

    att failed: Boolean


    att mode: String
    att nbThreads: Integer
    att nbForks: Integer
    att warmupIterations: Integer
    att warmupTime: String
    att warmupBatchSize: Integer
    att measurementIterations: Integer
    att measurementTime: String
    att measurementBatchSize : Integer

    att score: Double
    att scoreError: Double

}

index idx_benchmark: org.mwg.bench.Benchmark {
    name
}

class org.mwg.bench.Execution {
    att commitId: String
    att mwgVersion: String

    rel benchs: org.mwg.bench.Benchmark
}

index idx_execution : org.mwg.bench.Execution {
    commitId
}