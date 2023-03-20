mvn clean package -DskipTests

network=oauth2
auth=auth-server
eureka=eureka-server

docker network create $network

docker rm -f $eureka
docker rmi -f $eureka
docker build -t $eureka --force-rm ./eureka-server
docker run -dp 8761:8761 --network $network --network-alias $eureka --name $eureka -e port=8761 $eureka

docker rm -f $auth
docker rmi -f $auth
docker build -t $auth ./authorization-server
docker run -dp 9000:9000 --network $network --network-alias $auth --name $auth -e host=$auth $auth

gtw=gateway

docker rm -f $gtw
docker rmi -f $gtw
docker build -t $gtw ./gateway-zuul
docker run -dp 8080:8080 --network $network --network-alias $gtw --name $gtw \
    -e auth.host=$auth \
    -e eureka.host=$eureka \
    $gtw

docker rm -f ui1
docker rm -f ui2
docker rmi -f ui
docker build -t ui ./ui
docker run -d --network $network --name ui1 --network-alias ui1 \
    -e auth.host=$auth \
    -e eureka.host=$eureka \
    -e eureka.instance.hostname=ui1 \
    ui
docker run -d --network $network --name ui2 --network-alias ui2 \
    -e auth.host=$auth \
    -e eureka.host=$eureka \
    -e eureka.instance.hostname=ui2 \
    ui

docker rm -f resource1
docker rm -f resource2
docker rmi -f resource
docker build -t resource ./resource
docker run -d --network $network --name resource1 --network-alias resource1 \
    -e auth.host=$auth \
    -e eureka.host=$eureka \
    -e eureka.instance.hostname=resource1 \
    resource
docker run -d --network $network --name resource2 --network-alias resource2 \
    -e auth.host=$auth \
    -e eureka.host=$eureka \
    -e eureka.instance.hostname=resource2 \
    resource