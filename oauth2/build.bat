set JAVA_HOME_8=D:\java\jdk1.8.0_45
set JAVA_HOME_17=D:\java\jdk-17.0.6

set JAVA_HOME=%JAVA_HOME_17%

::::::::::::::::::::: Maven command line params for testing :::::::::::::::::::::::::::::::::::::::::::::::::
:: group-tests=integration-tests, turn on integration tests without connection to external microservices   ::
:: group-tests=ms-integration-tests, turn on microservice integration tests, involving 'integration-tests' ::
:: bus.enable=false, switch off Bus/Kafka feature, true by default                                         :: 
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

call mvn --file .\commons\pom.xml --toolchains .\toolchains.xml clean install

call mvn clean package -pl !commons ^
-Dkafka.log.level=OFF ^
-Dgroup-tests=none ^
-Dbus.enable=false