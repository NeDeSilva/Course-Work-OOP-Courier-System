@echo off
REM Compile the Courier Management System.
REM Uses the JDK in JAVA_HOME, or the javac on PATH if JAVA_HOME is not set.
setlocal

set CP=libs\sqlite-jdbc-3.46.1.0.jar;libs\slf4j-api-2.0.16.jar;libs\slf4j-simple-2.0.16.jar

if not exist out mkdir out

if defined JAVA_HOME (
  set JAVAC=%JAVA_HOME%\bin\javac.exe
) else (
  set JAVAC=javac
)

echo Compiling source files...
"%JAVAC%" -encoding UTF-8 -cp "%CP%" -d out *.java
if errorlevel 1 (
  echo Build FAILED.
  exit /b 1
)
echo Build OK.
