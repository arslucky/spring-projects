echo 'setting environment values'
#
function prop {
    grep "${1}=" ../commons/src/main/resources/default.properties|cut -d'=' -f2
}
#
export JAVA_HOME_17=/usr/lib/jvm/java-17-openjdk-amd64
export JAVA_HOME_8=/usr/lib/jvm/java-8-openjdk-amd64
export JAVA_HOME=$JAVA_HOME_17
#
export NETWORK=oauth2
#
export LOG_DIR=logs
export LOG_HOST_DIR=/mnt/c/$LOG_DIR
export LOG_CONTAINER_DIR=/app/$LOG_DIR
export LOG_TOPIC=$(prop 'LOG_TOPIC')
#
export LOG_SERVER=log-server
export LOG_SERVER_HOST=log-server
#
export AUTH=auth-server
export AUTH_HOST=auth-server
export AUTH_PORT=$(prop 'AUTH_PORT')
#
export EUREKA=eureka-server
export EUREKA_HOST=eureka-server
export EUREKA_PORT=$(prop 'EUREKA_PORT')
#
export KAFKA_SETUP=kafka-setup
#
export KAFKA1=kafka1
export KAFKA_HOST1=$(prop 'KAFKA_HOST1')
export KAFKA_PORT1=$(prop 'KAFKA_PORT1')
#
export KAFKA2=kafka2
export KAFKA_HOST2=$(prop 'KAFKA_HOST2')
export KAFKA_PORT2=$(prop 'KAFKA_PORT2')
#
export KAFKA3=kafka3
export KAFKA_HOST3=$(prop 'KAFKA_HOST3')
export KAFKA_PORT3=$(prop 'KAFKA_PORT3')
#
export KAFKA_HOSTS=$(prop 'KAFKA_HOSTS')
# dev settings
export KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE=true
export KAFKA_CFG_OFFSETS_TOPIC_REPLICATION_FACTOR=1
export KAFKA_CFG_MIN_INSYNC_REPLICAS=1
export KAFKA_CFG_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1
export KAFKA_CFG_TRANSACTION_STATE_LOG_MIN_ISR=1
#
export ZOO=zoo
export ZOO_HOST=zoo
export ZOO_PORT=$(prop 'ZOO_PORT')
#
export CONFIG_SERVER=config-server
export CONFIG_SERVER_HOST=config-server
export CONFIG_SERVER_PORT=$(prop 'CONFIG_SERVER_PORT')
#
export MYSQL=mysql
export MYSQL_SETUP=mysql-setup
export MYSQL_HOST=mysql
export MYSQL_PORT=$(prop 'MYSQL_PORT')
export MYSQL_DB=$(prop 'MYSQL_DB')
export MYSQL_ROOT=root
export MYSQL_ROOT_PASSWORD=password
export MYSQL_USER=$(prop 'MYSQL_USER')
export MYSQL_USER_PASSWORD=$(prop 'MYSQL_USER_PASSWORD')
#
export MONGO=mongo
export MONGO_SETUP=mongo-setup
export MONGO_HOST=mongo
export MONGO_PORT=$(prop 'MONGO_PORT')
export MONGO_DB=$(prop 'MONGO_DB')
export MONGO_USER=$(prop 'MONGO_USER')
export MONGO_USER_PASSWORD=$(prop 'MONGO_USER_PASSWORD')
export MONGO_ROOT=root
export MONGO_ROOT_PASSWORD=password
#
export GTW=gateway-server
export GTW_HOST=gateway-server
export GTW_PORT=$(prop 'GTW_PORT')
#
export UI=ui
export UI_1=ui1
export UI_2=ui2
#
export RES=resource
export RES_1=resource1
export RES_2=resource2
#
export CUSTOMER=customer-service
export CUSTOMER_1=customer-service1
export CUSTOMER_2=customer-service2
#
export ORDER=order-service
export ORDER_1=order-service1
export ORDER_2=order-service2