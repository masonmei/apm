#/bin/bash

while true;
do
  wget -T 5 --tries=1 -q http://localhost:8080/city/sync
  sleep 2
  wget -T 5 --tries=1 -q http://localhost:28080/index.html
  sleep 2
  wget -T 5 --tries=1 -q http://localhost:28080/callSelf/getCurrentTimestamp.pinpoint
  sleep 2
  wget -T 10 --tries=1 -q http://localhost:28080/consumeCpu.pinpoint
  sleep 2
  wget -T 10 --tries=1 -q http://localhost:28080/consumeMemory.pinpoint
  sleep 2
  wget -T 5 --tries=1 -q http://localhost:28080/getCurrentTimestamp.pinpoint
  sleep 2
done

