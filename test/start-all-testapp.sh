#!/bin/bash

apm_root=/root/application/apm

mkdir /tmp/apm/test -p

echo "start weather"
cd $apm_root/test/weather && mvn spring-boot:run > /tmp/apm/test/weather.log &
sleep 10

echo "start testapp"
cd $apm_root/test/test-app && mvn spring-boot:run > /tmp/apm/test/test-app.log &
sleep 10



