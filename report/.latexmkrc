##
## Default PDF viewer -- system dependent setting
##    See the documentation -- https://mirror.foobar.to/CTAN/support/latexmk/latexmk.pdf
## Windows
#$pdf_previewer = 'start acroread %O %S';
## OS X
#$pdf_previewer = 'open %S';
## GNU/Linux (Debian/Ubuntu like)
$pdf_previewer = 'start xdg-open';

##
## EPS to PDF conversion hook
##
@cus_dep_list = (@cus_dep_list, "eps pdf 0 eps2pdf");
sub eps2pdf {
   system("epstopdf $_[0].eps");
   }

##
## GLO to GLS conversion hook
##
add_cus_dep( 'slo', 'sls', 0, 'makeglossaries' );
add_cus_dep( 'acn', 'acr', 0, 'makeglossaries' );
add_cus_dep( 'glo', 'gls', 0, 'makeglossaries' );
$clean_ext .= "slo sls slg acr acn alg glo gls glg";
sub makeglossaries {
   my ($base_name, $path) = fileparse( $_[0] );
   pushd $path;
   my $return = system "makeglossaries", $base_name;
   popd;
   return $return;
}

##
## To enable shell-escape for all *latex commands  
##   Used i.e. for svg package invoking inkscape
##
set_tex_cmds( '--shell-escape %O %S' );

##
## Set default TeX file
##
#@default_files = ('tutorial-stm32cubeide-installation.tex');

##
## Latexmk build properties
##
$pdf_mode = 4;
$postscript_mode = $dvi_mode = 0;
$bibtex_use = 1;

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
$clean_ext .= '%R.aux %R.dvi %R.log %R.out tex~';
$clean_full_ext = 'bbl synctex.gz _build';
