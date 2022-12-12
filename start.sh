#!/bin/sh

if [ -n "$1" ]
then
  JARFILE=`find . -type f -iname "$1"`
  echo JAR_FILE_PATH: ${JARFILE}

  if [ -e ${JARFILE} ]
  then
      echo ps -ef | grep "$1" : `ps -ef | grep "$1"`
      echo pgrep -f "$1": `pgrep -f "$1"`
      kill -15 `pgrep -f "$1"`
  elif [ ! -e ${JARFILE} ]
  then
      echo "File not exists."
  fi

  echo clean
  ./gradlew clean
  echo build
  ./gradlew build

  echo docker-compose redis
  docker-compose -f docker-compose/redis/docker-compose.yml up -d

  echo "nohup java -jar ${JARFILE} &"
  nohup java -jar ${JARFILE} &
else
  echo "파라미터가 필요"
fi