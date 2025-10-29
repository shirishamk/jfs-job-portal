Job-Portal - Build & Deploy (Windows)

This repository contains a batch script to build the project with Maven and deploy the generated WAR to a local Tomcat installation.

Files added:
- `build_and_deploy.bat` - builds the project, stops any process using port 8080 (if Java), copies the WAR to Tomcat `webapps`, starts Tomcat, waits for the server, and prints logs.

Quick start (Windows cmd.exe):
1) Open an elevated cmd (Run as Administrator) if Tomcat is installed under `Program Files`.
2) Edit `build_and_deploy.bat` and set `TOMCAT_HOME` if your Tomcat path differs (default: `C:\Program Files\Apache Software Foundation\Tomcat 11.0`).
3) Run:

```
cd /d C:\Users\mkshi\Downloads\Job-Portal-main\Job-Portal-main
build_and_deploy.bat
```

What the script does:
- Runs `mvn -B clean package -DskipTests` to build a WAR at `target/Job-Portal-0.0.1-SNAPSHOT.war`.
- Attempts to stop any `java.exe` process that is listening on port 8080 (useful if Tomcat or another app holds the port).
- Copies the WAR to `%TOMCAT_HOME%\webapps`.
- Starts Tomcat using `%TOMCAT_HOME%\bin\startup.bat`.
- Waits up to 30 seconds for port 8080 to become active and prints tail of catalina logs for diagnosis.

Notes and troubleshooting:
- If the script cannot copy into Tomcat `webapps`, re-run it from an elevated cmd (Administrator) or manually copy the WAR.
- If Tomcat was installed as a Windows Service, you can stop/start the service instead of killing processes. The script currently kills java processes bound to 8080; this is deliberate to avoid relying on the service name, but review the output before confirming kills.
- If your Tomcat listens on a different port, edit the script and replace `:8080` with the correct port.
- The project `pom.xml` has been updated to package the webapp as a WAR and include compiler/war plugins. If you prefer a different Java version, update `maven.compiler.source` and `maven.compiler.target` in `pom.xml`.

If you want, I can:
- Create a PowerShell version of the script with richer logging.
- Configure the project to deploy to the root context (rename WAR to `ROOT.war`) automatically.
- Add a `mvn tomcat7:deploy` configuration if you prefer Tomcat manager deployment.

