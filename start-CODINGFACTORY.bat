@echo off
title CodingFactory Platform Launcher
color 0A
cls
echo =======================================================
echo          CodingFactory — Plateforme PFE & Chatbot
echo =======================================================
echo.
echo Veuillez choisir le mode d'execution :
echo.
echo   [1] Backend Unifie (Recommande - Port 8081 + Angular 4200)
echo   [2] Suite Microservices (Eureka 8761 + Gateway 8085 + Microservices 8081/8082)
echo.
set "choice=1"
set /p choice="Entrez votre choix (1 ou 2) [Defaut=1] : "

if "%choice%"=="2" goto microservices

:monolith
echo.
echo ---> Demarrage du Backend Unifie (Spring Boot 8081)...
echo @echo off > "%TEMP%\cf_start_backend.cmd"
echo title CodingFactory Backend [Port 8081] >> "%TEMP%\cf_start_backend.cmd"
echo cd /d "%~dp0backend" >> "%TEMP%\cf_start_backend.cmd"
echo mvn spring-boot:run >> "%TEMP%\cf_start_backend.cmd"
start "CodingFactory Backend" cmd /k "%TEMP%\cf_start_backend.cmd"

echo Attente de 6 secondes pour le demarrage du Backend...
timeout /t 6 /nobreak >nul

echo ---> Configuration du proxy pour le Backend Unifie (Port 8081)...
echo { > "%~dp0frontend\proxy.conf.json"
echo   "/api": { >> "%~dp0frontend\proxy.conf.json"
echo     "target": "http://localhost:8081", >> "%~dp0frontend\proxy.conf.json"
echo     "secure": false, >> "%~dp0frontend\proxy.conf.json"
echo     "changeOrigin": true >> "%~dp0frontend\proxy.conf.json"
echo   } >> "%~dp0frontend\proxy.conf.json"
echo } >> "%~dp0frontend\proxy.conf.json"

echo ---> Demarrage du Frontend Angular (Port 4200)...
echo @echo off > "%TEMP%\cf_start_frontend.cmd"
echo title CodingFactory Frontend [Port 4200] >> "%TEMP%\cf_start_frontend.cmd"
echo cd /d "%~dp0frontend" >> "%TEMP%\cf_start_frontend.cmd"
echo npm start >> "%TEMP%\cf_start_frontend.cmd"
start "CodingFactory Frontend" cmd /k "%TEMP%\cf_start_frontend.cmd"

echo.
echo =======================================================
echo Application lancee avec succes !
echo - Frontend Angular : http://localhost:4200
echo - Backend API REST : http://localhost:8081
echo =======================================================
pause
goto end

:microservices
echo.
echo [1/5] Demarrage de Eureka Server (Port 8761)...
echo @echo off > "%TEMP%\cf_start_eureka.cmd"
echo title Eureka Server [8761] >> "%TEMP%\cf_start_eureka.cmd"
echo cd /d "%~dp0backend\eureka-server" >> "%TEMP%\cf_start_eureka.cmd"
echo mvn spring-boot:run -Dspring-boot.run.profiles=local >> "%TEMP%\cf_start_eureka.cmd"
start "Eureka Server [8761]" cmd /k "%TEMP%\cf_start_eureka.cmd"

echo Attente de 10s pour Eureka...
timeout /t 10 /nobreak >nul

echo [2/5] Demarrage de Gateway Service (Port 8085)...
echo @echo off > "%TEMP%\cf_start_gateway.cmd"
echo title Gateway Service [8085] >> "%TEMP%\cf_start_gateway.cmd"
echo cd /d "%~dp0backend\gateway" >> "%TEMP%\cf_start_gateway.cmd"
echo mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.arguments=--server.port=8085 >> "%TEMP%\cf_start_gateway.cmd"
start "Gateway Service [8085]" cmd /k "%TEMP%\cf_start_gateway.cmd"

echo [3/5] Demarrage de PFE Service (Port 8081)...
echo @echo off > "%TEMP%\cf_start_pfe.cmd"
echo title PFE Service [8081] >> "%TEMP%\cf_start_pfe.cmd"
echo cd /d "%~dp0backend\pfe-service" >> "%TEMP%\cf_start_pfe.cmd"
echo mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.arguments=--server.port=8081 >> "%TEMP%\cf_start_pfe.cmd"
start "PFE Service [8081]" cmd /k "%TEMP%\cf_start_pfe.cmd"

echo [4/5] Demarrage de Chatbot Service (Port 8082)...
echo @echo off > "%TEMP%\cf_start_chatbot.cmd"
echo title Chatbot Service [8082] >> "%TEMP%\cf_start_chatbot.cmd"
echo cd /d "%~dp0backend\chatbot-service" >> "%TEMP%\cf_start_chatbot.cmd"
echo mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.arguments=--server.port=8082 >> "%TEMP%\cf_start_chatbot.cmd"
start "Chatbot Service [8082]" cmd /k "%TEMP%\cf_start_chatbot.cmd"

echo Attente de 5s pour enregistrement...
timeout /t 5 /nobreak >nul

echo [5/5] Demarrage de Angular Frontend (Port 4200)...
echo @echo off > "%TEMP%\cf_start_frontend.cmd"
echo title Angular Frontend [4200] >> "%TEMP%\cf_start_frontend.cmd"
echo cd /d "%~dp0frontend" >> "%TEMP%\cf_start_frontend.cmd"
echo npm start >> "%TEMP%\cf_start_frontend.cmd"
start "Angular Frontend [4200]" cmd /k "%TEMP%\cf_start_frontend.cmd"

echo.
echo =======================================================
echo Tous les microservices sont demandes !
echo - Frontend Angular : http://localhost:4200
echo - Gateway          : http://localhost:8085
echo - Eureka Registry  : http://localhost:8761
echo =======================================================
pause

:end
