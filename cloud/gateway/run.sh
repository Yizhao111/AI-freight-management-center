#!/usr/bin/env bash
mvn package

docker build -t gateway .