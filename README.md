# URL-Archiver
The URL archiver enables the extraction of URLs from any Unicode text or PDF file and allows for interactive archiving on one of the supported archiving services.
> ⚠️ The application was designed to be platform-independent. However, it has only been tested on the following systems, so it cannot be guaranteed to work without restrictions on other platforms.
> - Windows 10/11
> - macOS Sonoma
> - Ubuntu 20.04.3 LTS.

## Authors
- [Mr. Nicolin Dora](mailto:nicolin.dora@students.bfh.ch)
- [Mr. Abidin Vejseli](mailto:abidin.vejseli@students.bfh.ch)
- [Mr. Kilian Wampfler](mailto:kilian.wampfler@students.bfh.ch)

## Supervisor
- [Dr. Simon Kramer](mailto:simon.kramer@bfh.ch) (Technical)
- [Mr. Frank Helbling](mailto:frank.helbling@bfh.ch) (Project Management)
## Installation
### Requirements
- git
- Maven
- Java 21
### Clone the repository
To clone the repository, run the following command in a terminal:
```bash
git clone https://github.com/devobern/URL-Archiver.git
```
### Build and run scripts
The build and run scripts are provided for Windows (`build.bat`, `run.bat`, `build_and_run.bat`), Linux and MacOS (`build.sh`, `run.sh`, `build_and_run.sh`). The scripts are located in the root directory of the project.
> ⚠️ The scripts need to be executable. To make them executable, run the following command in a terminal: 
> - Linux / MacOS: `chmod +x build.sh run.sh build_and_run.sh` 
> - Windows: `chmod +x build.bat run.bat build_and_run.bat`

### Windows
#### Build the application
To build the application, open a command prompt and run the following script:
```bash
./build.bat
```
#### Run the application
To run the application, open a command prompt and run the following script:
```bash
./run.bat
```
#### Build and run the application
To build and run the application, open a command prompt and run the following script:
```bash
./build_and_run.bat
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

## ## Licenses and Attributions

This project uses the following open-source software:

- JUnit Jupiter API (https://junit.org/junit5/), licensed under the Eclipse Public License v2.0 (EPL-2.0). A copy of the EPL-2.0 can be found [here](LICENSES/EPL-2.0.txt).
- JUnit Jupiter Engine (https://junit.org/junit5/), licensed under the Eclipse Public License v2.0 (EPL-2.0). A copy of the EPL-2.0 can be found [here](LICENSES/EPL-2.0.txt).
- Selenium Java (https://www.selenium.dev/), licensed under the Apache License 2.0. A copy of the Apache License 2.0 can be found [here](LICENSES/Apache-2.0.txt).
- Selenium Logger (https://github.com/titusfortner/selenium-logger), licensed under the MIT License. A copy of the MIT License can be found [here](LICENSES/MIT.txt).
- Mockito Core (https://site.mockito.org/), licensed under the MIT License. A copy of the MIT License can be found [here](LICENSES/MIT.txt).
- Mockito JUnit Jupiter (https://site.mockito.org/), licensed under the MIT License. A copy of the MIT License can be found [here](LICENSES/MIT.txt).
- System Lambda (https://github.com/stefanbirkner/system-lambda), licensed under the MIT License. A copy of the MIT License can be found [here](LICENSES/MIT.txt).
- Apache PDFBox (https://pdfbox.apache.org/), licensed under the Apache License 2.0. A copy of the Apache License 2.0 can be found [here](LICENSES/Apache-2.0.txt).
- Jackson Core (https://github.com/FasterXML/jackson-core), licensed under the Apache License 2.0. A copy of the Apache License 2.0 can be found [here](LICENSES/Apache-2.0.txt).
- Jackson Dataformat XML (https://github.com/FasterXML/jackson-dataformat-xml), licensed under the Apache License 2.0. A copy of the Apache License 2.0 can be found [here](LICENSES/Apache-2.0.txt).