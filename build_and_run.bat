@echo off
REM Build and run the Maven project using separate scripts

REM Run the build script
call build.bat

IF %ERRORLEVEL% EQU 0 (
    echo Build successful. Running the application...
    REM Run the application using the run script
    call run.bat
) ELSE (
    echo Build failed. Application will not run.
)
