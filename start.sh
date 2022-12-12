#!/bin/sh

if [ -n "$1" ]
then
  JARFILE=`find . -type f -iname "$1"`
  echo ${JARFILE}

  if [ -e ${JARFILE} ]
  then
      kill -15 `pgrep -f ${JARFILE}`
  elif [ ! -e ${JARFILE} ]
  then
      echo "File not exists."
  fi

  ./gradlew clean
  ./gradlew build

  docker-compose -f docker-compose/redis/docker-compose.yml up -d

  pwd

  nohup java -jar `find . -type f -iname ${JARFILE}` &
else
  echo "파라미터가 필요"
fi