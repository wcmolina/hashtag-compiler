package com.compiler.syntaxhighlight;

public class Token {

    public final int type;
    public final int start;
    public final int length;

    public Token(int type, int start, int length) {
        this.type = type;
        this.start = start;
        this.length = length;
    }
}
