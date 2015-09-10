#!/bin/bash

WORK_DIR=`dirname $0`
WORK_DIR=`cd ${WORK_DIR}; pwd`
echo "Working in ${WORK_DIR}"

mkdir /tmp/apm -p

echo "start collector"
cd ${WORK_DIR}/collector && mvn spring-boot:run > /tmp/apm/collector.log &
sleep 10

echo "start statistics"
cd ${WORK_DIR}/statistics && mvn spring-boot:run > /tmp/apm/statistics.log &
sleep 10

echo "start web"
cd ${WORK_DIR}/web && mvn spring-boot:run > /tmp/apm/web.log &
sleep 10

