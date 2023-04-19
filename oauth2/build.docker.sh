export JAVA_HOME_17=/usr/lib/jvm/java-17-openjdk-amd64
export JAVA_HOME_8=/usr/lib/jvm/java-8-openjdk-amd64
export JAVA_HOME=$JAVA_HOME_17

function prop {
    grep "${1}=" ./commons/src/main/resources/default.properties|cut -d'=' -f2
}

LOG_DIR=logs
LOG_HOST_DIR=/mnt/c/$LOG_DIR
LOG_CONTAINER_DIR=/app/$LOG_DIR
NETWORK=oauth2
AUTH=auth-server
AUTH_PORT=$(prop 'auth.port')
EUREKA=eureka-server
EUREKA_PORT=$(prop 'eureka.port')
KAFKA=kafka
KAFKA_PORT=$(prop 'kafka.port')
ZOO=zookeeper
ZOO_PORT=$(prop 'zoo.port')
CONF=config-server
CONF_PORT=$(prop 'config.server.port')
MYSQL=mysql
MYSQL_PORT=$(prop 'mysql.port')
MYSQL_USER=root
MYSQL_PASSWORD=password
MYSQL_DB=customer
MONGO=mongo
MONGO_PORT=$(prop 'mongo.port')
MONGO_ROOT_PASSWORD=password

echo 'CONF_PORT='$CONF_PORT
echo 'LOG_DIR='$LOG_DIR
echo 'LOG_HOST_DIR='$LOG_HOST_DIR
echo 'LOG_CONTAINER_DIR='$LOG_CONTAINER_DIR
echo 'NETWORK='$NETWORK
echo 'AUTH='$AUTH
echo 'AUTH_PORT='$AUTH_PORT
echo 'EUREKA='$EUREKA
echo 'EUREKA_PORT='$EUREKA_PORT
echo 'KAFKA='$KAFKA
echo 'KAFKA_PORT='$KAFKA_PORT
echo 'ZOO='$ZOO
echo 'ZOO_PORT='$ZOO_PORT
echo 'CONF='$CONF
echo 'CONF_PORT='$CONF_PORT
echo 'MYSQL='$MYSQL
echo 'MYSQL_PORT='$MYSQL_PORT
echo 'MYSQL_USER='$MYSQL_USER
echo 'MYSQL_PASSWORD='$MYSQL_PASSWORD
echo 'MYSQL_DB='$MYSQL_DB
echo 'MONGO='$MONGO
echo 'MONGO_PORT='$MONGO_PORT
echo 'MONGO_ROOT_PASSWORD='$MONGO_ROOT_PASSWORD

##########################################################################################

docker network create $NETWORK

##########################################################################################
docker rm -f $ZOO
docker run -dp $ZOO_PORT:$ZOO_PORT --network $NETWORK --network-alias $ZOO --hostname $ZOO --name $ZOO \
    -e ALLOW_ANONYMOUS_LOGIN=yes \
    -e ZOO_PORT_NUMBER=$ZOO_PORT \
    bitnami/zookeeper:3.7.1

docker rm -f $KAFKA
docker run -dp $KAFKA_PORT:$KAFKA_PORT --network $NETWORK --network-alias $KAFKA --hostname $KAFKA --name $KAFKA \
    -e ALLOW_PLAINTEXT_LISTENER=yes \
    -e KAFKA_CFG_ZOOKEEPER_CONNECT=$ZOO:$ZOO_PORT \
    -e KAFKA_CFG_LISTENERS=PLAINTEXT://:$KAFKA_PORT \
    -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://:$KAFKA_PORT \
    bitnami/kafka:3.3.2

echo "creating topic log"

docker exec -it $KAFKA /opt/bitnami/kafka/bin/kafka-topics.sh --create --topic log --bootstrap-server $KAFKA:$KAFKA_PORT

##########################################################################################

docker rm -f $MYSQL
docker run -dp $MYSQL_PORT:$MYSQL_PORT --network $NETWORK --network-alias $MYSQL --hostname $MYSQL --name $MYSQL \
    -e MYSQL_TCP_PORT=$MYSQL_PORT \
    -e MYSQL_ROOT_PASSWORD=$MYSQL_PASSWORD \
    mysql:8.0.32

until docker exec -i $MYSQL sh -c 'exec mysql -u'$MYSQL_USER' -p'$MYSQL_PASSWORD' --port='$MYSQL_PORT' -e "select 1 from dual"'
do
  echo "Waiting for database connection..."
  # wait for 5 seconds before check again
  sleep 10
done
    
docker exec -i $MYSQL sh -c 'exec mysql -u'$MYSQL_USER' -p'$MYSQL_PASSWORD' --port='$MYSQL_PORT < ./customer-service/dev-database.sql

##########################################################################################

docker rm -f $MONGO
docker run -dp $MONGO_PORT:$MONGO_PORT --network $NETWORK --network-alias $MONGO --hostname $MONGO --name $MONGO \
    -e MONGODB_ROOT_PASSWORD=$MONGO_ROOT_PASSWORD \
    -e MONGODB_PORT_NUMBER=$MONGO_PORT \
    bitnami/mongodb:6.0.5

docker cp ./order-service/connect-and-insert-dev.js $MONGO:/connect-and-insert-dev.js

until docker exec -it $MONGO mongosh 'mongodb://'$MONGO':'$MONGO_PORT --username root --password $MONGO_ROOT_PASSWORD -f /connect-and-insert-dev.js
do
  echo "Waiting for mongo database connection..."
  # wait for 5 seconds before check again
  sleep 5
done

##########################################################################################

docker rm -f $CONF
docker rmi -f $CONF
docker build -t $CONF ./config-server
docker run -dp $CONF_PORT:$CONF_PORT --network $NETWORK --network-alias $CONF --name $CONF \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    $CONF

LOG=log-server

docker rm -f $LOG
docker rmi -f $LOG
docker build -t $LOG ./log-server
docker run -d --network $NETWORK --network-alias $LOG --name $LOG \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    $LOG

docker rm -f $EUREKA
docker rmi -f $EUREKA
docker build -t $EUREKA ./eureka-server
docker run -dp $EUREKA_PORT:$EUREKA_PORT --network $NETWORK --network-alias $EUREKA --name $EUREKA \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e port=$EUREKA_PORT \
    -e log.dir=$LOG_DIR \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    $EUREKA

docker rm -f $AUTH
docker rmi -f $AUTH
docker build -t $AUTH ./authorization-server
docker run -dp $AUTH_PORT:$AUTH_PORT --network $NETWORK --network-alias $AUTH --name $AUTH \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e host=$AUTH \
    -e port=$AUTH_PORT \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e zoo.host=$ZOO \
    -e zoo.port=$ZOO_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    $AUTH

GTW=gateway

docker rm -f $GTW
docker rmi -f $GTW
docker build -t $GTW ./gateway-zuul
docker run -dp 8080:8080 --network $NETWORK --network-alias $GTW --name $GTW \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e auth.host=$AUTH \
    -e auth.port=$AUTH_PORT \
    -e eureka.host=$EUREKA \
    -e eureka.port=$EUREKA_PORT \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e zoo.host=$ZOO \
    -e zoo.port=$ZOO_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    $GTW

UI1=ui1
UI2=ui2

docker rm -f $UI1
docker rm -f $UI2
docker rmi -f ui
docker build -t ui ./ui
docker run -d --network $NETWORK --name $UI1 --network-alias $UI1 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e auth.host=$AUTH \
    -e auth.port=$AUTH_PORT \
    -e eureka.host=$EUREKA \
    -e eureka.port=$EUREKA_PORT \
    -e eureka.instance.hostname=$UI1 \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e zoo.host=$ZOO \
    -e zoo.port=$ZOO_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    ui

docker run -d --network $NETWORK --name $UI2 --network-alias $UI2 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e auth.host=$AUTH \
    -e auth.port=$AUTH_PORT \
    -e eureka.host=$EUREKA \
    -e eureka.port=$EUREKA_PORT \
    -e eureka.instance.hostname=$UI2 \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e zoo.host=$ZOO \
    -e zoo.port=$ZOO_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    ui

RES1=resource1
RES2=resource2

docker rm -f $RES1
docker rm -f $RES2
docker rmi -f resource
docker build -t resource ./resource
docker run -d --network $NETWORK --name $RES1 --network-alias $RES1 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e auth.host=$AUTH \
    -e auth.port=$AUTH_PORT \
    -e eureka.host=$EUREKA \
    -e eureka.port=$EUREKA_PORT \
    -e eureka.instance.hostname=$RES1 \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e zoo.host=$ZOO \
    -e zoo.port=$ZOO_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    resource

docker run -d --network $NETWORK --name $RES2 --network-alias $RES2 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e auth.host=$AUTH \
    -e auth.port=$AUTH_PORT \
    -e eureka.host=$EUREKA \
    -e eureka.port=$EUREKA_PORT \
    -e eureka.instance.hostname=$RES2 \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e zoo.host=$ZOO \
    -e zoo.port=$ZOO_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    resource

##########################################################################################
CUST1=customer-service1
CUST2=customer-service2

docker rm -f $CUST1
docker rm -f $CUST2
docker rmi -f customer-service
docker build -t customer-service ./customer-service

docker run -d --network $NETWORK --name $CUST1 --network-alias $CUST1 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e eureka.host=$EUREKA \
    -e eureka.port=$EUREKA_PORT \
    -e eureka.instance.hostname=$CUST1 \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e zoo.host=$ZOO \
    -e zoo.port=$ZOO_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    -e mysql.host=$MYSQL \
    -e mysql.port=$MYSQL_PORT \
    -e mysql.db=$MYSQL_DB \
    customer-service

docker run -d --network $NETWORK --name $CUST2 --network-alias $CUST2 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e eureka.host=$EUREKA \
    -e eureka.port=$EUREKA_PORT \
    -e eureka.instance.hostname=$CUST2 \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    -e mysql.host=$MYSQL \
    -e mysql.port=$MYSQL_PORT \
    -e mysql.db=$MYSQL_DB \
    customer-service

##########################################################################################
ORD1=order-service1
ORD2=order-service2

docker rm -f $ORD1
docker rm -f $ORD2
docker rmi -f order-service
docker build -t order-service ./order-service

docker run -d --network $NETWORK --name $ORD1 --network-alias $ORD1 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e eureka.host=$EUREKA \
    -e eureka.port=$EUREKA_PORT \
    -e eureka.instance.hostname=$ORD1 \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    -e mongo.host=$MONGO \
    -e mongo.port=$MONGO_PORT \
    order-service

docker run -d --network $NETWORK --name $ORD2 --network-alias $ORD2 \
    -v $LOG_HOST_DIR:$LOG_CONTAINER_DIR \
    -e log.dir=$LOG_DIR \
    -e eureka.host=$EUREKA \
    -e eureka.port=$EUREKA_PORT \
    -e eureka.instance.hostname=$ORD2 \
    -e kafka.host=$KAFKA \
    -e kafka.port=$KAFKA_PORT \
    -e config.server.host=$CONF \
    -e config.server.port=$CONF_PORT \
    -e mongo.host=$MONGO \
    -e mongo.port=$MONGO_PORT \
    order-service