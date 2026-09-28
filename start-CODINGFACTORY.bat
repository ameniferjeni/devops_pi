@echo off
setlocal

cd /d "%~dp0backend"
start "Backend-CodingFactory" cmd /k "mvn spring-boot:run"

cd /d "%~dp0frontend"
start "Frontend-CodingFactory" cmd /k "npm start"

echo.
echo CodingFactory project started.
echo Backend: http://localhost:8081
echo Frontend: http://localhost:4200
echo.
exit /b 0
