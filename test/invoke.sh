#!/bin/bash

while true;
do
  wget -q http://localhost:38080/city/sync
  sleep 2
  wget -q http://localhost:38081/index.html
  sleep 2
  wget -q http://localhost:38081/callSelf/getCurrentTimestamp.pinpoint
  sleep 2
  wget -q http://localhost:38081/consumeCpu.pinpoint
  sleep 2
  wget -q http://localhost:38081/consumeMemory.pinpoint
  sleep 2
  wget -q http://localhost:38081/getCurrentTimestamp.pinpoint
  sleep 2
  rm -f sync*
  rm -f index.*
  rm -f *.pinpoint*
done

