#!/bin/bash
COMMAND="$1"


APP_NAME="zy-contract-1.0.0-SNAPSHOT.jar"
APP_LOG="logs/zy-contract-console.log"
APP_ACTIVE="dev"


if [[ "$COMMAND" != "start" ]] && [[ "$COMMAND" != "stop" ]] && [[ "$COMMAND" != "restart" ]]; then
	echo "Usage: $0 start | stop | restart"
	exit 0
fi

function start()
{
    nohup java -Xms256m -Xmx512m -jar $APP_NAME --spring.profiles.active=$APP_ACTIVE >> $APP_LOG 2>&1 &
}
function stop()
{
    P_ID=`ps -ef|grep $APP_NAME |grep -v grep|awk '{print $2}'`
    kill $P_ID
    P_ID=`ps -ef|grep $APP_NAME |grep -v grep|awk '{print $2}'`
    while [ -n "$P_ID" ]
    do
	echo "$P_ID: 关闭服务中..."    
	sleep 20s
    P_ID=`ps -ef|grep $APP_NAME |grep -v grep|awk '{print $2}'`
    done    
    echo "智采后端服务已关闭"
}

if [[ "$COMMAND" == "start" ]]; then
        start
elif [[ "$COMMAND" == "stop" ]]; then
        stop
else 
	echo "重启服务...."
        stop
        start
fi
