#!/bin/bash
# Build and run the Maven project using separate scripts

# Run the build script
./build.sh

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful. Running the application..."
    # Run the application using the run script
    ./run.sh
else
    echo "Build failed. Application will not run."
fi
