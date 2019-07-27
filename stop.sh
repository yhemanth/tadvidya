#/bin/sh
set -e
set -x


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

stop_all
