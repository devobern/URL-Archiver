# URL-Archiver

The URL archiver enables the extraction of URLs from any Unicode text or PDF file and allows for interactive archiving
on one of the supported archiving services.
> ⚠️ The application was designed to be platform-independent. However, it has only been tested on the following systems,
> so it cannot be guaranteed to work without restrictions on other platforms.
> - Windows 11 (Version 23H2)
> - Windows 10 (Version 22H2)
> - macOS (Ventura)
> - Ubuntu (20.04.3 LTS)

## Authors

- [Nicolin Dora](mailto:nicolin.dora@students.bfh.ch)
- [Abidin Vejseli](mailto:abidin.vejseli@students.bfh.ch)
- [Kilian Wampfler](mailto:kilian.wampfler@students.bfh.ch)

## Supervisor

- [Frank Helbling](mailto:frank.helbling@bfh.ch) (Project Management)
- [Dr. Simon Kramer](mailto:simon.kramer@bfh.ch) (Technical)

## Installation

### Requirements

To build and start the application, ensure that the following dependencies are installed on your system:
- Git: Latest stable version recommended.
- Maven: Version 3.8 or higher.
- Java: Version 21.

### Clone the repository

To clone the repository, run the following command in a terminal:

```bash
git clone https://github.com/devobern/URL-Archiver.git
```

### Build and run scripts

The build and run scripts are provided for Windows (`build.ps1`, `run.ps1`, `build_and_run.ps1`), Linux and
MacOS (`build.sh`, `run.sh`, `build_and_run.sh`). The scripts are located in the root directory of the project.
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

## User Manual

> ⚠️ To follow the instructions in this section, the application must be built, see [Installation](#installation).

The URL-Archiver is a user-friendly application designed for extracting and archiving URLs from text and PDF files. Its
intuitive interface requires minimal user input and ensures efficient management of URLs.

### Getting Started

#### Windows

Open Command Prompt, navigate to the application's directory, and execute:

```powershell
./run.ps1
```

#### Linux / MacOS

Open Terminal, navigate to the application's directory, and run:

```bash
./run.sh
```

### Operating Instructions

Upon launch, provide a path to a text or PDF file, or a directory containing such files. The application will process
and display URLs sequentially.

#### Navigation

Use the following keys to navigate through the application:

- o: Open the current URL in the default web browser.
- a: Access the Archive Menu to archive the URL.
- s: Show a list of previously archived URLs.
- u: Update and view pending archive jobs.
- n: Navigate to the next URL.
- q: Quit the application.
- c: Change application settings.
- h: Access the Help Menu for assistance.

#### Archiving URLs

Choose between archiving to Wayback Machine, Archive.today, both, or canceling.

When opting to use Archive.today for archiving, an automated browser session will initiate, requiring you to complete a captcha. Once resolved, the URL is archived, and the corresponding archived version is then collected and stored within the application.
#### Configuration

Customize Access/Secret Keys and the default browser. Current settings are shown with default values in brackets.

#### Exiting

- To exit, press q. If a Bibtex file was provided, you'll be prompted to save the archived URLs in the Bibtex file.
- Otherwise, or after saving the URLs in the Bibtex file, you'll be prompted to save the archived URLs in a CSV file.

For Bibtex entries:
- Without an existing note field, URLs are added as: note = {Archived Versions: \url{url1}, \url{url2}}
- With a note field, they're appended as: note = {<current note>, Archived Versions: \url{url1}, \url{url2}}

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
