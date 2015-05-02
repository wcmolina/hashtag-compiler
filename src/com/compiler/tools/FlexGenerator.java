package com.compiler.tools;

import jflex.SilentExit;

public class FlexGenerator {

    public static void main(String[] args) {
        String[] opts = new String[3];
        opts[0] = "-d";
        opts[1] = "src\\com\\compiler\\hashtag";
        opts[2] = "res\\lexers\\hashtag.flex";
        try {
            jflex.Main.generate(opts);
        } catch (SilentExit silentExit) {
            silentExit.printStackTrace();
        }
    }
    
}
