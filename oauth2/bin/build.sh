LOG_DIR=/var/logs
LOG_FILE=oauth2_test.log

###################: Maven command line params for testing #################################################
# group-tests=integration-tests, turn on integration tests without connection to external microservices   ##
# group-tests=ms-integration-tests, turn on microservice integration tests, involving 'integration-tests' ##
# BUS_ENABLE=false, switch off Bus/Kafka feature, true by default                                         ## 
############################################################################################################
mvn --file ../pom.xml --toolchains ../toolchains.xml clean package \
-DLOG_DIR=$LOG_DIR \
-DLOG_FILE=$LOG_FILE \
-DKAFKA_LOG_LEVEL=OFF \
-Dgroup-tests=none \
-DBUS_ENABLE=false