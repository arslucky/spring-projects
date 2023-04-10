export JAVA_HOME_17=/usr/lib/jvm/java-17-openjdk-amd64
export JAVA_HOME_8=/usr/lib/jvm/java-8-openjdk-amd64
export JAVA_HOME=$JAVA_HOME_17

###################: Maven command line params for testing #################################################
# group-tests=integration-tests, turn on integration tests without connection to external microservices   ##
# group-tests=ms-integration-tests, turn on microservice integration tests, involving 'integration-tests' ##
# bus.enable=false, switch off Bus/Kafka feature, true by default                                         ## 
############################################################################################################

mvn clean install \
-Dlog.dir=/mnt/c/logs \
-Dkafka.log.level=OFF \
-Dgroup-tests=none \
-Dbus.enable=false