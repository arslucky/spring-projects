export JAVA_HOME_17=/usr/lib/jvm/java-17-openjdk-amd64
export JAVA_HOME_8=/usr/lib/jvm/java-8-openjdk-amd64
export JAVA_HOME=$JAVA_HOME_17
export LOG_FILE=oauth2_test.log

###################: Maven command line params for testing #################################################
# group-tests=integration-tests, turn on integration tests without connection to external microservices   ##
# group-tests=ms-integration-tests, turn on microservice integration tests, involving 'integration-tests' ##
# BUS_ENABLE=false, switch off Bus/Kafka feature, true by default                                         ## 
############################################################################################################

mvn --file ./commons/pom.xml --toolchains ./toolchains.xml clean install \
-Dlog.dir=/mnt/c/logs

mvn clean package -pl !commons \
-DLOG_DIR=/mnt/c/logs \
-DKAFKA_LOG_LEVEL=OFF \
-Dgroup-tests=none \
-DBUS_ENABLE=false