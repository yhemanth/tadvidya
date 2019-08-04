#!/bin/sh
TV_VERSION=1.0-SNAPSHOT
docker run -p 9000:9000 -e EXTERNAL_JDBC_URL='jdbc:postgresql://192.168.1.8:5432/tadvidya?currentSchema=tadvidya' --rm --name tadvidya-scala tadvidya:${TV_VERSION} &
docker run -p 5000:5000 --rm --name tadvidya-web tadvidya-web:${TV_VERSION}
