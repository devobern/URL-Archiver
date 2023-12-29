#!/bin/bash

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
