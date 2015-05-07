package com.compiler.hashtag;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */
public class Data {

    //data from Symbol
    private String lexeme;
    private String token;
    private int line;
    private int column;

    //other useful information
    private String type;
    private boolean declared;
    private Object assign_value;

    //for Identifiers, mostly for init and declare
    public Data(String lex, String tok, Object val, boolean decl, int l, int col) {
        lexeme = lex;
        token = tok;
        type = "";
        assign_value = val;
        line = l;
        column = col;
        declared = decl;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getToken() {
        return token;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public Object getAssignValue() {
        return assign_value;
    }

    public String getType() {
        return type;
    }

    public boolean isDeclared() {
        return declared;
    }

    public void setDeclared(boolean decl) {
        declared = decl;
    }

    public void setType(String t) {
        this.type = t;
    }

    @Override
    public String toString() {
        return "Data {" +
                "lexeme='" + lexeme + '\'' +
                ", token='" + token + '\'' +
                ", line=" + line +
                ", column=" + column +
                ", type='" + type + '\'' +
                ", declared=" + declared +
                ", assign_value=" + assign_value +
                '}';
    }

    public String tabularData(){ //for CSV saving purposes (makes it easier I think)
        return ""+lexeme+","+token+","+declared+","+type+","+assign_value+","+line+","+column;
    }
}
