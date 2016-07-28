#!/usr/bin/env bash

if [ -d ./target ]
then
   if [ -f ./target/benchmark.storage-1.0-SNAPSHOT-jar-with-dependencies.jar ]
   then
        rm ./exec/server.jar
        cp ./target/benchmark.storage-1.0-SNAPSHOT-jar-with-dependencies.jar ./exec/
        mv ./exec/benchmark.storage-1.0-SNAPSHOT-jar-with-dependencies.jar ./exec/server.jar
   else
        >&2 echo "benchmark.storage-1.0-SNAPSHOT-jar-with-dependencies.jar is missing in target folder"
   fi
else
    >&2 echo "target folder is missing or you don't execute the shell from storage folder."
fi
