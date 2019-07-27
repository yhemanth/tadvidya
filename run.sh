#!/bin/sh
set -x
set -e

VERSION=1.0-SNAPSHOT
SCRIPT_DIR="$(dirname "$0")"

source $SCRIPT_DIR/stop.sh

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

run_all() {
  run_scala
  run_web
}

stop_all
run_all

