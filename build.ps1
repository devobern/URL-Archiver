# Check for Java installation
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "Java is not installed or not in the PATH. Please install Java 21."
    exit 1
}

# Check for Maven installation
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Host "Maven is not installed or not in the PATH. Please install Maven."
    exit 1
}

# Build the project with Maven
mvn clean package
if ($LASTEXITCODE -eq 0) {
    Write-Host "Build successful."

   # Create 'bin' directory if it doesn't exist
   $binDir = "bin"
   if (-not (Test-Path $binDir)) {
       New-Item -Path $binDir -ItemType Directory
   }

   # Define source and destination paths
   $sourcePath = "target/URL-Archiver-1.0-RELEASE-jar-with-dependencies.jar"
   $destinationPath = Join-Path $binDir (Split-Path -Leaf $sourcePath)

   # Delete the existing file if it exists
   if (Test-Path $destinationPath) {
       Remove-Item -Path $destinationPath
   }

   # Move the JAR file to the 'bin' directory
   Move-Item -Path $sourcePath -Destination $destinationPath
} else {
    Write-Host "Build failed."
}
