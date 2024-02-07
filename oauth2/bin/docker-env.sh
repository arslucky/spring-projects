echo 'setting environment values'
#
function prop {
    grep "${1}=" ../commons/src/main/resources/default.properties|cut -d'=' -f2
}
#
JAVA_HOME_17=/usr/lib/jvm/java-17-openjdk-amd64
JAVA_HOME_8=/usr/lib/jvm/java-8-openjdk-amd64
JAVA_HOME=$JAVA_HOME_17
#
NETWORK=oauth2
#
LOG_DIR=logs
LOG_HOST_DIR=/var/$LOG_DIR
LOG_CONTAINER_DIR=/app/$LOG_DIR
LOG_TOPIC=$(prop 'LOG_TOPIC')
#
LOG_SERVER=log-server
LOG_SERVER_HOST=log-server
#
AUTH=auth-server
AUTH_HOST=auth-server
AUTH_PORT=$(prop 'AUTH_PORT')
#
EUREKA=eureka-server
EUREKA_HOST=eureka-server
EUREKA_PORT=$(prop 'EUREKA_PORT')
#
KAFKA_SETUP=kafka-setup
#
KAFKA1=kafka1
KAFKA_HOST1=$(prop 'KAFKA_HOST1')
KAFKA_PORT1=$(prop 'KAFKA_PORT1')
#
KAFKA2=kafka2
KAFKA_HOST2=$(prop 'KAFKA_HOST2')
KAFKA_PORT2=$(prop 'KAFKA_PORT2')
#
KAFKA3=kafka3
KAFKA_HOST3=$(prop 'KAFKA_HOST3')
KAFKA_PORT3=$(prop 'KAFKA_PORT3')
#
KAFKA_HOSTS=$(prop 'KAFKA_HOSTS')
# dev settings
KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE=true
KAFKA_CFG_OFFSETS_TOPIC_REPLICATION_FACTOR=1
KAFKA_CFG_MIN_INSYNC_REPLICAS=1
KAFKA_CFG_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1
KAFKA_CFG_TRANSACTION_STATE_LOG_MIN_ISR=1
#
ZOO=zoo
ZOO_HOST=zoo
ZOO_PORT=$(prop 'ZOO_PORT')
#
CONFIG_SERVER=config-server
CONFIG_SERVER_HOST=config-server
CONFIG_SERVER_PORT=$(prop 'CONFIG_SERVER_PORT')
#
MYSQL=mysql
MYSQL_SETUP=mysql-setup
MYSQL_HOST=mysql
MYSQL_PORT=$(prop 'MYSQL_PORT')
MYSQL_DB=$(prop 'MYSQL_DB')
MYSQL_ROOT=root
MYSQL_ROOT_PASSWORD=password
MYSQL_USER=$(prop 'MYSQL_USER')
MYSQL_USER_PASSWORD=$(prop 'MYSQL_USER_PASSWORD')
#
MONGO=mongo
MONGO_SETUP=mongo-setup
MONGO_HOST=mongo
MONGO_PORT=$(prop 'MONGO_PORT')
MONGO_DB=$(prop 'MONGO_DB')
MONGO_USER=$(prop 'MONGO_USER')
MONGO_USER_PASSWORD=$(prop 'MONGO_USER_PASSWORD')
MONGO_ROOT=root
MONGO_ROOT_PASSWORD=password
#
GTW=gateway-server
GTW_HOST=gateway-server
GTW_PORT=$(prop 'GTW_PORT')
#
UI=ui
UI_1=ui1
UI_2=ui2
#
RES=resource
RES_1=resource1
RES_2=resource2
#
CUSTOMER=customer-service
CUSTOMER_1=customer-service1
CUSTOMER_2=customer-service2
#
ORDER=order-service
ORDER_1=order-service1
ORDER_2=order-service2