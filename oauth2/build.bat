set JAVA_HOME_8=D:\java\jdk1.8.0_45
set JAVA_HOME_17=D:\java\jdk-17.0.6

set JAVA_HOME=%JAVA_HOME_17%

set log_dir=c:\logs
set auth=localhost
set auth_port=9000
set eureka=localhost
set eureka_port=8761
set kafka=localhost
set kafka_port=9092
set zoo=localhost
set zoo_port=2181
set conf=localhost
set conf_port=8888

::::::::::::::::::::: Maven command line params for testing :::::::::::::::::::::::::::::::::::::::::::::::::
:: group-tests=integration-tests, turn on integration tests without connection to external microservices   ::
:: group-tests=ms-integration-tests, turn on microservice integration tests, involving 'integration-tests' ::
:: bus.enable=false, switch off Bus/Kafka feature, true by default                                         :: 
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

call mvn clean package install ^
    -Dlog.dir=%log_dir% ^
    -Dgroup-tests=none ^
    -Dbus.enable=false ^
    -Dkafka.log.disable=true ^
    -Dauth.host=%auth% ^
    -Dauth.port=%auth_port% ^
    -Deureka.host=%eureka% ^
    -Deureka.port=%eureka_port% ^
    -Dkafka.host=%kafka% ^
    -Dkafka.port=%kafka_port% ^
    -Dzoo.host=%zoo% ^
    -Dzoo.port=%zoo_port% ^
    -Dconfig.server.host=%conf% ^
    -Dconfig.server.port=%conf_port%