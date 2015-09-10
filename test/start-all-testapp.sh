#!/bin/bash

WORK_DIR=`dirname $0`
WORK_DIR=`cd ${WORK_DIR}; pwd`
echo "Working in ${WORK_DIR}"

mkdir /tmp/apm/test -p

echo "start weather"
cd ${WORK_DIR}/weather && mvn spring-boot:run > /tmp/apm/test/weather.log &
sleep 10

echo "start testapp"
cd ${WORK_DIR}/test-app && mvn spring-boot:run > /tmp/apm/test/test-app.log &
sleep 10



