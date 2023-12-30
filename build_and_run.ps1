# Run the build script
& "./build.ps1"
if ($LASTEXITCODE -eq 0) {
    Write-Host "Build successful. Running the application..."
    # Run the application using the run script
    & "./run.ps1"
} else {
    Write-Host "Build failed. Application will not run."
}
