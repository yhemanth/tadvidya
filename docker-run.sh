#!/bin/sh
docker run -p 9000:9000 -e EXTERNAL_JDBC_URL='jdbc:postgresql://192.168.1.5:5432/tadvidya?currentSchema=tadvidya' --rm --name tadvidya-scala tadvidya:1.0-SNAPSHOT
