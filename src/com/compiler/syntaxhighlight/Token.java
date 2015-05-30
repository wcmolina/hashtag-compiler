package com.compiler.syntaxhighlight;

public class Token {

    public final int type;
    public final int start;
    public final int length;
    public final String text;

    public Token(int type, int start, int length, String str) {
        this.type = type;
        this.start = start;
        this.length = length;
        this.text = str;
    }
}
