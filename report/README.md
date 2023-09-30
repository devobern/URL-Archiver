# LaTeX Template for BFH Reports (TI)
This is a LaTeX template to write a report in BFH like style.

#### Tested On

* Ubuntu 18.04, 20.04
* Debian 9, 10
* Windows 10

## Linux Users (Ubuntu/Debian)
Linux users can use both an TeX GUI and the CLI. The GUI approach works the same as on Windows and OS X. 

 * Install used LaTeX packages:
```bash
apt install texlive-base texlive-bibtex-extra texlive-binaries texlive-extra-utils texlive-formats-extra texlive-latex-base texlive-latex-extra texlive-latex-recommended texlive-pictures texlive-plain-generic texlive-science texlive-xetex

```
 * Install LaTeX language packages:
```bash
apt install texlive-lang-english texlive-lang-french texlive-lang-german
```
 * Install used fonts packages:
```bash
apt install texlive-fonts-extra texlive-fonts-extra-links texlive-fonts-recommended texlive-font-utils
```
 * Install used build environment packages:
```bash
apt install latexmk
```
 * Install used converter packages:
```bash
apt install inkscape libcanberra-gtk-module
```
### LaTeX GUI Editor
 * [LaTeXila](https://wiki.gnome.org/Apps/GNOME-LaTeX) -- editing TeX files (Linux only)
```bash
apt install latexila
```
 * [TexStudio](https://www.texstudio.org/) -- editing TeX files (an alternative to LeTeXila)
```bash
apt install texstudio
```
## Windows Users
### Windows Users
 * MikTeX (LaTeX package manager  for Windows)
   * https://miktex.org/download
 * Install latexmk
    * If it’s not installed already, open the MikTeX Package Manager and install the `latexmk` package.
 * Install perl for latexmk
   * (recommended) https://strawberryperl.com/
   * (alternative) https://www.perl.org/
 * Install TeXStudio (Multi-platform -> chose appropriate installer)
   * https://www.texstudio.org/
 * The LaTeX package "svg" uses inkscape to include and convert svg files on-the-fly. Download and install inkscape.
   * https://inkscape.org/release/inkscape-1.0.2/
 * (Hint) If inkscape installation path is missing, add it i.e. to the environment variable "path"..
   * https://tex.stackexchange.com/questions/473994/svg-and-inkscape

## OS X Users
 * MacTeX (LaTeX package manager for OS X)
   
   * http://www.tug.org/mactex/
 * Install latexmk
    * It’s probably already installed. If not, open “TeX Live Utility”, search for “latexmk” and install  it. If you prefer using the Terminal:.
    ```bash
    sudo tlmgr install latexmk
    ```
 * Install TeXStudio (Multi-platform -> chose appropriate installer)
   
   * https://www.texstudio.org/
 * The LaTeX package "svg" uses Inkscape to include and convert svg files on-the-fly. Download and install Inkscape.
   
   * https://inkscape.org/release/inkscape-1.0.2/
 * (Hint) If Inkscape installation path is missing, add it i.e. to the environment variable "path"..
   
   * https://tex.stackexchange.com/questions/473994/svg-and-inkscape

## All Platforms -- TeXStudio Settings

Use the LaTeX GUI tool and open the main document "reportTitle.tex". Apply the settings given below when you start TeXstudio the first time. There is a file deployed with the template called ".latexmkrc" which holds some default settings. The settings are used to build the report because the selected build system will be "Latexmk". Using Latexmk has several advantages and builds on the same way whether you use the CLI or the GUI approach. 

  1. Open the configuration window *Options* -> *Configure TeXstudio*
  2. Go to *Build* -> Change Default Compiler to *latexmk* and Default Bibliography Tool to *BibTeX*
  3. In the bottom left corner, activate -> *Show Advanced Options*
   * Build Options: Add as additional search paths **Log File**  -> *_build*
   * Build Options: Add as additional search paths **PDF File** path -> *_build*

When you applied the configurations as described above you are ready to use the template. Use fancy buttons to edit and build the LaTeX report. For further details about the GUI tool read the reference and/or user manual.

## CLI Usage (latexmk)
If you use cross-references, you often have to run LaTeX more than once, if you use BibTeX for your bibliography or if you want to have a glossary you even need to run external programs in-between.

To avoid all this hassle, you should simply use [Latexmk](https://personal.psu.edu/~jcc8/software/latexmk/)!

Latexmk is a Perl script which you just have to run once and it does everything else for you ... completely automatically.


Follow the instruction given below for a first test drive with Latexmk using the CLI. See the online reference of Latexmk for further information.

Replace "FILE" with the name of the main file. To build the template use "projectReport" in place of "FILE"

#### Running Latexmk
 * In the simplest case you just have to type
```bash
latexmk
```
 *This runs LaTeX on all .tex files in the current directory using the output format specified by the configuration files.*

 * If you want to make sure to get a .pdf file as output, just mention it:
```bash
latexmk -pdf
```
 * If you want to use latex instead of pdflatex but still want a .pdf file in the end, use
```bash
latexmk -pdfps
```
 * If you want to compile only one specific .tex file in the current directory, just provide the file name:
```bash
latexmk myfile.tex
```
 * If you want to preview the resulting output file(s), just use
```bash
latexmk -pv
```
 * And now the Killer Feature: If you want Latexmk to continuously check all input files for changes and re-compile the whole thing if needed and always display the result, type
```bash
latexmk -pvc
```
Then, whenever you change something in any of your source files and save your changes, the preview is automatically updated. But: This doesn’t work with all viewers, especially not with Adobe Reader. See the section about configuration files below for setting a suitable viewer application.

 * Of course, options can be combined, e.g.
```bash
latexmk -outdir=_build -pdf -pv myfile.tex
```

#### Cleaning Up
  * After running LaTeX, the current directory is contaminated with a myriad of temporary files; you can get rid of them with
```bash
latexmk -c
```
  * Previous command doesn’t delete the final .pdf/.ps/.dvi files. If you want to delete those too, use
```bash
latexmk -C
```

#### Configuration Files
*To customize some latexmk build setting such as the default viewer for the preview, edit the file ".latexmkrc".* A snippet with some good pre-configurations for latexmk is given below.

```bash
cat << "EOF" > .latexmkrc
##
## Adapt previewer settings
##
#$dvi_previewer = 'start xdvi -watchfile 1.5';
#$ps_previewer  = 'start gv --watch';
#$pdf_previewer = 'start evince';

##
## EPS to PDF conversion hook
##
@cus_dep_list = (@cus_dep_list, "eps pdf 0 eps2pdf");
sub eps2pdf {
   system("epstopdf $_[0].eps");
   }

##
## Set default TeX file
##
#@default_files = ('reportTitle.tex');

##
## Latexmk build properties
##
$pdf_mode = 1;
$bibtex_use = 1;
$latex = 'latex -interaction=nonstopmode -shell-escape';
$pdflatex = 'pdflatex -interaction=nonstopmode -shell-escape';

##
## Build directory
##
$out_dir = '_build';

##
## Post process hooks (Linux, OS X only; For Windows install used CLI tools)
##
## Copy PDF to a sub directory named "_output"
#$success_cmd = 'mkdir -p _output && cp _build/*.pdf _output/';
## Copy PDF to a sub directory named "_output" and create a link from top level to the PDF file
#$success_cmd = 'mkdir -p _output && cp _build/*.pdf _output/ && ln -s _output/%R.pdf';

##
## List of file extensions to clean up
## 
$clean_ext = '%R.aux %R.dvi %R.log %R.out tex~';
$clean_full_ext = 'bbl synctex.gz';
EOF
```

## Further Reading
 * [KOMA-Script Documentation](https://mirror.foobar.to/CTAN/macros/latex/contrib/koma-script/doc/scrguien.pdf)
 * [KOMA-Script HTML Doc Dir](https://mirror.foobar.to/CTAN/macros/latex/contrib/koma-script/doc/)
 * [Sci-Hub](https://sci-hub.st/)
 * [Document Structure](https://psychology.ucsd.edu/undergraduate-program/undergraduate-resources/academic-writing-resources/writing-research-papers/research-paper-structure.html)

 * [Google Scholar](https://scholar.google.com/)

#### Information about citation style in engineering/science

 * [IEEE Xplore](https://ieeexplore.ieee.org/Xplore/home.jsp)

#### Information about citation style in medicine

 * [Pub-Med](https://pubmed.ncbi.nlm.nih.gov/)

