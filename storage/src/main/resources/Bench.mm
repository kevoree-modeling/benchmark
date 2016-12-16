class org.mwg.bench.Benchmark {
    att name: String

    att hasFailed: Boolean

    att cacheSize : Long
    att useHeap : Boolean
    att batchSize : Long

    att score: Double
    att scoreUnit: String

}

index idx_benchmark: org.mwg.bench.Benchmark {
    name
}

class org.mwg.bench.Execution {
    att id : String
    att commitId: String
    att mwgVersion: String

    rel benchs: org.mwg.bench.Benchmark
}

index idx_exec: org.mwg.bench.Execution {
    id
}

