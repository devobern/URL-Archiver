@echo off

REM Check for Java installation
where java >nul 2>nul
IF %ERRORLEVEL% NEQ 0 (
    echo Java is not installed or not in the PATH. Please install Java 21 and open a new command prompt.
    exit /b 1
)

REM Check for Maven installation
where mvn >nul 2>nul
IF %ERRORLEVEL% NEQ 0 (
    echo Maven is not installed or not in the PATH. Please install Maven and open a new command prompt.
    exit /b 1
)

REM Build the project with Maven
mvn clean package

echo After Maven Command
echo Error Level: %ERRORLEVEL%

IF %ERRORLEVEL% EQU 0 (
    echo Build successful.

    REM Create 'bin' directory if it doesn't exist
    IF NOT EXIST bin (
        mkdir bin
        IF %ERRORLEVEL% NEQ 0 (
            echo Failed to create bin directory.
            exit /b 1
        )
    )

    REM Move the JAR file to the 'bin' directory
    move target\URL-Archiver-1.0-RELEASE-jar-with-dependencies.jar bin\
    IF %ERRORLEVEL% NEQ 0 (
        echo Failed to move JAR file.
        exit /b 1
    )
) ELSE (
    echo Build failed.
)
