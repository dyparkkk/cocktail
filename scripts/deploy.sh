#!/bin/bash
 PROJECT_NAME="github_action"
 JAR_PATH="/home/ubuntu/github_action/build/libs/*.jar"
 DEPLOY_PATH="/cocktail/"
 DEPLOY_LOG_PATH="/data/logs/deploy.log"
 DEPLOY_ERR_LOG_PATH="/data/logs/deploy_err.log"
 APPLICATION_LOG_PATH="/data/logs/application.log"
 BUILD_JAR=$(ls $JAR_PATH)
 JAR_NAME=$(basename $JAR_PATH)

 echo "===== 배포 시작 : $(date +%c) =====" >> $DEPLOY_LOG_PATH

 echo "> build 파일명: $JAR_NAME" >> $DEPLOY_LOG_PATH
 echo "> build 파일 복사" >> $DEPLOY_LOG_PATH
 cp $BUILD_JAR $DEPLOY_PATH

 echo "> 현재 동작중인 어플리케이션 pid 체크" >> $DEPLOY_LOG_PATH
 CURRENT_PID=$(pgrep -f $JAR_NAME)

 if [ -z $CURRENT_PID ]
 then
   echo "> 현재 동작중인 어플리케이션 존재 X" >> $DEPLOY_LOG_PATH
 else
   echo "> 현재 동작중인 어플리케이션 존재 O" >> $DEPLOY_LOG_PATH
   echo "> 현재 동작중인 어플리케이션 강제 종료 진행" >> $DEPLOY_LOG_PATH
   echo "> kill -9 $CURRENT_PID" >> $DEPLOY_LOG_PATH
   kill -9 $CURRENT_PID
 fi

 DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
 echo "> DEPLOY_JAR 배포" >> $DEPLOY_LOG_PATH
 rm /data/logs/cocktail-api.log
 nohup java -jar $DEPLOY_JAR > /data/logs/cocktail-api.log 2>&1 &

 sleep 3

 echo "> 배포 종료 : $(date +%c)" >> $DEPLOY_LOG_PATH
