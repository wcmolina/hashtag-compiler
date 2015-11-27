package com.compiler.hashtag;
import java_cup.runtime.Symbol;
%%

%public
%class Lexer
%int
%unicode
%line
%column
%cup


%{

    JavaSymbol cur_sym = null;
    StringBuilder string = new StringBuilder();
    StringBuilder comment = new StringBuilder();

    private Symbol symbol(int type){
        return new JavaSymbol(type,yyline+1,yycolumn+1,yytext());
    }

    private Symbol symbol (int type, Object value){
        return new JavaSymbol(type,yyline+1,yycolumn+1,yytext(),value);
    }

    //apparently this returns the 'lookahead' symbol, not the current one
    public JavaSymbol current_symbol() {
        return cur_sym;
    }
%}

%eofval{
  return symbol(SymbolConstants.EOF);
%eofval}



/*----------MACROS------------*/
DIGITO=[0-9]+
LETRA=[a-zA-Z]
ALPHANUMERICO={DIGITO}|{LETRA}
GUIONBAJO=[_]
ESPACIO=[" "]
SALTOLINEA=[\n\t\r]
IDENTIFICADOR={LETRA}({ALPHANUMERICO}|{GUIONBAJO})*
NUMERO= {DIGITO}
REAL= {DIGITO}"."{DIGITO}
CARACTER= ' . '
LLAVEIZQ=[{]
LLAVEDER=[}]
COMILLAS=[\"]
CONTENIDOCOMENT=([^}])*
HASHTAG=[#]
COMENTARIOUNALINEA={HASHTAG}.*

/*--------ESTADOS------------*/
%state COMMENT
%state SSTRING 
%%

<YYINITIAL> {
    {COMILLAS}              {string.setLength(0); yybegin(SSTRING);}
    {LLAVEIZQ}              {comment.setLength(0); yybegin(COMMENT);}
    {COMENTARIOUNALINEA}    {}
    {NUMERO}                {return symbol(SymbolConstants.NUMERO, new Integer(yytext()));}
    {REAL}                  {return symbol(SymbolConstants.REAL, new Double(yytext()));}
    {CARACTER}              {return symbol(SymbolConstants.CARACTER, new Character(yytext().charAt(1)));}  
    {SALTOLINEA}            {}
    {ESPACIO}               {}
    
    /*-----------------------------------------OPERADORES----------------------------------*/
    "+"              {return symbol(SymbolConstants.SUMA);}
    "-"              {return symbol(SymbolConstants.MENOS);}
    "/"              {return symbol(SymbolConstants.DIV);}
    "*"              {return symbol(SymbolConstants.MULT);}
    ">"              {return symbol(SymbolConstants.MAYOR);}
    "<"              {return symbol(SymbolConstants.MENOR);}
    ">="             {return symbol(SymbolConstants.MAYORIGUAL);}
    "<="             {return symbol(SymbolConstants.MENORIGUAL);}
    "not"            {return symbol(SymbolConstants.NOT);}
    "!="             {return symbol(SymbolConstants.DIFERENTE);}
    "=="             {return symbol(SymbolConstants.IGUAL);}
    "="              {return symbol(SymbolConstants.ASIGNACION);}
    /*---------------------------------------SIGNOS----------------------------------------*/
    "("              {return symbol(SymbolConstants.PARIZQ);}
    ")"              {return symbol(SymbolConstants.PARDER);}
    "%"              {return symbol(SymbolConstants.MOD);}
    ","              {return symbol(SymbolConstants.COMA);}
    ";"              {return symbol(SymbolConstants.PUNTOCOMA);}
    ":"              {return symbol(SymbolConstants.DOSPUNTOS);}
    /*------------------------------TIPOS DE DATOS-----------------------------------------*/
    "int"            {return symbol(SymbolConstants.INT);}
    "double"         {return symbol(SymbolConstants.DOUBLE);}
    "char"           {return symbol (SymbolConstants.CHAR);}
    "string"         {return symbol(SymbolConstants.STRING);}
    "boolean"        {return symbol(SymbolConstants.BOOLEAN);}
    /*-----------------------------PALABRAS RESERVADAS-------------------------------------*/
    "and"            {return symbol(SymbolConstants.AND);}
    "or"             {return symbol(SymbolConstants.OR);}
    "for"            {return symbol(SymbolConstants.FOR);}
    "if"             {return symbol(SymbolConstants.IF);}
    "else"           {return symbol(SymbolConstants.ELSE);}
    "while"          {return symbol(SymbolConstants.WHILE);}
    "function"       {return symbol(SymbolConstants.FUNCTION);}
    "mainbegin"      {return symbol(SymbolConstants.MAINBEGIN);}
    "begin"          {return symbol(SymbolConstants.BEGIN); }
    "switch"         {return symbol(SymbolConstants.SWITCH);}
    "case"           {return symbol(SymbolConstants.CASE);}
    "do"             {return symbol(SymbolConstants.DO);}
    "end"            {return symbol(SymbolConstants.END); }
    "true"           {return symbol(SymbolConstants.TRUE);}
    "false"          {return symbol(SymbolConstants.FALSE);}
    "other"          {return symbol(SymbolConstants.OTHER);}
    "break"          {return symbol(SymbolConstants.BREAK);}
    "return"         {return symbol(SymbolConstants.RETURN);}
    "readint"        {return symbol(SymbolConstants.READINT);}
    "readdouble"     {return symbol(SymbolConstants.READDOUBLE);}
    "readstring"     {return symbol(SymbolConstants.READSTRING);}
    "readchar"       {return symbol(SymbolConstants.READCHAR);}
    "print"          {return symbol(SymbolConstants.PRINT);}
    "void"           {return symbol(SymbolConstants.VOID);}
    {IDENTIFICADOR}  {return symbol(SymbolConstants.IDENTIFICADOR, yytext());}
    [^]              {int l = yyline+1; int c = yycolumn+1;
                        Editor.console.setText(Editor.console.getText()+"Error: (line: " + l + ", column: " + c + "). Unrecognized token " + yytext() + " : Lexical error\n");
                    }
}

<COMMENT> {
    {CONTENIDOCOMENT}   {comment.append(yytext());}
    {LLAVEDER}          {yybegin (YYINITIAL);}
}

<SSTRING>{
    
    {COMILLAS}          {yybegin (YYINITIAL); return symbol(SymbolConstants.CADENA, string.toString());  }
    .                   { string.append(yytext());}
} 
