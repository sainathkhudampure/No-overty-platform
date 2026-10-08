@echo off
title NO POVERTY Welfare Platform Launcher
color 0A
echo ====================================================================
echo  🇮🇳 NO POVERTY - Welfare Platform (UN SDG 1)
echo ====================================================================
echo.

netstat -ano | findstr :8085 >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [INFO] Platform is ALREADY running on http://localhost:8085/
) else (
    echo [INFO] Starting Spring Boot Server on port 8085...
    start "NO POVERTY Backend" /min java -jar backend\target\platform-1.0.0.jar
    echo [INFO] Waiting 6 seconds for server to initialize...
    timeout /t 6 /nobreak >nul
)

echo.
echo [SUCCESS] Opening browser at http://localhost:8085/
start http://localhost:8085/

echo.
echo ====================================================================
echo Platform is active!
echo Web Portal:  http://localhost:8085/
echo Health Check: http://localhost:8085/api/health
echo H2 Console:  http://localhost:8085/h2-console
echo.
echo Demo Citizen Login: citizen@example.com / password123
echo Demo Admin Login:   admin@nopoverty.gov.in / admin123
echo ====================================================================
pause

