@echo off
REM Run the Courier Management System (Swing GUI).
setlocal

set CP=out;libs\sqlite-jdbc-3.46.1.0.jar;libs\slf4j-api-2.0.16.jar;libs\slf4j-simple-2.0.16.jar

if defined JAVA_HOME (
  set JAVA=%JAVA_HOME%\bin\java.exe
) else (
  set JAVA=java
)

if not exist out\App.class (
  echo Classes not found. Run build.bat first.
  exit /b 1
)

"%JAVA%" -cp "%CP%" App
