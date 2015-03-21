package hashtag;

import java.io.File;

public class LexerMain {

    public static void main(String[] args) {

       jflex.Main.generate(new File("src\\hashtag\\hashtag.flex"));
    }
    
}
