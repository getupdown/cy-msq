#!/usr/bin/env bash

set -e
git pull
# 先把core发布到本地仓库
cd cy-msq-core
mvn clean -U install  -DskipTests
cd ..
# 再package broker的部分
cd cy-msq-broker
mvn clean -U package -Pdev -DskipTests
mv target/msq-broker.jar /home/msq/project

cd /home/msq/project
java -jar msq-broker.jar &