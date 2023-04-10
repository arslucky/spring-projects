export JAVA_HOME_17=/usr/lib/jvm/java-17-openjdk-amd64
export JAVA_HOME_8=/usr/lib/jvm/java-8-openjdk-amd64
export JAVA_HOME=$JAVA_HOME_17

function prop {
    grep "${1}=" ./commons/src/main/resources/default.properties|cut -d'=' -f2
}

log_dir=logs
log_host_dir=/mnt/c/$log_dir
log_container_dir=/app/$log_dir
network=oauth2
auth=auth-server
auth_port=$(prop 'auth.port')
eureka=eureka-server
eureka_port=$(prop 'eureka.port')
kafka=kafka
kafka_port=$(prop 'kafka.port')
zoo=zookeeper
zoo_port=$(prop 'zoo.port')
conf=config-server
conf_port=$(prop 'config.server.port')

echo 'conf_port='$conf_port
echo 'log_dir='$log_dir
echo 'log_host_dir='$log_host_dir
echo 'log_container_dir='$log_container_dir
echo 'network='$network
echo 'auth='$auth
echo 'auth_port='$auth_port
echo 'eureka='$eureka
echo 'eureka_port='$eureka_port
echo 'kafka='$kafka
echo 'kafka_port='$kafka_port
echo 'zoo='$zoo
echo 'zoo_port='$zoo_port
echo 'conf='$conf
echo 'conf_port='$conf_port

##########################################################################################

docker network create $network

##########################################################################################
docker rm -f $zoo
docker run -dp $zoo_port:$zoo_port --network $network --network-alias $zoo --hostname $zoo --name $zoo \
    -e ALLOW_ANONYMOUS_LOGIN=yes \
    -e ZOO_PORT_NUMBER=$zoo_port \
    bitnami/zookeeper:3.7.1

docker rm -f $kafka
docker run -dp $kafka_port:$kafka_port --network $network --network-alias $kafka --hostname $kafka --name $kafka \
    -e ALLOW_PLAINTEXT_LISTENER=yes \
    -e KAFKA_CFG_ZOOKEEPER_CONNECT=$zoo:$zoo_port \
    -e KAFKA_CFG_LISTENERS=PLAINTEXT://:$kafka_port \
    -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://:$kafka_port \
    bitnami/kafka:3.3.2

echo "creating log topic"

docker exec -it $kafka /opt/bitnami/kafka/bin/kafka-topics.sh --create --topic log --bootstrap-server $kafka:$kafka_port

##########################################################################################

docker rm -f $conf
docker rmi -f $conf
docker build -t $conf ./config-server
docker run -dp $conf_port:$conf_port --network $network --network-alias $conf --name $conf \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    -e config.server.host=$conf \
    -e config.server.port=$conf_port \
    $conf

log=log-server

docker rm -f $log
docker rmi -f $log
docker build -t $log ./log-server
docker run -d --network $network --network-alias $log --name $log \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    -e config.server.host=$conf \
    -e config.server.port=$conf_port \
    $log

docker rm -f $eureka
docker rmi -f $eureka
docker build -t $eureka ./eureka-server
docker run -dp $eureka_port:$eureka_port --network $network --network-alias $eureka --name $eureka \
    -v $log_host_dir:$log_container_dir \
    -e port=$eureka_port \
    -e log.dir=$log_dir \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    -e config.server.host=$conf \
    -e config.server.port=$conf_port \
    $eureka

docker rm -f $auth
docker rmi -f $auth
docker build -t $auth ./authorization-server
docker run -dp $auth_port:$auth_port --network $network --network-alias $auth --name $auth \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e host=$auth \
    -e port=$auth_port \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    -e zoo.host=$zoo \
    -e zoo.port=$zoo_port \
    -e config.server.host=$conf \
    -e config.server.port=$conf_port \
    $auth

gtw=gateway

docker rm -f $gtw
docker rmi -f $gtw
docker build -t $gtw ./gateway-zuul
docker run -dp 8080:8080 --network $network --network-alias $gtw --name $gtw \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e auth.host=$auth \
    -e auth.port=$auth_port \
    -e eureka.host=$eureka \
    -e eureka.port=$eureka_port \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    -e zoo.host=$zoo \
    -e zoo.port=$zoo_port \
    -e config.server.host=$conf \
    -e config.server.port=$conf_port \
    $gtw

ui1=ui1
ui2=ui2

docker rm -f $ui1
docker rm -f $ui2
docker rmi -f ui
docker build -t ui ./ui
docker run -d --network $network --name $ui1 --network-alias $ui1 \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e auth.host=$auth \
    -e auth.port=$auth_port \
    -e eureka.host=$eureka \
    -e eureka.port=$eureka_port \
    -e eureka.instance.hostname=$ui1 \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    -e zoo.host=$zoo \
    -e zoo.port=$zoo_port \
    -e config.server.host=$conf \
    -e config.server.port=$conf_port \
    ui

docker run -d --network $network --name $ui2 --network-alias $ui2 \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e auth.host=$auth \
    -e auth.port=$auth_port \
    -e eureka.host=$eureka \
    -e eureka.port=$eureka_port \
    -e eureka.instance.hostname=$ui2 \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    -e zoo.host=$zoo \
    -e zoo.port=$zoo_port \
    -e config.server.host=$conf \
    -e config.server.port=$conf_port \
    ui

res1=resource1
res2=resource2

docker rm -f $res1
docker rm -f $res2
docker rmi -f resource
docker build -t resource ./resource
docker run -d --network $network --name $res1 --network-alias $res1 \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e auth.host=$auth \
    -e auth.port=$auth_port \
    -e eureka.host=$eureka \
    -e eureka.port=$eureka_port \
    -e eureka.instance.hostname=$res1 \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    -e zoo.host=$zoo \
    -e zoo.port=$zoo_port \
    -e config.server.host=$conf \
    -e config.server.port=$conf_port \
    resource

docker run -d --network $network --name $res2 --network-alias $res2 \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e auth.host=$auth \
    -e auth.port=$auth_port \
    -e eureka.host=$eureka \
    -e eureka.port=$eureka_port \
    -e eureka.instance.hostname=$res2 \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    -e zoo.host=$zoo \
    -e zoo.port=$zoo_port \
    -e config.server.host=$conf \
    -e config.server.port=$conf_port \
    resource    