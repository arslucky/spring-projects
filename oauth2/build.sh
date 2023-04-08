export JAVA_HOME_17=/usr/lib/jvm/java-17-openjdk-amd64
export JAVA_HOME_8=/usr/lib/jvm/java-8-openjdk-amd64
export JAVA_HOME=$JAVA_HOME_17

log_dir=logs
log_host_dir=/mnt/c/$log_dir
auth=localhost
auth_port=9000
eureka=localhost
eureka_port=8762
kafka=localhost
kafka_port=9093
zoo=localhost
zoo_port=2182
conf=localhost
conf_port=8889

###################: Maven command line params for testing #################################################
# group-tests=integration-tests, turn on integration tests without connection to external microservices   ##
# group-tests=ms-integration-tests, turn on microservice integration tests, involving 'integration-tests' ##
# bus.enable=false, switch off Bus/Kafka feature, true by default                                         ## 
############################################################################################################

mvn clean install \
    -D log.dir=$log_host_dir \
    -D group-tests=none \
    -D bus.enable=false \
    -D kafka.log.disable=true \
    -D auth.host=$auth \
    -D auth.port=$auth_port \
    -D eureka.host=$eureka \
    -D eureka.port=$eureka_port \
    -D kafka.host=$kafka \
    -D kafka.port=$kafka_port \
    -D zoo.host=$zoo \
    -D zoo.port=$zoo_port \
    -D config.server.host=$conf \
    -D config.server.port=$conf_port