mvn clean package -DskipTests

log_dir=logs
log_host_dir=/mnt/c/$log_dir
log_container_dir=/app/$log_dir
network=oauth2
auth=auth-server
eureka=eureka-server
kafka=kafka
kafka_port=9092

docker network create $network
##########################################################################################
zoo=zookeeper
zoo_port=2181

docker rm -f $zoo
docker run -dp $zoo_port:$zoo_port --network $network --network-alias $zoo --name $zoo \
    -e ALLOW_ANONYMOUS_LOGIN=yes \
    bitnami/zookeeper:3.7.1

docker rm -f $kafka
docker run -dp $kafka_port:$kafka_port --network $network --network-alias $kafka --name $kafka \
    -e ALLOW_PLAINTEXT_LISTENER=yes \
    -e KAFKA_CFG_ZOOKEEPER_CONNECT=$zoo:$zoo_port \
    bitnami/kafka:3.3.2

docker exec -it $kafka /opt/bitnami/kafka/bin/kafka-topics.sh --create --topic log --bootstrap-server $kafka:$kafka_port

##########################################################################################

log=log-server

docker rm -f $log
docker rmi -f $log
docker build -t $log ./log-server
docker run -d --network $network --network-alias $log --name $log \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    $log

docker rm -f $eureka
docker rmi -f $eureka
docker build -t $eureka ./eureka-server
docker run -dp 8761:8761 --network $network --network-alias $eureka --name $eureka \
    -v $log_host_dir:$log_container_dir \
    -e port=8761 \
    -e log.dir=$log_dir \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    $eureka

docker rm -f $auth
docker rmi -f $auth
docker build -t $auth ./authorization-server
docker run -dp 9000:9000 --network $network --network-alias $auth --name $auth \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e host=$auth \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    $auth

gtw=gateway

docker rm -f $gtw
docker rmi -f $gtw
docker build -t $gtw ./gateway-zuul
docker run -dp 8080:8080 --network $network --network-alias $gtw --name $gtw \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e auth.host=$auth \
    -e eureka.host=$eureka \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
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
    -e eureka.host=$eureka \
    -e eureka.instance.hostname=$ui1 \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    ui
docker run -d --network $network --name $ui2 --network-alias $ui2 \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e auth.host=$auth \
    -e eureka.host=$eureka \
    -e eureka.instance.hostname=$ui2 \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
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
    -e eureka.host=$eureka \
    -e eureka.instance.hostname=$res1 \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    resource
docker run -d --network $network --name $res2 --network-alias $res2 \
    -v $log_host_dir:$log_container_dir \
    -e log.dir=$log_dir \
    -e auth.host=$auth \
    -e eureka.host=$eureka \
    -e eureka.instance.hostname=$res2 \
    -e kafka.host=$kafka \
    -e kafka.port=$kafka_port \
    resource