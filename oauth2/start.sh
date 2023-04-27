export JAVA_HOME_17=/usr/lib/jvm/java-17-openjdk-amd64
export JAVA_HOME_8=/usr/lib/jvm/java-8-openjdk-amd64
export JAVA_HOME=$JAVA_HOME_17
export LOG_DIR=/mnt/c/logs

$JAVA_HOME_17/bin/java -jar ./config-server/target/config-server-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
sleep 5
$JAVA_HOME_17/bin/java -jar ./eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
sleep 5
$JAVA_HOME_8/bin/java -jar ./authorization-server/target/authorization-server-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
$JAVA_HOME_8/bin/java -jar ./gateway-zuul/target/gateway-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
$JAVA_HOME_8/bin/java -jar ./ui/target/ui-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
$JAVA_HOME_8/bin/java -jar ./ui/target/ui-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
$JAVA_HOME_8/bin/java -jar ./resource/target/resource-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
$JAVA_HOME_8/bin/java -jar ./resource/target/resource-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
$JAVA_HOME_17/bin/java -jar ./customer-service/target/customer-service-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
$JAVA_HOME_17/bin/java -jar ./customer-service/target/customer-service-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
$JAVA_HOME_17/bin/java -jar ./order-service/target/order-service-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &
$JAVA_HOME_17/bin/java -jar ./order-service/target/order-service-0.0.1-SNAPSHOT.jar -DLOG_DIR=$LOG_DIR &