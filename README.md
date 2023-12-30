# URL-Archiver
The URL archiver enables the extraction of URLs from any Unicode text or PDF file and allows for interactive archiving on one of the supported archiving services.
> ⚠️ The application was designed to be platform-independent. However, it has only been tested on the following systems, so it cannot be guaranteed to work without restrictions on other platforms.
> - Windows 11 (Version 23H2)
> - Windows 10 (Version 22H2)
> - macOS (Sonoma)
> - Ubuntu (20.04.3 LTS)

## Authors
- [Mr. Nicolin Dora](mailto:nicolin.dora@students.bfh.ch)
- [Mr. Abidin Vejseli](mailto:abidin.vejseli@students.bfh.ch)
- [Mr. Kilian Wampfler](mailto:kilian.wampfler@students.bfh.ch)

## Supervisor
- [Mr. Frank Helbling](mailto:frank.helbling@bfh.ch) (Project Management)
- [Dr. Simon Kramer](mailto:simon.kramer@bfh.ch) (Technical)
## Installation
### Requirements
- Git: Latest stable version recommended.
- Maven: Version 3.8 or higher.
- Java: Version 21.
### Clone the repository
To clone the repository, run the following command in a terminal:
```bash
git clone https://github.com/devobern/URL-Archiver.git
```
### Build and run scripts
The build and run scripts are provided for Windows (`build.ps1`, `run.ps1`, `build_and_run.ps1`), Linux and MacOS (`build.sh`, `run.sh`, `build_and_run.sh`). The scripts are located in the root directory of the project.
> ⚠️ The scripts need to be executable. To make them executable, run the following command in a terminal: 
> - Linux / MacOS: `chmod +x build.sh run.sh build_and_run.sh` 
> - Windows: 
>   - Open PowerShell as an Administrator. 
>   - Check the current execution policy by running: Get-ExecutionPolicy. 
>   - If the policy is Restricted, change it to RemoteSigned to allow local scripts to run. Execute: Set-ExecutionPolicy RemoteSigned. 
>   - Confirm the change when prompted.
>   - This change allows you to run PowerShell scripts that are written on your local machine. **Be sure to only run scripts from trusted sources.**

### Windows
#### Build the application
To build the application, open a command prompt and run the following script:
```powershell
./build.ps1
```
#### Run the application
To run the application, open a command prompt and run the following script:
```powershell
./run.ps1
```
#### Build and run the application
To build and run the application, open a command prompt and run the following script:
```powershell
./build_and_run.ps1
```

### Linux
#### Build the application
To build the application, open a terminal and run the following script:
```bash
./build.sh
```
#### Run the application
To run the application, open a terminal and run the following script:
```bash
./run.sh
```
#### Build and run the application
To build and run the application, open a terminal and run the following script:
```bash
./build_and_run.sh
```

### MacOS
#### Build the application
To build the application, open a terminal and run the following script:
```bash
./build.sh
```
#### Run the application
To run the application, open a terminal and run the following script:
```bash
./run.sh
```
#### Build and run the application
To build and run the application, open a terminal and run the following script:
```bash
./build_and_run.sh
```
## Deinstallation
To deinstall the application, simply delete the folder containing the application.

## Licenses and Attributions

This project uses the following open-source software:

| Library                                                                       | License                                                       |
|-------------------------------------------------------------------------------|---------------------------------------------------------------|
| [JUnit Jupiter API](https://junit.org/junit5/)                                | [Eclipse Public License v2.0 (EPL-2.0)](LICENSES/EPL-2.0.txt) |
| [JUnit Jupiter Engine](https://junit.org/junit5/)                             | [Eclipse Public License v2.0 (EPL-2.0)](LICENSES/EPL-2.0.txt) |
| [Selenium Java](https://www.selenium.dev/)                                    | [Apache License 2.0](LICENSES/Apache-2.0.txt)                 |
| [Selenium Logger](https://github.com/titusfortner/selenium-logger)            | [MIT License](LICENSES/MIT.txt)                               |
| [Mockito Core](https://site.mockito.org/)                                     | [MIT License](LICENSES/MIT.txt)                               |
| [Mockito JUnit Jupiter](https://site.mockito.org/)                            | [MIT License](LICENSES/MIT.txt)                               |
| [System Lambda](https://github.com/stefanbirkner/system-lambda)               | [MIT License](LICENSES/MIT.txt)                               |
| [Apache PDFBox](https://pdfbox.apache.org/)                                   | [Apache License 2.0](LICENSES/Apache-2.0.txt)                 |
| [Jackson Core](https://github.com/FasterXML/jackson-core)                     | [Apache License 2.0](LICENSES/Apache-2.0.txt)                 |
| [Jackson Dataformat XML](https://github.com/FasterXML/jackson-dataformat-xml) | [Apache License 2.0](LICENSES/Apache-2.0.txt)                 |
