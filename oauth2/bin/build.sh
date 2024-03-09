set -a
LOG_DIR=/home/ars/logs
LOG_FILE=oauth2_test.log
GTW_HOST=192.168.1.82
GTW_PORT=8081
set +a
###################: Maven command line params for testing #################################################
# group-tests=integration-tests, turn on integration tests without connection to external microservices   ##
# group-tests=ms-integration-tests, turn on microservice integration tests, involving 'integration-tests' ##
# BUS_ENABLE=false, switch off Bus/Kafka feature, true by default                                         ## 
############################################################################################################
mvn --file ../pom.xml --toolchains ../toolchains.xml clean install -DskipTests=true \
-Dgroup-tests=none \
-DBUS_ENABLE=false