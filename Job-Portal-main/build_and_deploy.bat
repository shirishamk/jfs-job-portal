@echo off
rem Build and deploy script for Job-Portal (Windows cmd)
rem Edit TOMCAT_HOME below if Tomcat is installed in a different location.

setlocal enabledelayedexpansion

set "PROJECT_DIR=%~dp0"
set "TOMCAT_HOME=C:\Program Files\Apache Software Foundation\Tomcat 11.0"

echo Project dir: %PROJECT_DIR%
echo Tomcat home: %TOMCAT_HOME%

if not exist "%TOMCAT_HOME%\bin\startup.bat" (
  echo Tomcat not found at %TOMCAT_HOME%
  echo Please edit build_and_deploy.bat and set TOMCAT_HOME to your Tomcat installation path.
  pause
  exit /b 1
)

rem Build the project (non-interactive)
cd /d "%PROJECT_DIR%"
echo Running: mvn -B clean package -DskipTests
mvn -B clean package -DskipTests
if errorlevel 1 (
  echo Maven build failed. See output above.
  pause
  exit /b 1
)

set "WAR=%PROJECT_DIR%target\Job-Portal-0.0.1-SNAPSHOT.war"
if not exist "%WAR%" (
  echo WAR not found at %WAR%
  dir "%PROJECT_DIR%target"
  pause
  exit /b 1
)

rem Try to stop any process listening on 8080 (safe: only if it's the app port)
echo Looking for processes listening on port 8080...
for /f "tokens=5" %%p in ('netstat -ano ^| findstr ":8080"') do (
  echo Found PID %%p - attempting to stop
  tasklist /FI "PID eq %%p" | findstr /I "java.exe" >nul && (
    echo Killing java PID %%p
    taskkill /PID %%p /F
  ) || (
    echo PID %%p is not a java process, skipping
  )
)

rem Copy WAR to Tomcat webapps
echo Copying %WAR% to %TOMCAT_HOME%\webapps\
copy /Y "%WAR%" "%TOMCAT_HOME%\webapps\"
if errorlevel 1 (
  echo Copy failed. You may need to run this script as Administrator.
  pause
  exit /b 1
)

rem Start Tomcat
echo Starting Tomcat...
call "%TOMCAT_HOME%\bin\startup.bat"

rem Wait for port 8080 to appear (30s timeout)
set /a count=0
set "PID="
:waitloop
  timeout /t 1 >nul
  set /a count+=1
  for /f "tokens=5" %%q in ('netstat -ano ^| findstr ":8080"') do set "PID=%%q"
  if defined PID (
    echo Tomcat is listening (PID=%PID%)
    goto done
  )
  if %count% geq 30 (
    echo Timeout waiting for port 8080
    goto logs
  )
goto waitloop

:logs
echo Showing last 80 lines of catalina log for diagnosis:
powershell -Command "Get-Content -Path '%TOMCAT_HOME%\\logs\\catalina.*.log' -Tail 80"
pause
goto end

:done
echo Deployment finished. App should be available at http://localhost:8080/Job-Portal-0.0.1-SNAPSHOT/
echo Showing last 40 lines of catalina log:
powershell -Command "Get-Content -Path '%TOMCAT_HOME%\\logs\\catalina.*.log' -Tail 40"
pause

:end
endlocal
exit /b 0
