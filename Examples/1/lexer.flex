import java.io.*;
%%

%unicode
%class Lexer
%int
%line
%standalone

%{
	Writer writer = null;
%}

%init{
	try {
	    writer = new BufferedWriter(new OutputStreamWriter(
	          new FileOutputStream("output.txt"), "utf-8"));
	} catch (IOException ex) {
		System.out.println(ex.getMessage());
	}
%init}

%eof{
	try {writer.close();} catch (Exception e) {System.out.println(e.getMessage());}
%eof}

blanklines = [\r\n]
tabs = [\t*]
%%

<YYINITIAL> {
	{blanklines}	{}
	{tabs}	{}
	. {writer.write(yytext());}
}
