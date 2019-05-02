#!/usr/bin/env bash

set -e
git pull
# 再最外层先install
mvn clean -U install  -DskipTests
# 再package broker的部分
cd cy-msq-broker
mvn clean -U package -Pdev -DskipTests
mv target/msq-broker.jar /home/msq/project

cd /home/msq/project
java -jar msq-broker.jar &