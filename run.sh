#!/bin/sh
set -x
set -e

VERSION=1.0-SNAPSHOT

run_scala() {
  pushd installer/scala
  tadvidya-${VERSION}/bin/tadvidya -Dplay.http.secret.key=KJPO9FHA2AWG9T8IQUH4ZVSV6QXXEGH6 2>&1 >> /tmp/tadvidya-scala.out &
  popd
}

run_web() {
  pushd installer/web
  npm install -g serve
  serve -s build 2>&1 >> /tmp/tadvidya-web.out &
  popd
}

stop_all() {
  SCALA_PID=`ps -ef | grep java | grep tadvidya | grep ProdServerStart | awk '{print $2}'`
  if ! [ -z ${SCALA_PID} ]; then 
    kill ${SCALA_PID}
  fi
  WEB_PID=`ps -ef | grep node | grep serve | awk '{print $2}'`
  if ! [ -z ${WEB_PID} ]; then
    kill ${WEB_PID}
  fi
}

run_all() {
  run_scala
  run_web
}

stop_all
run_all

