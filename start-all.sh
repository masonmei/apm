#/bin/bash

apm_root=/root/application/apm

mkdir /tmp/apm -p

echo "start collector"
cd $apm_root/collector && mvn spring-boot:run > /tmp/apm/collector.log & 
sleep 10

echo "start statistics"
cd $apm_root/statistics && mvn spring-boot:run > /tmp/apm/statistics.log &
sleep 10

echo "start web"
cd $apm_root/web && mvn spring-boot:run > /tmp/apm/web.log &
sleep 10

echo "start weather"
cd $apm_root/test/weather && mvn spring-boot:run > /tmp/apm/weather.log &
sleep 10

echo "start testapp"
cd $apm_root/testapp && mvn spring-boot:run > /tmp/apm/testapp.log &
sleep 10

