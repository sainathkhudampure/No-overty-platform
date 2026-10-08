@echo off
title Stop NO POVERTY Welfare Platform
color 0C
echo ====================================================================
echo  Stopping NO POVERTY Welfare Platform (Port 8085)
echo ====================================================================
echo.

for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8085') do (
    echo [INFO] Terminating PID %%a listening on port 8085...
    taskkill /f /pid %%a >nul 2>&1
)

echo [SUCCESS] Server stopped. Port 8085 is now free.
pause

