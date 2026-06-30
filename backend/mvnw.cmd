@echo off
setlocal
set "MAVEN_PROJECTBASEDIR=%~dp0"
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "MAVEN_OPTS=-Xmx512m"
java -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR:~0,-1%" %MAVEN_OPTS% -classpath "%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
endlocal
