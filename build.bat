@echo off
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
