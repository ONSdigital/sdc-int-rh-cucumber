#!/bin/bash
#
# This script checks to see if the RH Cucumber prerequisites, needed for
# local execution are running.
#
# If any services are not responding then an error message is printed and 
# the script exits with a non zero code.
#

set -e 

mock_envoy_port="8181" 
mock_ai_port="8162"
pubsub_emulator_port="9808"
redis_port="6379"
rh_service_port="8071"
rhui_port="9092"

error_found=0

RED='\033[0;31m'
NO_COLOUR='\033[0m'


function verify_service_running {
  service_name=$1
  url=$2
  expected_status=$3

  status=`curl -s -o /dev/null -w '%{http_code}' localhost:9808`
  
  if [ $status != $expected_status ]
  then 
    echo "$service_name: NOT RUNNING  Response=$status  Expected:$expected_status"
    error_found=1
  else 
    echo "$service_name: ok"
  fi
}


function verify_redis_running {
  redis_host=$1
  redis_port=$2

  result=$(echo PING | nc $redis_host $redis_port | tr -d '\r\n')
  
  if [ $result != "+PONG" ]
  then 
    echo "Redis: NOT RUNNING  Response=$result  Expected:+PONG"
    error_found=1
  else 
    echo "Redis          : ok"
  fi
}


# Check to see that the required services appear to be running
verify_service_running "Mock Envoy     " "http://localhost:$mock_envoy_port/info" "200" 
verify_service_running "Mock AI        " "http://localhost:$mock_ai_port/info" "200" 
verify_service_running "PubSub emulator" "http://localhost:$pubsub_emulator_port" "200"
verify_redis_running "localhost" "$redis_port"
verify_service_running "RH Service     " "http://localhost:$rh_service_port/info" "200"
verify_service_running "RH UI          " "http://localhost:$rhui_port/info" "200"


# Complain if any services not running
if [ $error_found != 0 ]
then
  echo -e "${RED}ERROR: At least one service is not running${NO_COLOUR}"
  exit 1
fi

#EOF
