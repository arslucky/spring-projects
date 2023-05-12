set JAVA_HOME_8=D:\java\jdk1.8.0_45
set JAVA_HOME_17=D:\java\jdk-17.0.6
set JAVA_HOME=%JAVA_HOME_17%
set LOG_FILE=oauth2_test.log

::::::::::::::::::::: Maven command line params for testing :::::::::::::::::::::::::::::::::::::::::::::::::
:: group-tests=integration-tests, turn on integration tests without connection to external microservices   ::
:: group-tests=ms-integration-tests, turn on microservice integration tests, involving 'integration-tests' ::
:: BUS_ENABLE=false, switch off Bus/Kafka feature, true by default                                         :: 
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
call mvn --file ..\commons\pom.xml --toolchains ..\toolchains.xml clean install

call mvn --file ..\pom.xml --toolchains ..\toolchains.xml clean package -pl !commons ^
-DKAFKA_LOG_LEVEL=OFF ^
-Dgroup-tests=none ^
-DBUS_ENABLE=false