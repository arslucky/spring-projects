. ./docker-env.sh

##########################################################################################

docker network create $NETWORK

##########################################################################################
docker rm -f $ZOO
docker run -dp $ZOO_PORT:$ZOO_PORT --network $NETWORK --network-alias $ZOO_HOST --hostname $ZOO_HOST --name $ZOO \
    -e ALLOW_ANONYMOUS_LOGIN=yes \
    -e ZOO_PORT_NUMBER=$ZOO_PORT \
    bitnami/zookeeper:3.7.1

docker rm -f $KAFKA
docker run -dp $KAFKA_PORT:$KAFKA_PORT --network $NETWORK --network-alias $KAFKA_HOST --hostname $KAFKA_HOST --name $KAFKA \
    -e ALLOW_PLAINTEXT_LISTENER=yes \
    -e KAFKA_ENABLE_KRAFT=no \
    -e KAFKA_CFG_ZOOKEEPER_CONNECT=$ZOO_HOST:$ZOO_PORT \
    -e KAFKA_CFG_LISTENERS=PLAINTEXT://:$KAFKA_PORT \
    -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://:$KAFKA_PORT \
    bitnami/kafka:3.3.2

echo "creating topic log"

docker exec -it $KAFKA_HOST /opt/bitnami/kafka/bin/kafka-topics.sh --create --topic $LOG_TOPIC --bootstrap-server $KAFKA_HOST:$KAFKA_PORT

##########################################################################################

docker rm -f $MYSQL
docker run -dp $MYSQL_PORT:$MYSQL_PORT --network $NETWORK --network-alias $MYSQL_HOST --hostname $MYSQL_HOST --name $MYSQL \
    -e MYSQL_TCP_PORT=$MYSQL_PORT \
    -e MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD \
    mysql:8.0.32

docker cp ./customer-service/db $MYSQL:/app

docker exec -it $MYSQL sh -c 'export MYSQL_HOST='$MYSQL_HOST' && export MYSQL_ROOT='$MYSQL_ROOT' && export MYSQL_ROOT_PASSWORD='$MYSQL_ROOT_PASSWORD\
' && export MYSQL_PORT='$MYSQL_PORT' && cd /app && exec /app/initdb.sh'
##########################################################################################

docker rm -f $MONGO
docker run -dp $MONGO_PORT:$MONGO_PORT --network $NETWORK --network-alias $MONGO_HOST --hostname $MONGO_HOST --name $MONGO \
    -e MONGODB_ROOT_PASSWORD=$MONGO_ROOT_PASSWORD \
    -e MONGODB_PORT_NUMBER=$MONGO_PORT \
    bitnami/mongodb:6.0.5

docker cp ./order-service/db $MONGO:/app

docker exec -it $MONGO sh -c 'export MONGO_HOST='$MONGO_HOST' && export MONGO_ROOT='$MONGO_ROOT' && export MONGO_ROOT_PASSWORD='$MONGO_ROOT_PASSWORD\
' && export MONGO_PORT='$MONGO_PORT' && cd /app && /app/initdb.sh'

##########################################################################################

docker rm -f $CONFIG_SERVER
docker rmi -f $CONFIG_SERVER
docker build -t $CONFIG_SERVER ./config-server
docker run -dp $CONFIG_SERVER_PORT:$CONFIG_SERVER_PORT --network $NETWORK --network-alias $CONFIG_SERVER_HOST --hostname $CONFIG_SERVER_HOST --name $CONFIG_SERVER \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    $CONFIG_SERVER

docker rm -f $LOG_SERVER
docker rmi -f $LOG_SERVER
docker build -t $LOG_SERVER ./log-server
docker run -d --network $NETWORK --network-alias $LOG_SERVER_HOST --hostname $LOG_SERVER_HOST --name $LOG_SERVER \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    $LOG_SERVER

docker rm -f $EUREKA
docker rmi -f $EUREKA
docker build -t $EUREKA ./eureka-server
docker run -dp $EUREKA_PORT:$EUREKA_PORT --network $NETWORK --network-alias $EUREKA_HOST --hostname $EUREKA_HOST --name $EUREKA \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e LOG_DIR=$LOG_DIR \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    $EUREKA

docker rm -f $AUTH
docker rmi -f $AUTH
docker build -t $AUTH ./authorization-server
docker run -dp $AUTH_PORT:$AUTH_PORT --network $NETWORK --network-alias $AUTH_HOST --hostname $AUTH_HOST --name $AUTH \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e AUTH_HOST=$AUTH_HOST \
    -e AUTH_PORT=$AUTH_PORT \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e ZOO_HOST=$ZOO_HOST \
    -e ZOO_PORT=$ZOO_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    $AUTH

docker rm -f $GTW
docker rmi -f $GTW
docker build -t $GTW ./gateway-zuul
docker run -dp 8080:8080 --network $NETWORK --network-alias $GTW_HOST --hostname $GTW_HOST --name $GTW \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e AUTH_HOST=$AUTH_HOST \
    -e AUTH_PORT=$AUTH_PORT \
    -e EUREKA_HOST=$EUREKA_HOST \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e ZOO_HOST=$ZOO_HOST \
    -e ZOO_PORT=$ZOO_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    $GTW

docker rm -f $UI_1
docker rm -f $UI_2
docker rmi -f $UI
docker build -t $UI ./ui

docker run -d --network $NETWORK --network-alias $UI_1 --hostname $UI_1 --name $UI_1 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e AUTH_HOST=$AUTH_HOST \
    -e AUTH_PORT=$AUTH_PORT \
    -e EUREKA_HOST=$EUREKA_HOST \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e EUREKA_INSTANCE_HOSTNAME=$UI_1 \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e ZOO_HOST=$ZOO_HOST \
    -e ZOO_PORT=$ZOO_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    $UI

docker run -d --network $NETWORK --network-alias $UI_2 --hostname $UI_2 --name $UI_2 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e AUTH_HOST=$AUTH_HOST \
    -e AUTH_PORT=$AUTH_PORT \
    -e EUREKA_HOST=$EUREKA_HOST \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e EUREKA_INSTANCE_HOSTNAME=$UI_2 \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e ZOO_HOST=$ZOO_HOST \
    -e ZOO_PORT=$ZOO_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    $UI

docker rm -f $RES_1
docker rm -f $RES_2
docker rmi -f $RES
docker build -t $RES ./resource

docker run -d --network $NETWORK --network-alias $RES_1 --hostname $RES_1 --name $RES_1 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e AUTH_HOST=$AUTH_HOST \
    -e AUTH_PORT=$AUTH_PORT \
    -e EUREKA_HOST=$EUREKA_HOST \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e EUREKA_INSTANCE_HOSTNAME=$RES_1 \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e ZOO_HOST=$ZOO_HOST \
    -e ZOO_PORT=$ZOO_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    $RES

docker run -d --network $NETWORK --network-alias $RES_2 --hostname $RES_2 --name $RES_2 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e AUTH_HOST=$AUTH_HOST \
    -e AUTH_PORT=$AUTH_PORT \
    -e EUREKA_HOST=$EUREKA_HOST \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e EUREKA_INSTANCE_HOSTNAME=$RES_2 \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e ZOO_HOST=$ZOO_HOST \
    -e ZOO_PORT=$ZOO_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    $RES

##########################################################################################

docker rm -f $CUSTOMER_1
docker rm -f $CUSTOMER_2
docker rmi -f $CUSTOMER
docker build -t $CUSTOMER ./customer-service

docker run -d --network $NETWORK --network-alias $CUSTOMER_1 --hostname $CUSTOMER_1 --name $CUSTOMER_1 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e EUREKA_HOST=$EUREKA_HOST \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e EUREKA_INSTANCE_HOSTNAME=$CUSTOMER_1 \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e ZOO_HOST=$ZOO_HOST \
    -e ZOO_PORT=$ZOO_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    -e MYSQL_HOST=$MYSQL_HOST \
    -e MYSQL_PORT=$MYSQL_PORT \
    -e MYSQL_DB=$MYSQL_DB \
    $CUSTOMER

docker run -d --network $NETWORK --network-alias $CUSTOMER_2 --hostname $CUSTOMER_2 --name $CUSTOMER_2 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e EUREKA_HOST=$EUREKA_HOST \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e EUREKA_INSTANCE_HOSTNAME=$CUSTOMER_2 \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e ZOO_HOST=$ZOO_HOST \
    -e ZOO_PORT=$ZOO_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    -e MYSQL_HOST=$MYSQL_HOST \
    -e MYSQL_PORT=$MYSQL_PORT \
    -e MYSQL_DB=$MYSQL_DB \
    $CUSTOMER

##########################################################################################

docker rm -f $ORDER_1
docker rm -f $ORDER_2
docker rmi -f $ORDER
docker build -t $ORDER ./order-service

docker run -d --network $NETWORK --network-alias $ORDER_1 --hostname $ORDER_1 --name $ORDER_1 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e EUREKA_HOST=$EUREKA_HOST \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e EUREKA_INSTANCE_HOSTNAME=$ORDER_1 \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    -e MONGO_HOST=$MONGO_HOST \
    -e MONGO_PORT=$MONGO_PORT \
    $ORDER

docker run -d --network $NETWORK --network-alias $ORDER_2 --hostname $ORDER_2 --name $ORDER_2 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e LOG_DIR=$LOG_DIR \
    -e EUREKA_HOST=$EUREKA_HOST \
    -e EUREKA_PORT=$EUREKA_PORT \
    -e EUREKA_INSTANCE_HOSTNAME=$ORDER_2 \
    -e KAFKA_HOST=$KAFKA_HOST \
    -e KAFKA_PORT=$KAFKA_PORT \
    -e CONFIG_SERVER_HOST=$CONFIG_SERVER_HOST \
    -e CONFIG_SERVER_PORT=$CONFIG_SERVER_PORT \
    -e MONGO_HOST=$MONGO_HOST \
    -e MONGO_PORT=$MONGO_PORT \
    $ORDER