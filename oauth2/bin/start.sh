JAVA_HOME_17=/usr/lib/jvm/java-17-openjdk-amd64
JAVA_HOME_8=/usr/lib/jvm/java-8-openjdk-amd64
JAVA_HOME=$JAVA_HOME_17

set -a
LOG_DIR=/home/ars/logs
KAFKA_HOSTS=localhost:9091,localhost:9093,localhost:9094
GTW_PORT=8081
set +a

$JAVA_HOME_17/bin/java -jar ../config-server/target/config-server-0.1.1-SNAPSHOT-exec.jar &
sleep 5
$JAVA_HOME_17/bin/java -jar ../eureka-server/target/eureka-server-0.1.1-SNAPSHOT-exec.jar &
sleep 5
$JAVA_HOME_8/bin/java -jar ../authorization-server/target/authorization-server-0.1.1-SNAPSHOT-exec.jar &
$JAVA_HOME_8/bin/java -jar ../gateway-zuul/target/gateway-0.1.1-SNAPSHOT-exec.jar &
$JAVA_HOME_8/bin/java -jar ../ui/target/ui-0.1.1-SNAPSHOT-exec.jar &
#$JAVA_HOME_8/bin/java -jar ../ui/target/ui-0.1.1-SNAPSHOT-exec.jar &
$JAVA_HOME_8/bin/java -jar ../resource/target/resource-0.1.1-SNAPSHOT-exec.jar &
#$JAVA_HOME_8/bin/java -jar ../resource/target/resource-0.1.1-SNAPSHOT-exec.jar &
$JAVA_HOME_17/bin/java -jar ../customer-service/target/customer-service-0.1.1-SNAPSHOT-exec.jar &
#$JAVA_HOME_17/bin/java -jar ../customer-service/target/customer-service-0.1.1-SNAPSHOT-exec.jar &
$JAVA_HOME_17/bin/java -jar ../order-service/target/order-service-0.1.1-SNAPSHOT-exec.jar &
#$JAVA_HOME_17/bin/java -jar ../order-service/target/order-service-0.1.1-SNAPSHOT-exec.jar &