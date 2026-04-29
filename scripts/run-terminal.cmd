@echo off
setlocal

cd /d "%~dp0.."

echo [LabLogix] Building terminal...
if exist out rmdir /s /q out
mkdir out

rem Collect all Java source files under src recursively.
dir /s /b src\*.java > out\sources.txt

javac -d out @out\sources.txt
if errorlevel 1 (
  echo [LabLogix] Build failed.
  exit /b 1
)

echo [LabLogix] Starting terminal...
java -cp "out;lib\mysql-connector-j-9.6.0.jar" src.Main

endlocal
