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
    private String type;
    private boolean declared;
    private Object assigned_value;

    /*
    Constructor for IDs, mostly for assign and declare
        * lex: the current lexeme of the symbol sent
        * tok: the type of token the lexeme is part of
        * val: the assigned value to the identifier. null if none
        * decl: true if the variable has been declared, false if not
        * l: line
        * col: column
    * */
    public Data(String lex, String tok, Object val, boolean decl, int l, int col) {
        lexeme = lex;
        token = tok;
        line = l;
        column = col;
        declared = decl;
        type = "";
        assigned_value = null;

        //get the type from val
        if (val != null) {
            if (val instanceof String) {
                type = "string";
                assigned_value = val;
            } else if (val instanceof Integer) {
                type = "int";
                assigned_value = ((Integer) val).intValue();
            } else if (val instanceof Boolean) {
                type = "boolean";
                assigned_value = ((Boolean) val).booleanValue();
            } else if (val instanceof Character) {
                type = "char";
                assigned_value = ((Character) val).charValue();
            } else if (val instanceof Double) {
                type = "double";
                assigned_value = ((Double) val).doubleValue();
            }
        }
    }

    public Data() { //used only for non-relevant tokens, where their type is all I need
        lexeme = token = type = "";
        line = column = -1;
        declared = false;
        assigned_value = null;
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
        return assigned_value;
    }

    public void setAssignValue(Object obj) {
        if (obj != null) {
            if (obj instanceof String) {
                type = "string";
                assigned_value = obj;
            } else if (obj instanceof Integer) {
                type = "int";
                assigned_value = ((Integer) obj).intValue();
            } else if (obj instanceof Boolean) {
                type = "boolean";
                assigned_value = ((Boolean) obj).booleanValue();
            } else if (obj instanceof Character) {
                type = "char";
                assigned_value = ((Character) obj).charValue();
            } else if (obj instanceof Double) {
                type = "double";
                assigned_value = ((Double) obj).doubleValue();
            }
        }
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
                ", assigned_value=" + assigned_value +
                '}';
    }

    public String tabularData() { //for CSV saving purposes (makes it easier I think)
        return "" + lexeme + "," + token + "," + declared + "," + type + "," + assigned_value + "," + line + "," + column;
    }
}
