set JAVA_HOME_8=D:\java\jdk1.8.0_45
set JAVA_HOME_18=D:\java\jdk-18.0.2

set JAVA_HOME=%JAVA_HOME_18%
call mvn clean package

start "eureka-server" cmd /k "%JAVA_HOME_18%\bin\java.exe -jar .\eureka-server\target\eureka-server-0.0.1-SNAPSHOT.jar"
timeout 3
start "auth-server" cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\authorization-server\target\authorization-server-0.0.1-SNAPSHOT.jar"
start "gateway"     cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\gateway-zuul\target\gateway-0.0.1-SNAPSHOT.jar"
start "ui-1"        cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\ui\target\ui-0.0.1-SNAPSHOT.jar"
start "ui-2"        cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\ui\target\ui-0.0.1-SNAPSHOT.jar"
start "resource-1"  cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\resource\target\resource-0.0.1-SNAPSHOT.jar"
start "resource-2"  cmd /k "%JAVA_HOME_8%\bin\java.exe -jar .\resource\target\resource-0.0.1-SNAPSHOT.jar"




