ANALYSIS:
See analysis of algorithm in "JulieGarciaLabStrassen - ANALYSIS.pdf"

INPUT:
Input file must be named xxxxxInput.txt, where xxxxx can be any length text. The input file should be placed in the same directory as the .exe file or you should include the path in the argument sent on the command line (see TO RUN section below).

I have included three input files: LabStrassenGoodInput.txt, LabStrassenWrongSizeInput.txt, and LabStrassenFormatErrorsInput.txt

New Input files should be formatted as follows:
The first line contains the order of the matrix, then follow this by the first matrix, in row major order, reading a row at a time, then the second matrix. Then put a blank line, then the order of the next multiplication and so on. So the beginning of the file with required input will look like this.
2
2 1
1 5
6 7
4 3

4
3 2 1 4 
-1 2 0 1
2 3 -1 -2
5 1 1 0
-1 2 -1 0
3 -1 0 2
-4 0 -3 1
0 -2 1 2

.
.
.

OUTPUT:
The outputfile will be placed in the same directory as the input file and will be named using the name of the input file. xxxxxOutput.txt. CAUTION: be aware that everytime you run the application with the same input file, it will overwrite the previous output file.

TO RUN:
Run ProgrammingProjectStrassenLab.exe with argument to input file. See INPUT section for input formatting instructions. 

"ProgrammingProjectStrassenLab.exe LabStrassenGoodInput.txt"

or if you put your input file in a different location

"ProgrammingProjectStrassenLab.exe /path/to/file/LabStrassenGoodInput.txt"