package com.compiler.ast;

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
    private Object value;
    private Scope scope;

    /*
    Constructor for IDs, mostly for assign and declare
        * lex: the current lexeme of the symbol sent
        * tok: the type of token the lexeme is part of
        * val: the value the token has
        * l: line
        * col: column
    */

    public Data(String lex, String tok, Object val, int lin, int col) {
        lexeme = lex;
        token = tok;
        line = lin;
        column = col;
        type = "null";
        value = null;
        scope = null;
        //get the type from val
        if (val != null) {
            if (val instanceof String) {
                type = "string";
                value = val;
                //fix column number
                column -= ((String) val).length() - 1;
            } else if (val instanceof Integer) {
                type = "int";
                value = ((Integer) val).intValue();
            } else if (val instanceof Boolean) {
                type = "boolean";
                value = ((Boolean) val).booleanValue();
            } else if (val instanceof Character) {
                type = "char";
                value = ((Character) val).charValue();
            } else if (val instanceof Double) {
                type = "double";
                value = ((Double) val).doubleValue();
            } else if (val instanceof FunctionType) {
                type = "function";
                //assign the whole object... FunctionType should contain information about the function being declared.
                value = (FunctionType) val;
            }
        }
    }

    public Data() { //can't think of a use for this, but i'll leave it just in case...
        lexeme = token = type = "null";
        line = column = -1;
        value = null;
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

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public int getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object obj) { //sets the value while updating the type if necessary
        if (obj != null) {
            if (obj instanceof String) {
                type = "string";
                value = obj;
                //fix column number
                column -= ((String) obj).length() - 1;
            } else if (obj instanceof Integer) {
                type = "int";
                value = ((Integer) obj).intValue();
            } else if (obj instanceof Boolean) {
                type = "boolean";
                value = ((Boolean) obj).booleanValue();
            } else if (obj instanceof Character) {
                type = "char";
                value = ((Character) obj).charValue();
            } else if (obj instanceof Double) {
                type = "double";
                value = ((Double) obj).doubleValue();
            } else if (obj instanceof FunctionType) {
                type = "function";
                value = (FunctionType) obj;
            }
        }
    }


    public String getType() {
        return type;
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
                ", value=" + value +
                ", scope=" + scope.getID() +
                '}';
    }

    public String getTabularForm() { //for CSV saving purposes (makes it easier I think)
        return "" + lexeme + "," + token + "," + type + "," + value + "," + line + "," + column + "," + scope.getID();
    }
}
