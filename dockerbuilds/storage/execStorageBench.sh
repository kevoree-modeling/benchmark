#!/bin/bash

WORKDIR=$1

java -jar $WORKDIR/server.jar >> $WORKDIR/server.log 2>&1 &
echo $! >  $WORKDIR/serverBench.pid
