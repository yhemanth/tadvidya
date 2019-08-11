#!/bin/sh

set -e
set -x

VERSION=1.0-SNAPSHOT
WEB_PACKAGE=tadvidya-web-${VERSION}.tar.gz

clean_scala() {
  pushd src/scala/tadvidya/
  rm -rf target
  popd
}

build_scala() {
  pushd src/scala/tadvidya/
  sbt dist
  popd
}

install_scala() {
  cp src/scala/tadvidya/target/universal/tadvidya-${VERSION}.zip installer/scala
  pushd installer/scala
  unzip tadvidya-${VERSION}.zip
  popd
}

containerize_scala() {
  pushd installer/scala
  cp ../../src/scala/tadvidya/Dockerfile .
  docker build -t tadvidya:${VERSION} .
  popd
}

clean_web() {
  pushd src/web/tadvidya/
  rm -rf build
  popd
}

build_web() {
  pushd src/web/tadvidya
  npm run build
  tar -czf ${WEB_PACKAGE} build/
  popd
}

install_web() {
  cp src/web/tadvidya/${WEB_PACKAGE} installer/web
  pushd installer/web
  tar -xzf ${WEB_PACKAGE}
  popd
}

containerize_web() {
  pushd installer/web
  cp ../../src/web/tadvidya/Dockerfile .
  docker build -t tadvidya-web:${VERSION} .
  popd
}

install_init_scripts() {
  pushd installer/init_scripts
  cp ../../src/python/parse_song_details.py .
  cp ../../src/python/requirements.txt .
  cp -R ../../data .
  popd
}

containerize_init_scripts() {
  pushd installer/init_scripts
  cp ../../src/python/Dockerfile .
  docker build -t tadvidya-init:${VERSION} .
  popd
}

clean_all() {
  rm -rf installer/
  clean_scala
  clean_web
}

compile_all() {
  build_scala
  build_web
}

install_all() {
  mkdir -p installer/scala installer/web installer/init_scripts
  install_scala
  install_web
  install_init_scripts
}

containerize_all() {
  containerize_scala
  containerize_web
  containerize_init_scripts
}

if [ $# -gt 0 ] && [ "$1" = "clean" ]
then
  clean_all
fi
compile_all
install_all
containerize_all
