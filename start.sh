#!/bin/sh
if [ -e "$1" ]
then
    kill -15 `pgrep -f "$1"`
elif [ ! -e "$1" ]
then
    echo "File not exists."
else
    echo "파라미터가 필요"
fi

./gradlew clean
./gradlew build

docker-compose -f docker-compose/redis/docker-compose.yml up -d

pwd

nohup java -jar `find . -type f -iname "$1"` &
