@echo off
setlocal
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.10"
set "PATH=%JAVA_HOME%\bin;%PATH%"
call "%~dp0gradlew.bat" --no-daemon runClient
endlocal
