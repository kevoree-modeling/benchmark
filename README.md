Benchmark
---

This repository is used to continuously benchmark MwDB, using [jmh](http://openjdk.java.net/projects/code-tools/jmh/).

# Benchmark process
Twice a day (around noon and midnight), a [Jenkins](https://jenkins.io/) server pulls MwDB repository and builds it. Then, it processes all the benchmarks. If the MwG build or the benchmark project build fail, the bench will also fail, without process the bench. 
If one or more bench fail, the build will also failed at the end. BUT, all the bench are processed, the results are store as normal and send to the storage server. A JSON file is created with all the bench results and is they succeed or not. If a bench failed, we add the stacktrace in the Json file. This file could be find in `/var/bench-logs` in docker that contains Jenkins.

Currently, the storage server create a CSV file to sum-up the results. If a bench had failed, we put -1 as value.

Benchmarck process:
``` shell
#!/bin/bash
#Clone and build last version of MwDB
git clone https://github.com/kevoree-modeling/mwDB.git
cd mwDB
mvn clean install

#CLone and build last verisons of benchmarks
git clone https://github.com/kevoree-modeling/benchmark.git
cd benchmark
mvn clean install
#Executes the benchmarks and create a JSON file with the results
cd bench/target
java -jar bench-1.0-SNAPSHOT-jar-with-dependencies.jar /var/bench-logs http://build:9876

```

> Next sections is for intern purpose

# Everything in a docker
The Jenkins build server and the Java server that collects the results, referenced as storage server, are in a docker on `build` machine.
The Jenkins docker has been made using lmouline/myjenkins docker, built with [DockerBuild-Jenkins](https://github.com/kevoree-modeling/benchmark/blob/master/dockerbuilds/jenkins/DockerBuild-Jenkins). Running command is: 
`docker run -p 8181:8080 -p 50001:50000 -v /home/kluster/jmh/jenkins:/var/jenkins_home --name bench-myjenkins -i myjenkins`

The storage docker has been made using lmouline/storage docker, built with [DockerBuild-Storage](https://github.com/kevoree-modeling/benchmark/blob/master/dockerbuilds/storage/DockerBuild-Storage). Running command is:
``` shell
docker run -it lmouline/storage --name bench-storage -p 9876:9876
[ctrl-C]
docker exec -t bench-storage /etc/init.d/storage-daemon start
```

# Manage the Java server daemon:
Start it: `/etc/init.d/storage-daemon start` <br>
Stop it: `/etc/init.d/storage-daemon start` <br>
Reload it: `/etc/init.d/storage-daemon reload` <br>
The reload action gets the jar file in `storage/exec` and launches it. <br>
To put the storage jar file here, after a `mvn install`, just execute `storage/deploy.sh` script. <br>

#Get the Bench data (as CSV file)
To get the CSV files containing a summary of the bench results, connect to the `build` machine and find the files in `/home/kluster/bench-results/mwg-bench`.
There is one file per bench (so two per day). The file name is created with this java code: 
`System.currentTimeMillis() + "-bench.csv"`
