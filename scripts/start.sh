#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="/home/ubuntu/app/Titto_Backend/build/libs/Titto_Backend-0.0.1-SNAPSHOT.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> "$DEPLOY_LOG"
cd /home/ubuntu/app/Titto_Backend/build/libs
nohup java -jar "$JAR_FILE" >> /dev/null 2>&1 &

sleep 5  # 프로세스가 실행되기를 기다립니다. 필요한 경우 조정하세요.

CURRENT_PID=$(pgrep -f "java -jar $JAR_FILE")
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> "$DEPLOY_LOG"
