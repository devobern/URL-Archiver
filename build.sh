#!/bin/bash

# Check for Java installation
if ! type java > /dev/null 2>&1; then
    echo "Java is not installed or not in the PATH. Please install Java 21."
    exit 1
fi

# Check for Maven installation
if ! type mvn > /dev/null 2>&1; then
    echo "Maven is not installed or not in the PATH. Please install Maven."
    exit 1
fi

# Build the project with Maven
mvn clean package

# Check for build success
if [ $? -eq 0 ]; then
    echo "Build successful."

    # Create 'bin' directory if it doesn't exist
    mkdir -p bin

    # Move the JAR file to the 'bin' directory
    mv target/URL-Archiver-1.0-RELEASE-jar-with-dependencies.jar bin/
else
    echo "Build failed."
fi
