@echo off
setlocal

cd /d "%~dp0..\website"

set "NPM_CMD=%ProgramFiles%\nodejs\npm.cmd"
if not exist "%NPM_CMD%" (
  echo [LabLogix] Could not find npm.cmd at: %NPM_CMD%
  exit /b 1
)

if not exist node_modules (
  echo [LabLogix] Installing website dependencies...
  call "%NPM_CMD%" install
  if errorlevel 1 (
    echo [LabLogix] npm install failed.
    exit /b 1
  )
)

echo [LabLogix] Starting website server...
call "%NPM_CMD%" start

endlocal
