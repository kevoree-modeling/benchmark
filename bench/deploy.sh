#!/usr/bin/env bash

if [ -d ./target ]
then
   if [ -f ./target/bench-1.0-SNAPSHOT-jar-with-dependencies.jar ]
   then
        rm -f ./exec/bench.jar
        cp ./target/bench-1.0-SNAPSHOT-jar-with-dependencies.jar ./exec/bench.jar
   else
        >&2 echo "bench-1.0-SNAPSHOT-jar-with-dependencies.jar is missing in target folder"
   fi
else
    >&2 echo "target folder is missing or you don't execute the shell from storage folder."
fi
