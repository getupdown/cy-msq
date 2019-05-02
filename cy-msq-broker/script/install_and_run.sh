#!/usr/bin/env bash

set -e
git pull
cd ..
mvn clean -U install -Pdev -DskipTests
mv target/msq-broker.jar /home/msq/project

cd /home/msq/project
java -jar msq-broker.jar &