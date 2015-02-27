import java.io.*;
%%

%class Lexer
%int
%unicode
%line
%column


%{
	Writer writer = null;
%}

%init{
	try {
	    writer = new BufferedWriter(new OutputStreamWriter(
	          new FileOutputStream("tokens.txt"), "utf-8"));
	} catch (IOException ex) {
		System.out.println(ex.getMessage());
	}
%init}

%eof{
	try {writer.close();} catch (Exception e) {System.out.println(e.getMessage());}
%eof}

//--------ESTADOS------------
%state COMMENT

//----------MACROS------------
//----------PALABRAS RESERVADAS--------------
AND 				= and
OR 					= or
FOR 				= for
IF 					= if
THEN 				= then
ELSE 				= else
WHILE 				= while
ENDIF 				= endif
ENDWHILE 			= endwhile
BEGIN 				= begin
END 				= end
SWITCH 				= switch
CASE 				= case
DO 					= do
TRUE 				= true
FALSE 				= false
PUBLIC 				= public
PRIVATE 			= private
CLASS 				= class
NEW 				= new
VOID 				= void
BREAK 				= break
RETURN 				= return
PRINT 				= print


//-----------------TIPOS DE DATOS-------------------
INT 				= int
CHAR 				= char
BOOLEAN 			= bool
STRING 				= string
FLOAT 				= float

//------------------OPERADORES----------------------
MAYOR 				= \>
MENOR 				= \<
MENORIGUAL 			= \<\=
MAYORIGUAL 			= \>\=
DISTINTO 			= \<\>
IGUAL 				= equals
MAS 				= \+
MENOS 				= \-
POR 				= \*
ENTRE 				= \/
NOT 				= \!
MOD 				= \%
ASIG 				= \=
PUNTO 				= \.

//---------------------------------------------------
DIGITO				= [0-9]+
LETRA				= [a-zA-Z]
ALPHANUMERICO		= {DIGITO}|{LETRA}
GUIONBAJO			= [_]
ESPACIO				= [" "]
IDENTIFICADOR		= {LETRA}({ALPHANUMERICO}|{GUIONBAJO})*
NUMERO				= {DIGITO}
REAL				= {DIGITO}.{DIGITO}
LLAVEIZQ			= [\{]
LLAVEDER			= [\}]
CONTENIDOCOMMENT	= [^}\n]*
//------------------------------------------------------

%%
<YYINITIAL> {

	{LLAVEIZQ}       	{writer.write("Token found: " + yytext() + " is a LLAVEIZQ"+"\n");}      
	{NUMERO}         	{writer.write("Token found: " + yytext() + " is a NUMERO"+"\n");}
	{REAL}           	{writer.write("Token found: " + yytext() + " is a REAL"+"\n");}
	{ESPACIO}        	{writer.write("Token found: " + yytext() + " is a ESPACIO"+"\n");}

	//-----------------------------------------OPERADORES----------------------------------
	{MAS}              	{writer.write("Token found: " + yytext() + " is a MAS"+"\n");}
	{MENOS}             {writer.write("Token found: " + yytext() + " is a MENOS"+"\n");}
	{ENTRE}             {writer.write("Token found: " + yytext() + " is a ENTRE"+"\n");}
	{POR}              	{writer.write("Token found: " + yytext() + " is a POR"+"\n");}
	{MAYOR}             {writer.write("Token found: " + yytext() + " is a MAYOR"+"\n");}
	{MENOR}             {writer.write("Token found: " + yytext() + " is a MENOR"+"\n");}
	{MAYORIGUAL}        {writer.write("Token found: " + yytext() + " is a MAYORIGUAL"+"\n");}
	{MENORIGUAL}        {writer.write("Token found: " + yytext() + " is a MENORIGUAL"+"\n");}
	{NOT}              	{writer.write("Token found: " + yytext() + " is a NOT"+"\n");}
	{DISTINTO}          {writer.write("Token found: " + yytext() + " is a DISTINTO"+"\n");}
	{IGUAL}             {writer.write("Token found: " + yytext() + " is a IGUAL"+"\n");}
	{MOD}              	{writer.write("Token found: " + yytext() + " is a MOD"+"\n");}
	{ASIG}              {writer.write("Token found: " + yytext() + " is a ASIG"+"\n");}
	{PUNTO}             {writer.write("Token found: " + yytext() + " is a PUNTO"+"\n");}

	//---------------------------------------SIGNOS----------------------------------------
	"("              	{writer.write("Token found: " + yytext() + " is a PARIZQ"+"\n");}
	")"              	{writer.write("Token found: " + yytext() + " is a PARDER"+"\n");}
	","              	{writer.write("Token found: " + yytext() + " is a COMA"+"\n");}
	";"              	{writer.write("Token found: " + yytext() + " is a PUNTOCOMA"+"\n");}
	\"               	{writer.write("Token found: " + yytext() + " is a COMILLA"+"\n");}
	"?"              	{writer.write("Token found: " + yytext() + " is a SIGNOINTERROGACION"+"\n");}
	"'"              	{writer.write("Token found: " + yytext() + " is a UNACOMILLA"+"\n");} 

	//------------------------------TIPOS DE DATOS-----------------------------------------
	{INT}            	{writer.write("Token found: " + yytext() + " is a INT"+"\n");}
	{CHAR}           	{writer.write("Token found: " + yytext() + " is a CHAR"+"\n");}
	{BOOLEAN}			{writer.write("Token found: " + yytext() + " is a BOOLEAN"+"\n");}
	{STRING}         	{writer.write("Token found: " + yytext() + " is a STRING"+"\n");}
	{FLOAT}				{writer.write("Token found: " + yytext() + " is a FLOAT"+"\n");}

	//-----------------------------PALABRAS RESERVADAS-------------------------------------
	{AND}            	{writer.write("Token found: " + yytext() + " is a AND"+"\n");}
	{OR}         		{writer.write("Token found: " + yytext() + " is a OR"+"\n");}
	{FOR}             	{writer.write("Token found: " + yytext() + " is a FOR"+"\n");}
	{IF}             	{writer.write("Token found: " + yytext() + " is a IF"+"\n");}
	{THEN}           	{writer.write("Token found: " + yytext() + " is a THEN"+"\n");}
	{ELSE}           	{writer.write("Token found: " + yytext() + " is a ELSE"+"\n");}
	{WHILE}          	{writer.write("Token found: " + yytext() + " is a WHILE"+"\n");}
	{ENDIF}          	{writer.write("Token found: " + yytext() + " is a ENDIF"+"\n");}
	{ENDWHILE}       	{writer.write("Token found: " + yytext() + " is a ENDWHILE"+"\n");}
	{BEGIN}          	{writer.write("Token found: " + yytext() + " is a BEGIN"+"\n");}
	{END}            	{writer.write("Token found: " + yytext() + " is a END"+"\n");}
	{SWITCH}         	{writer.write("Token found: " + yytext() + " is a SWITCH"+"\n");}
	{CASE}           	{writer.write("Token found: " + yytext() + " is a CASE"+"\n");}
	{DO}             	{writer.write("Token found: " + yytext() + " is a DO"+"\n");}
	{TRUE}           	{writer.write("Token found: " + yytext() + " is a TRUE"+"\n");}  
	{FALSE}				{writer.write("Token found: " + yytext() + " is a FALSE"+"\n");}
	{PUBLIC}         	{writer.write("Token found: " + yytext() + " is a PUBLIC"+"\n");}
	{PRIVATE}        	{writer.write("Token found: " + yytext() + " is a PRIVATE"+"\n");}
	{CLASS}          	{writer.write("Token found: " + yytext() + " is a CLASS"+"\n");}
	{NEW}            	{writer.write("Token found: " + yytext() + " is a NEW"+"\n");}
	{VOID}           	{writer.write("Token found: " + yytext() + " is a VOID"+"\n");}  
	{BREAK}          	{writer.write("Token found: " + yytext() + " is a BREAK"+"\n");} 
	{RETURN}         	{writer.write("Token found: " + yytext() + " is a RETURN"+"\n");}
	{PRINT}          	{writer.write("Token found: " + yytext() + " is a PRINT"+"\n");}        
	{IDENTIFICADOR}  	{writer.write("Token found: " + yytext() + " is a IDENTIFICADOR"+"\n");}
	[^]              	{writer.write("Token found: " + yytext() + " is a ERROR"+"\n");}
}

<COMMENT> {
	{CONTENIDOCOMMENT}   {}
	"\n"				{yyline++;}
	{LLAVEDER}          {yybegin (YYINITIAL);}
}