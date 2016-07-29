Benchmark
---

This repository is used to continuously benchmark MwDB, using [jmh](http://openjdk.java.net/projects/code-tools/jmh/).

# Benchmark process
Twice a day (around noon and midnight), a [Jenkins](https://jenkins.io/) server pull MwDB repository and build it. Then, it processes all the benchmarks. If the build fail or one of the benchmark fail, all the process will fail. (We currently do this to detect without big effort any problem in the bench process). We use the Json output format to create a bench result file, that we send to a Java server using post request (created with Wget). Below, you can find the shell script that make all theses step (**Last update: 29th July, 2016**)

``` shell
#!/bin/bash
#Clone and build last version of MwDB
git clone https://github.com/kevoree-modeling/mwDB.git
cd mwDB
#git reset --soft 93700d8
mvn clean install

#CLone and build last verisons of benchmarks
git clone https://github.com/kevoree-modeling/benchmark.git
cd benchmark
mvn clean install
#Executes the benchmarks and create a JSON file with the results
java -jar bench/target/mwg-benchmark.jar -rf json -rff benchmark.json -v EXTRA -foe true

#Send the json
wget --header="Content-Type:application/json" --post-file benchmark.json http://0.0.0.0:9876
```

> Next sections is for intern purpose

# Everything in a docker
The Jenkins build server and the Java server that collects the resulst, referenced as storage server, are in a docker on `build` machine.
The Jenkins docker has be made using lmouline/myjenkins docker, built with [DockerBuild-Jenkins](). Running command: 
`docker run -p 8181:8080 -p 50001:50000 -v /home/kluster/jmh/jenkins:/var/jenkins_home --name bench-myjenkins -i myjenkins`

The storage docker has be made using lmouline/storage docker, built with [DockerBuild-Storage](). Running command:
``` shell
docker run -it lmouline/storage --name bench-storage -p 9876:9876
[ctrl-C]
docker exec -t bench-storage /etc/init.d/storage-daemon start
```

# Manage the Java server daemon:
Start it: `/etc/init.d/storage-daemon start`
Stop it: `/etc/init.d/storage-daemon start`
Reload it: `/etc/init.d/storage-daemon reload`
The reload action get the jar file in `storage/exec` and launch it.
To put the storage jar file here, after a `mvn install`, just execute `storage/deploy.sh` script.
