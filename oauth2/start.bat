set JAVA_HOME_8=D:\java\jdk1.8.0_45
set JAVA_HOME_17=D:\java\jdk-17.0.6

set JAVA_HOME=%JAVA_HOME_17%
set log_dir=c:\logs

start "config-server" cmd /k "%JAVA_HOME_17%\bin\java.exe -jar .\config-server\target\config-server-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
timeout 5
start "log-server" cmd /k "%JAVA_HOME_17%\bin\java.exe -jar .\log-server\target\log-server-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "eureka-server" cmd /k "%JAVA_HOME_17%\bin\java.exe -jar .\eureka-server\target\eureka-server-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
timeout 5
start "auth-server" cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\authorization-server\target\authorization-server-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "gateway"     cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\gateway-zuul\target\gateway-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "ui-1"        cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\ui\target\ui-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "ui-2"        cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\ui\target\ui-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "resource-1"  cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\resource\target\resource-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "resource-2"  cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\resource\target\resource-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "customer-service-1"  cmd /k "%JAVA_HOME_17%\bin\java.exe -jar .\customer-service\target\customer-service-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "customer-service-2"  cmd /k "%JAVA_HOME_17%\bin\java.exe -jar .\customer-service\target\customer-service-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "order-service-1"  cmd /k "%JAVA_HOME_17%\bin\java.exe -jar .\order-service\target\order-service-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"
start "order-service-2"  cmd /k "%JAVA_HOME_17%\bin\java.exe -jar .\order-service\target\order-service-0.0.1-SNAPSHOT.jar --log.dir=%log_dir%"