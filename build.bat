@echo off

REM Check for Java installation
where java >nul 2>nul
IF %ERRORLEVEL% NEQ 0 (
    echo Java is not installed or not in the PATH. Please install Java 21.
    exit /b 1
)

REM Check for Maven installation
where mvn >nul 2>nul
IF %ERRORLEVEL% NEQ 0 (
    echo Maven is not installed or not in the PATH. Please install Maven.
    exit /b 1
)

REM Build the project with Maven
mvn clean package

IF %ERRORLEVEL% EQU 0 (
    echo Build successful.

    REM Create 'bin' directory if it doesn't exist
    IF NOT EXIST bin mkdir bin

    REM Move the JAR file to the 'bin' directory
    move target\URL-Archiver-1.0-RELEASE-jar-with-dependencies.jar bin\
) ELSE (
    echo Build failed.
)
