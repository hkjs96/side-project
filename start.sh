#!/bin/sh

if [ -n "$1" ]
then
  JARFILE=`find . -type f -iname "$1"`
  echo JAR_FILE_PATH: ${JARFILE}

  if [ -e ${JARFILE} ]
  then
      echo pgrep -f ${JARFILE}: `pgrep -f ${JARFILE}`
      kill -15 `pgrep -f ${JARFILE}`
  elif [ ! -e ${JARFILE} ]
  then
      echo "File not exists."
  fi

  echo ./gradlew clean
  ./gradlew clean
  echo ./gradlew build
  ./gradlew build

  echo docker-compose redis
  docker-compose -f docker-compose/redis/docker-compose.yml up -d

  echo "nohup java -jar ${JARFILE} &"
  nohup java -jar ${JARFILE} &
else
  echo "파라미터가 필요"
fi