package com.compiler.tools;

public class CupGenerator {

    public static void main(String[] args) {
        String opts[] = new String[5];
        opts[0] = "-destdir";
        opts[1] = "src\\com\\compiler\\hashtag";
        opts[2] = "-parser";
        opts[3] = "Parser";
        opts[4] = "res\\parsers\\parser.cup";
        //opts[4] = "src\\hashtag\\test.cup";

        try {
            java_cup.Main.main(opts);
        } catch (Exception e) {
        }
    }
}
