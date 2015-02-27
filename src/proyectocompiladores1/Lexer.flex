package proyectocompiladores1;
import java_cup.runtime.*;
%%

%class Lexer
%int
%unicode
%line
%column
%cup


%{
    /*para los simbolos generales*/
    private Symbol symbol(int type){
      return new Symbol(type,yyline,yycolumn);
    }

    /*para el tipo de token con su valor*/
    private Symbol symbol (int type, Object value){
      return new Symbol(type,yyline,yycolumn,value);
    }

    public String Tokens="";
    StringBuffer string = new StringBuffer();
%}



//----------MACROS------------
DIGITO=[0-9]+
LETRA=[a-zA-Z]
ALPHANUMERICO={DIGITO}|{LETRA}
GUIONBAJO=[_]
ESPACIO=[" "]
SALTOLINEA=[\n\t\r]
IDENTIFICADOR={LETRA}({ALPHANUMERICO}|{GUIONBAJO})*
NUMERO= {DIGITO}
REAL= {DIGITO}"."{DIGITO}
LLAVEIZQ=[\{]
LLAVEDER=[\}]
COMILLAS=[\"]
CONTENIDOCOMENT=([^}])*
HASHTAG=[#]
COMENTARIOUNALINEA={HASHTAG}. 
//--------ESTADOS------------
%state COMMENT
%state SSTRING
%%
<YYINITIAL>
{
{COMILLAS}              {string.setLength(0);yybegin(SSTRING);}
{LLAVEIZQ}              {yybegin(COMMENT);}  
{COMENTARIOUNALINEA}  {/*Ignore*/}      
{NUMERO}                {return symbol(sym.NUMERO, yytext());}
{REAL}                  {return symbol(sym.REAL, yytext());}
{SALTOLINEA}            {/*Ignore*/}
{ESPACIO}               {/*Ignore*/}

//-----------------------------------------OPERADORES----------------------------------
"+"              {return symbol(sym.SUMA);}
"-"              {return symbol(sym.MENOS);}
"/"              {return symbol(sym.DIV);}
"*"              {return symbol(sym.MULT);}
">"              {return symbol(sym.MAYOR);}
"<"              {return symbol(sym.MENOR);}
">="             {return symbol(sym.MAYORIGUAL);}
"<="             {return symbol(sym.MENORIGUAL);}
"!"              {return symbol(sym.NOT);}
"!="             {return symbol(sym.DIFERENTE);}
"=="             {return symbol(sym.IGUAL);}
"="              {return symbol(sym.ASIGNACION);}
"."              {return symbol(sym.PUNTO);}
//---------------------------------------SIGNOS----------------------------------------
"("              {return symbol(sym.PARDER);}
")"              {return symbol(sym.PARIZQ);}
"%"              {return symbol(sym.MOD);}
","              {return symbol(sym.COMA);}
";"              {return symbol(sym.PUNTOCOMA);}
"?"              {return symbol(sym.INTERROGACION);}
"'"              {return symbol(sym.UNACOMILLA);}
"["              {return symbol(sym.CORCHETEDER);}
"]"              {return symbol(sym.CORCHETEIZQ);}
//------------------------------TIPOS DE DATOS-----------------------------------------
"int"            {return symbol(sym.INT, yytext());}
"double"           {return symbol(sym.DOUBLE, yytext());}
"char"             {return symbol(sym.CHAR);}
"string"           {return symbol(sym.STRING);}
"boolean"          {return symbol(sym.BOOLEAN);}
//-----------------------------PALABRAS RESERVADAS-------------------------------------
"and"            {return symbol(sym.AND);}
"equals"         {return symbol(sym.EQUALS);}
"or"             {return symbol(sym.OR);}
"for"            {return symbol(sym.FOR);}
"if"             {return symbol(sym.IF);}
"then"           {return symbol(sym.THEN);}
"else"           {return symbol(sym.ELSE);}
"while"          {return symbol(sym.WHILE);}
"endif"          {return symbol(sym.ENDIF);}
"endwhile"       {return symbol(sym.ENDWHILE);}
"mainbegin"      {return symbol(sym.MAINBEGIN); }
"begin"          {return symbol(sym.BEGIN); }
"end"            {return symbol(sym.END); }
"switch"         {return symbol(sym.SWITCH);}
"case"           {return symbol(sym.CASE);}
"do"             {return symbol(sym.DO);}
"true"           {return symbol(sym.TRUE);}
"false"          {return symbol(sym.FALSE);}
"class"          {return symbol(sym.CLASS);}
"void"           {return symbol(sym.VOID);}
"break"          {return symbol(sym.BREAK);}
"return"         {return symbol(sym.RETURN);}
"print"          {return symbol(sym.PRINT);}
{IDENTIFICADOR}  {return symbol(sym.IDENTIFICADOR, yytext());}
[^]              {throw new Error("Error Lexico, caracter <" + yytext() + "> ilegal, en la linea: " + yyline + "Columna: " + yycolumn);}

}

<COMMENT> {
{CONTENIDOCOMENT}   {/*IGNORE*/}
{LLAVEDER}          {yybegin (YYINITIAL);}
}

<SSTRING>{
{COMILLAS}          {
                        yybegin (YYINITIAL);
                    }

 .                  {string.append(yytext());}
}