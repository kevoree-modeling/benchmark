import groovy.json.JsonSlurper

def conf = new JsonSlurper().parse(new File("./configuration.json"))

List benchs = conf.benchmarks

def bench
def builder = new ProcessBuilder()
builder.redirectErrorStream(true)

def logFile = new File(conf.logFile)
logFile.createNewFile()

def csvFile = new File(conf.resultFile)
if(!csvFile.exists()) {
    csvFile.createNewFile()
    csvFile.write("timestamp")
}

def i
for(i=0; i<benchs.size();i++) {
    bench = benchs.get(i)
    def command = "java -jar bench-1.0-SNAPSHOT.jar ${bench.class} ${bench.roundsBefore} ${bench.rounds} " +
            "${bench.displayEach} ${bench.useOffHeap} ${bench.cacheSize}"

    def benchID = "${bench.class}(${bench.roundsBefore},${bench.rounds}," +
            "${bench.displayEach},${bench.useOffHeap},${bench.cacheSize})"

    def reader = new BufferedReader(csvFile.newInputStream())
    def line
    line = reader.readLine()
    def columns = line.split(";")
    def j
    for(j = 1;j<columns.size();j++) {
        if(columns[j].equals(benchID)) {
            break;
        }
    }

    if(j == columns.length) {
        csvFile.write(";$benchID")
    }


    reader.close()


    builder.command(command.split(' '))
    def process = builder.start()
    def stdout = process.inputStream

    reader = new BufferedReader(new InputStreamReader(stdout))

    println "[${new Date()}] Execute $command"
    logFile.append "[${new Date()}] Execute $command\n"

    while((line = reader.readLine()) != null) {
        println "[${new Date()}] $line"
        logFile.append "[${new Date()}] $line\n"
    }
    reader.close()
    println "[${new Date()}] Process finished with exit code ${process.waitFor()}"
    logFile.append "[${new Date()}] Process finished with exit code ${process.waitFor()}\n"

    def toAppend = new StringBuilder()
    toAppend.append "${System.currentTimeMillis()}"
    for(def k = 0;k<j;k++) {
        toAppend.append(";")
    }
    toAppend.append(55)
    for(def k = j;k<columns.length;k++) {
        toAppend.append(";")
    }
    csvFile.append toAppend.toString()
}




