package com.compiler.ast;

import com.compiler.hashtag.JavaSymbol;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */
public class Data {

    //data from Symbol
    private String lexeme;
    private String token;
    private int line;
    private int column;
    private int direction;
    private int context;
    private String type;
    private Object value;
    private SymbolTable table;

    public Data(String lex, String tok, Object val, int lin, int col) {
        lexeme = lex;
        token = tok;
        line = lin;
        column = col;
        context = -1;
        type = "null";
        value = null;
        table = null;
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
                value = (FunctionType) val;
            }
        }
    }


    public Data(JavaSymbol symbol) {
        lexeme = symbol.getLexeme();
        token = symbol.getTokenName();
        line = symbol.getLine();
        column = symbol.getColumn();
        type = "null";
        context = -1;
        value = table = null;
    }

    public Data() { //can't think of a use for this, but i'll leave it just in case...
        lexeme = token = type = "null";
        line = column = context = -1;
        value = null;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getToken() {
        return token;
    }

    public int getContext() {
        return context;
    }

    public Data setContext(int context) {
        this.context = context;
        return this;
    }

    public int getLine() {
        return line;
    }

    public SymbolTable getScope() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    public int getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int dir) {
        this.direction = dir;
    }

    public void setValue(Object obj) { //sets the value while updating the type if necessary
        if (obj != null) {
            if (obj instanceof String) {
                value = obj;
                //fix column number
                column -= ((String) obj).length() - 1;
            } else if (obj instanceof Integer) {
                value = ((Integer) obj).intValue();
            } else if (obj instanceof Boolean) {
                value = ((Boolean) obj).booleanValue();
            } else if (obj instanceof Character) {
                value = ((Character) obj).charValue();
            } else if (obj instanceof Double) {
                value = ((Double) obj).doubleValue();
            } else if (obj instanceof FunctionType) {
                value = (FunctionType) obj;
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                ", scope=" + (this.getToken().equalsIgnoreCase("identifier") ? table.getID() : "scope...?") +
                ", context=" + context +
                '}';
    }

    public String getTabularForm() {
        //for CSV saving purposes (makes it easier to visualize, I think)
        return "" + lexeme + "," + token + "," + type + "," + value + "," + line + "," + column + "," + table.getID() + "," + direction;
    }
}
