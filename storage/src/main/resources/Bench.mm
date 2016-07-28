class org.mwg.bench.Benchmark {
    att name : String

}

class org.mwg.bench.Parameter {
    att mode : String
    att nbThreads : Integer
    att nbForks: Integer
    att warmupIterations : Integer
    att warmupTime : String
    att warmupBatchSize : Integer
    att measurementIterations : Integer
    att measurementTime : String
    att measurementBatchSize  : Integer
}

class org.mwg.bench.Metric {
    att name : String
    att score : Double
    att scoreError : Double
}
