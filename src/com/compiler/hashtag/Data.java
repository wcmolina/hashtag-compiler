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
    private Object value;
    private Scope scope;

        /*
        helpful for this case, for example:
        * int x;
        * x = x+1;
        * should give an error: var 'x' has not been initialized.
        *
        * so x is DECLARED, not INIT_AND_DECLARED
        * see arithmetic expression handler (TreeAnalyzer)
        * */

    private static final int DECLARED = 0;
    private static final int INIT_AND_DECLARED = 1;
    private static final int CALLED = 2;
    private int context;

    /*
    Constructor for IDs, mostly for assign and declare
        * lex: the current lexeme of the symbol sent
        * tok: the type of token the lexeme is part of
        * val: the value the token has
        * l: line
        * col: column
    * */
    public Data(String lex, String tok, Object val, int l, int col) {
        lexeme = lex;
        token = tok;
        line = l;
        column = col;
        type = "";
        value = null;
        context = -1;
        //get the type from val
        if (val != null) {
            if (val instanceof String) {
                type = "string";
                value = val;
                //correct column number
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
            }
        }
    }

    public Data() { //used only for non-relevant tokens, where their type is all I need
        lexeme = token = type = "";
        line = column = context = -1;
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
                //correct column number
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
            }
        }
    }

    public int getContext() {
        return context;
    }

    public void setContext(int con) {
        this.context = con;
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
                '}';
    }

    public String getTabularForm() { //for CSV saving purposes (makes it easier I think)
        return "" + lexeme + "," + token + "," + type + "," + value + "," + line + "," + column;
    }
}
