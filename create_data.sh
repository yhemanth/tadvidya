#!/bin/sh
set -e

if ! which flyway;
then
  echo "Flyway database migration tool is not found in path."
  echo "Please install flyway (https://flywaydb.org/) and setup PATH to include the flyway executable";
  exit -1
fi

flyway  -configFiles=src/scala/tadvidya/conf/flyway.conf migrate

python3 src/python/parse_song_details.py
